package com.revature.get_comments;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.revature.models.Comment;
import com.revature.repos.NodeRepository;

import java.util.List;

public class GetCommentsHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private static final Gson mapper = new GsonBuilder().setPrettyPrinting().create();
    private final NodeRepository nodeRepo = new NodeRepository();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent requestEvent, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("RECEIVED EVENT: " + requestEvent);
        APIGatewayProxyResponseEvent responseEvent = new APIGatewayProxyResponseEvent();
        try {
            List<Comment> comments = nodeRepo.getAllComments(requestEvent.getPathParameters().get("parentid"));

            if (comments.isEmpty())
                throw new RuntimeException("No comments found with that parentID");

            responseEvent.setBody(mapper.toJson(comments));

            return responseEvent;
        } catch (RuntimeException re){
            responseEvent.setStatusCode(400);
            String payload;
            if(re.getClass().equals(NullPointerException.class))
                payload = "No path parameter provided";
            else
                payload = re.getMessage();
            responseEvent.setBody(mapper.toJson(payload));
            return responseEvent;
        }
    }
}
