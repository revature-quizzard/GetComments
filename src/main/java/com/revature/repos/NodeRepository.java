package com.revature.repos;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.revature.models.Comment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NodeRepository {

    private final DynamoDBMapper dbReader;

    public NodeRepository(){ dbReader = new DynamoDBMapper(AmazonDynamoDBClientBuilder.defaultClient());}

    public List<Comment> getAllComments(String parent){
        Map<String, AttributeValue> queryInputs = new HashMap<>();
        queryInputs.put(":parent", new AttributeValue().withS(parent));

        DynamoDBScanExpression query = new DynamoDBScanExpression()
                .withFilterExpression("parent = :parent")
                .withExpressionAttributeValues(queryInputs);

        return dbReader.scan(Comment.class,query);
    }


}
