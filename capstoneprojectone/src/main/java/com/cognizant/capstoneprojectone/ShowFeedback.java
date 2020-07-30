package com.cognizant.capstoneprojectone;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.cognizant.capstoneprojectone.models.Feedback;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.Gson;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ShowFeedback implements RequestHandler<APIGatewayProxyRequestEvent , Map<String, Object>> {
    private DynamoDbClient dynamoDbClient = DynamoDbClient.builder().region(Region.US_WEST_2).build();
    @Override
    public Map<String, Object> handleRequest(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context) {

        GetRecordsRequest getRecordsRequest = null;
        GetItemRequest getItemRequest = null;
        ScanResponse feedback = dynamoDbClient.scan(ScanRequest.builder().tableName("feedback").build());
        List<Map<String, AttributeValue>> items = feedback.items();
        LambdaLogger logger = context.getLogger();
        logger.log(items.toString());


       List<Feedback> collect = items.stream()
                .map(data -> {
                    Feedback feedback1 = new Feedback();
                    feedback1.setId(data.get("id").s());
                    feedback1.setName((data.get("Name").s()));
                    feedback1.setRating(Feedback.Rating.valueOf(data.get("Rating").s()));
                    feedback1.setEmail(data.get("Email").s());
                    feedback1.setFeedback(data.get("Feedback").s());
                    return feedback1;
                })
                .collect(Collectors.toList());

                logger.log(collect.toString());



        final Map<String, Object> data = new HashMap<>();
        data.put("isBase64Encoded", false);
        data.put("statusCode", 200);
        final Map<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Headers", "Content-Type,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Methods", "DELETE,GET,HEAD,OPTIONS,PATCH,POST,PUT");
        headers.put("Content-Type", "application/json");
        data.put("headers", headers);
        data.put("body", new Gson().toJson(collect));
        logger.log(items.toString());

        logger.log(data.get("body").toString());
        return data;
    }


}
