package com.revature.get_comments;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.xml.bind.ValidationException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

public class GetCommentsHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private static final Gson mapper = new GsonBuilder().setPrettyPrinting().create();
    private final ThreadsRepo nodeRepo = new ThreadsRepo(new ThreadsService());

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent requestEvent, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("RECEIVED EVENT: " + requestEvent);
        APIGatewayProxyResponseEvent responseEvent = new APIGatewayProxyResponseEvent();

        try {
            List<Comment> comments = nodeRepo.getAllComments(requestEvent.getPathParameters().get("threadId"));

            if (comments.isEmpty())
                throw new RuntimeException("No comments found with that parentID");

            responseEvent.setBody(mapper.toJson(comments));

            return responseEvent;
        } catch (NullPointerException npe) {
            responseEvent.setStatusCode(404);
            String payload = "No path parameter provided";
            responseEvent.setBody(mapper.toJson(payload));
            return responseEvent;
        } catch (ValidationException ve) {
            responseEvent.setStatusCode(400);
            String payload = ve.getMessage();
            responseEvent.setBody(mapper.toJson(payload));
            return responseEvent;
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String sStackTrace = sw.toString();
            responseEvent.setStatusCode(500);
            responseEvent.setBody(mapper.toJson(sStackTrace));
            return responseEvent;
        }
    }
}
