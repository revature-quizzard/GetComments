package com.revature.get_comments;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

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

    public List<Comment> getAllComments(String parent) throws RuntimeException, ValidationException {
        if(!getParent(parent).isPresent() || !threadsService.isValidParent((getParent(parent).get())))
            throw new ValidationException("Either parentID doesn't exist, or parentID belongs to a node that is not a thread ");

        Map<String, AttributeValue> queryInputs = new HashMap<>();
        queryInputs.put(":parentID", new AttributeValue().withS(parent));

        DynamoDBQueryExpression query = new DynamoDBQueryExpression()
                .withFilterExpression("parent = :parentID")
                .withExpressionAttributeValues(queryInputs);

        return dbReader.query(Comment.class, query);
    }

    public Optional<? extends Comment> getParent(String parentID) {
        Map<String, AttributeValue> queryInputs = new HashMap<>();
        queryInputs.put(":id", new AttributeValue().withS(parentID));

        DynamoDBQueryExpression query = new DynamoDBQueryExpression()
                .withKeyConditionExpression("id = :id")
                .withExpressionAttributeValues(queryInputs);

        return dbReader.query(Comment.class, query).stream().findFirst();
    }


}
