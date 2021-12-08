package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Index;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.DeleteItemResult;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.net.request.FollowUserRequest;
import edu.byu.cs.tweeter.model.net.request.FollowerPageRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingPageRequest;
import edu.byu.cs.tweeter.model.net.request.UnfollowUserRequest;
import edu.byu.cs.tweeter.server.dao.config.DAOConfig;
import edu.byu.cs.tweeter.server.factories.interfaces.FollowsDAOInterface;
import edu.byu.cs.tweeter.server.util.Pair;

public class FollowsDAO extends DAOConfig implements FollowsDAOInterface {

  public FollowsDAO() {
    super();
  }

  @Override
  public Pair<ItemCollection<QueryOutcome>, Boolean> getFollowing(FollowingPageRequest request) {
    ItemCollection<QueryOutcome> results;
    QuerySpec followsSpec;
    Boolean hasMorePages;

    if (request.getLastFollowsAlias() == null) {
      followsSpec = new QuerySpec()
              .withKeyConditionExpression("follower_handle = :v_id")
              .withValueMap(new ValueMap()
                      .withString(":v_id", request.getFollowerAlias()))
              .withMaxResultSize(request.getLimit());

      results = followsTable.query(followsSpec);
      List<Item> a = new ArrayList<>();
      results.pages().forEach(p -> {
        p.iterator().forEachRemaining(o -> a.add(o));
      });

      if (results.getLastLowLevelResult().getQueryResult().getLastEvaluatedKey() == null) {
        hasMorePages = false;
      } else {
        hasMorePages = true;
      }
      return new Pair<>(results, hasMorePages);

    } else {
      //todo could be broken?

      PrimaryKey lastKey = new PrimaryKey("follower_handle", request.getFollowerAlias(), "followee_handle", request.getLastFollowsAlias());

      followsSpec = new QuerySpec()
              .withKeyConditionExpression("follower_handle = :v_id")
              .withValueMap(new ValueMap()
                      .withString(":v_id", request.getFollowerAlias()))
              .withMaxResultSize(request.getLimit())
              .withExclusiveStartKey(lastKey);

      results = followsTable.query(followsSpec);
      List<Item> a = new ArrayList<>();
      results.pages().forEach(p -> {
        p.iterator().forEachRemaining(o -> a.add(o));
      });
      hasMorePages = (results.getLastLowLevelResult().getQueryResult().getLastEvaluatedKey() != null);
      return new Pair<>(results, hasMorePages);
    }
  }

  @Override
  public Pair<ItemCollection<QueryOutcome>, Boolean> getFollowers(FollowerPageRequest request) {
    ItemCollection<QueryOutcome> results;
    QuerySpec followsSpec;
    Boolean hasMorePages;

    if (request.getLastFollowsAlias() == null) {
      followsSpec = new QuerySpec()
              .withKeyConditionExpression("followee_handle = :v_id")
              .withValueMap(new ValueMap()
                      .withString(":v_id", request.getFollowerAlias()))
              .withMaxResultSize(request.getLimit());

      Index index = followsTable.getIndex("follows_index");

      results = index.query(followsSpec);

      List<Item> a = new ArrayList<>();
      results.pages().forEach(p -> {
        p.iterator().forEachRemaining(o -> a.add(o));
      });

      if(results.getLastLowLevelResult().getQueryResult().getLastEvaluatedKey() == null) {
        hasMorePages = false;
      } else {
        hasMorePages = true;
      }
      return new Pair<>(results, hasMorePages);

    } else {
      //todo could be broken?

      PrimaryKey lastKey = new PrimaryKey("follower_handle", request.getLastFollowsAlias(),
              "followee_handle", request.getFollowerAlias());

      followsSpec = new QuerySpec()
              .withKeyConditionExpression("followee_handle = :v_id")
              .withValueMap(new ValueMap()
                      .withString(":v_id", request.getFollowerAlias()))
              .withMaxResultSize(request.getLimit())
              .withExclusiveStartKey(lastKey);
      Index index = followsTable.getIndex("follows_index");

      results = index.query(followsSpec);
      List<Item> a = new ArrayList<>();
      results.pages().forEach(p -> {
        p.iterator().forEachRemaining(o -> a.add(o));
      });
      hasMorePages = (results.getLastLowLevelResult().getQueryResult().getLastEvaluatedKey() != null);
      return new Pair<>(results, hasMorePages);
    }
  }

  @Override
  public ItemCollection<QueryOutcome> getAllFollowing(FollowingPageRequest request) {
    QuerySpec followsSpec = new QuerySpec()
            .withKeyConditionExpression("follower_handle = :v_id")
            .withValueMap(new ValueMap()
                    .withString(":v_id", request.getFollowerAlias()));

    return followsTable.query(followsSpec);
  }

  @Override
  public ItemCollection<QueryOutcome> getAllFollowers(FollowerPageRequest request) {
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

    usersTable.updateItem(updateItemSpec);

    return outcome.getDeleteItemResult();
  }
}
