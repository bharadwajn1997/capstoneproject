package com.cognizant.capstoneprojectone;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.cognizant.capstoneprojectone.models.Feedback;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CreateFeedback implements RequestHandler<APIGatewayProxyRequestEvent , Map<String, Object>> {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final DynamoDbClient dynamoDbClient = DynamoDbClient.create();
    @Override
    public Map<String, Object> handleRequest(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context) {

        String body = apiGatewayProxyRequestEvent.getBody();
        LambdaLogger logger = context.getLogger();
        logger.log(body);
        PutItemRequest putItemRequest = null;
        try {
            putItemRequest = getItem(body);
        } catch (JsonProcessingException e) {

            logger.log(e.toString());
        }
        PutItemResponse putItemResponse = dynamoDbClient.putItem(putItemRequest);

        final Map<String, Object> data = new HashMap<>();
        data.put("isBase64Encoded", false);
        data.put("statusCode", 200);
        final Map<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Headers", "Content-Type,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Methods", "DELETE,GET,HEAD,OPTIONS,PATCH,POST,PUT");
        headers.put("Content-Type", "application/json");
        data.put("headers", headers);
        data.put("body", "{\"status\": \"success\"}");


        return data;

    }

    private PutItemRequest getItem(String body) throws JsonProcessingException {
        Feedback feedback = OBJECT_MAPPER.readValue(body, Feedback.class);
        Map<String , AttributeValue> data = new HashMap<>();
        data.put("id" , AttributeValue.builder().s(UUID.randomUUID().toString()).build());
        data.put("Email" , AttributeValue.builder().s(feedback.getEmail()).build());
        data.put("Name" , AttributeValue.builder().s(feedback.getName()).build());
        data.put("Feedback" , AttributeValue.builder().s(feedback.getFeedback()).build());
        data.put("Rating" , AttributeValue.builder().s(feedback.getRating().name()).build());
        return PutItemRequest.builder()
                .tableName("feedback")
                .item(data)
                .build();
    }

}
