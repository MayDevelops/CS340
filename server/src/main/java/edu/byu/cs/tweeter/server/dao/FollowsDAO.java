package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Index;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.DeleteItemResult;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;

import edu.byu.cs.tweeter.model.net.request.FollowUserRequest;
import edu.byu.cs.tweeter.model.net.request.FollowerPageRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingPageRequest;
import edu.byu.cs.tweeter.model.net.request.UnfollowUserRequest;
import edu.byu.cs.tweeter.server.factories.abstracts.UserAbstractFactory;
import edu.byu.cs.tweeter.server.factories.interfaces.FollowsDAOInterface;
import edu.byu.cs.tweeter.server.service.FactoryConfig;

public class FollowsDAO implements FollowsDAOInterface {
  AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
          .withRegion("us-west-2").build();
  DynamoDB dynamoDB = new DynamoDB(client);

  Table followsTable = dynamoDB.getTable("follows");

  public FollowsDAO() {
  }

  @Override
  public ItemCollection<QueryOutcome> getFollowing(FollowingPageRequest request) {
    QuerySpec followsSpec = new QuerySpec()
            .withKeyConditionExpression("follower_handle = :v_id")
            .withValueMap(new ValueMap()
                    .withString(":v_id", request.getFollowerAlias()));

    return followsTable.query(followsSpec);
  }

  @Override
  public ItemCollection<QueryOutcome> getFollowers(FollowerPageRequest request) {
    QuerySpec followsSpec = new QuerySpec()
            .withKeyConditionExpression("followee_handle = :v_id")
            .withValueMap(new ValueMap()
                    .withString(":v_id", request.getFollowerAlias()));

    Index index = followsTable.getIndex("follows_index");

    return index.query(followsSpec);

  }

  @Override
  public PutItemOutcome follow(FollowUserRequest request) {
    PutItemOutcome putItemOutcome = followsTable
            .putItem(new Item().withPrimaryKey(
                    "follower_handle", request.getCurrentUser().getAlias(),
                    "followee_handle", request.getSelectedUser().getAlias())
                    .with("follower_name", request.getSelectedUser().getFirstName())
                    .with("followee_name", request.getCurrentUser().getFirstName()));

    UpdateItemSpec updateItemSpec = new UpdateItemSpec()
            .withPrimaryKey("user_handle", request.getCurrentUser().getAlias())
            .withUpdateExpression("SET follower_count = follower_count + :p")
            .withValueMap(new ValueMap().withInt(":p", 1))
            .withReturnValues(ReturnValue.UPDATED_NEW);

    AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
            .withRegion("us-west-2").build();
    DynamoDB dynamoDB = new DynamoDB(client);

    Table usersTable = dynamoDB.getTable("users");



    usersTable.updateItem(updateItemSpec);


    return putItemOutcome;
  }

  @Override
  public DeleteItemResult unfollow(UnfollowUserRequest request) {
    DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
            .withPrimaryKey(new PrimaryKey(
                    "follower_handle", request.getCurrentUser().getAlias(),
                    "followee_handle", request.getSelectedUser().getAlias())).withReturnValues(ReturnValue.ALL_OLD);

    DeleteItemOutcome outcome = followsTable.deleteItem(deleteItemSpec);

    UpdateItemSpec updateItemSpec = new UpdateItemSpec()
            .withPrimaryKey("user_handle", request.getCurrentUser().getAlias())
            .withUpdateExpression("SET follower_count = follower_count - :p")
            .withValueMap(new ValueMap().withInt(":p", 1))
            .withReturnValues(ReturnValue.UPDATED_NEW);

    AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
            .withRegion("us-west-2").build();
    DynamoDB dynamoDB = new DynamoDB(client);

    Table usersTable = dynamoDB.getTable("users");



    usersTable.updateItem(updateItemSpec);

    return outcome.getDeleteItemResult();
  }

}
