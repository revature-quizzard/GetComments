package com.revature.get_comments;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.revature.service.ThreadsService;

import javax.xml.bind.ValidationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class ThreadsRepo {

    private final DynamoDBMapper dbReader;
    private final ThreadsService threadsService;

    public ThreadsRepo(ThreadsService threadsService){
        dbReader = new DynamoDBMapper(AmazonDynamoDBClientBuilder.defaultClient());
        this.threadsService = threadsService;
    }

    /**
     * Grabs all comments belonging to a thread, with the thread's ID specified as an argument.
     *
     * @param parentID The id of the thread whose comments we want to obtain
     *
     * @throws RuntimeException A catch-all for runtime exceptions, most likely a null-pointer exception
     * @throws ValidationException Thrown if the argument passed is invalid
     *
     * Any DynamoDB-related exceptions are unhandled, because nobody really knows how DynamoDB queries work.
     *
     * @author Hiroshi Nobuoka
     */
    public List<Comment> getAllComments(String parentID) throws RuntimeException, ValidationException {
        if(!getParent(parentID).isPresent() || !threadsService.isValidParent((getParent(parentID).get())))
            throw new ValidationException("Either parentID doesn't exist, or parentID belongs to a node that is not a thread ");

        Map<String, AttributeValue> queryInputs = new HashMap<>();
        queryInputs.put(":parentID", new AttributeValue().withS(parentID));

        DynamoDBQueryExpression query = new DynamoDBQueryExpression()
                .withIndexName("parent-date_created-index")
                .withKeyConditionExpression("parent = :parentID")
                .withExpressionAttributeValues(queryInputs)
                .withConsistentRead(false);

        return dbReader.query(Comment.class, query);
    }

    /**
     * Fetches any document within our DynamoDB table whose id matches the argument passed. Used to ensure a thread whose
     * id equals parentID actually exists in our persistence.
     *
     * @param parentID The id of the thread we want to verify exists
     *
     * @author Hiroshi Nobuoka
     */
    public Optional<? extends Comment> getParent(String parentID) {
        Map<String, AttributeValue> queryInputs = new HashMap<>();
        queryInputs.put(":id", new AttributeValue().withS(parentID));

        DynamoDBQueryExpression query = new DynamoDBQueryExpression()
                .withKeyConditionExpression("id = :id")
                .withExpressionAttributeValues(queryInputs);

        return dbReader.query(Comment.class, query).stream().findFirst();
    }


}
