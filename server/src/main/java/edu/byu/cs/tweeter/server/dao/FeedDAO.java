package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;

import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.server.factories.interfaces.FeedDAOInterface;

public class FeedDAO implements FeedDAOInterface {

  public ItemCollection<QueryOutcome> getFeed(FeedRequest request) {
    AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
            .withRegion("us-west-2").build();
    DynamoDB dynamoDB = new DynamoDB(client);

    Table feedTable = dynamoDB.getTable("feed");

    QuerySpec feedSpec = new QuerySpec()
            .withKeyConditionExpression("user_handle = :v_id")
            .withValueMap(new ValueMap()
                    .withString(":v_id", request.getUser().getAlias()));

    return feedTable.query(feedSpec);
  }

}
