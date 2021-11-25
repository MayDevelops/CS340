package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;

import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.server.factories.interfaces.StoryDAOInterface;

public class StoryDAO implements StoryDAOInterface {

  AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
          .withRegion("us-west-2").build();
  DynamoDB dynamoDB = new DynamoDB(client);

  Table storyTable = dynamoDB.getTable("story");

  @Override
  public ItemCollection<QueryOutcome> getStories(StoryRequest request) {


    QuerySpec storySpec = new QuerySpec()
            .withKeyConditionExpression("user_handle = :v_id")
            .withValueMap(new ValueMap()
                    .withString(":v_id", request.getUser().getAlias()));

    return storyTable.query(storySpec);
  }

  @Override
  public PutItemOutcome postStatus(PostStatusRequest request) {
    Item item = new Item().withPrimaryKey("user_handle", request.getStatus().getUser().getAlias())
            .with("post", request.getStatus().getPost())
            .with("datetime", request.getStatus().getDate())
            .with("urls", request.getStatus().getUrls())
            .with("mentions", request.getStatus().getMentions());

    return storyTable.putItem(item);
  }

}
