package com.cognizant.capstoneprojectone;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;

import com.cognizant.capstoneprojectone.models.Feedback;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class DeleteFeedback implements RequestHandler<APIGatewayProxyRequestEvent , Map<String, Object>>
{
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final DynamoDbClient dynamoDbClient = DynamoDbClient.create();

    @Override
    public Map<String, Object> handleRequest(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context) {
        String body = apiGatewayProxyRequestEvent.getBody();
        LambdaLogger logger = context.getLogger();
        logger.log(body);

        String httpMethod = apiGatewayProxyRequestEvent.getHttpMethod();
        logger.log(httpMethod);
        if("OPTIONS".equalsIgnoreCase(httpMethod))
        {
            final Map<String, Object> data = new HashMap<>();
            data.put("isBase64Encoded", false);
            data.put("statusCode", 200);
            final Map<String, String> headers = new HashMap<>();
            headers.put("Access-Control-Allow-Headers", "Content-Type,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
            headers.put("Access-Control-Allow-Origin", "*");
            headers.put("Access-Control-Allow-Methods", "DELETE,GET,HEAD,OPTIONS,PATCH,POST,PUT");
            headers.put("Content-Type", "application/json");
            data.put("headers", headers);

            return data;
        }
        JsonNode jsonNode = null;
        try {
            jsonNode = OBJECT_MAPPER.readTree(body);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        JsonNode ids = jsonNode.get("ids");

        if(ids != null && ids.isArray())
        {
                List<String> fids = StreamSupport.stream(ids.spliterator() , false)
                        .map(JsonNode::asText)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                logger.log(fids.toString());
                for(String id : fids) {
                    Map<String , AttributeValue> data = new HashMap<>();
                    data.put("id" , AttributeValue.builder().s(id).build());
                    dynamoDbClient.deleteItem(DeleteItemRequest.builder().tableName("feedback").key(data).build());
                }

        }
        else
        {
            logger.log("else block");
        }


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

}
