package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Index;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowerPageRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingPageRequest;
import edu.byu.cs.tweeter.model.net.response.FollowerPageResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingPageResponse;
import edu.byu.cs.tweeter.server.factories.interfaces.FollowsDAOInterface;

public class FollowsDAOProvider implements FollowsDAOInterface {

  String followerHandle;
  String followeeHandle;
  String followerName;
  String followeeName;


  public FollowsDAOProvider() {
  }

  public FollowsDAOProvider(String followerHandle, String followeeHandle, String followerName, String followeeName) {
    this.followerHandle = followerHandle;
    this.followeeHandle = followeeHandle;
    this.followerName = followerName;
    this.followeeName = followeeName;

  }


  @Override
  public FollowingPageResponse getFollowing(FollowingPageRequest request) {
      assert request.getLimit() > 0;
      assert request.getFollowerAlias() != null;

      List<User> allFollowees = new ArrayList<>();
      List<User> responseFollowees = new ArrayList<>(request.getLimit());

      AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
              .withRegion("us-west-2").build();
      DynamoDB dynamoDB = new DynamoDB(client);

      Table followsTable = dynamoDB.getTable("follows");
      Table usersTable = dynamoDB.getTable("users");

      QuerySpec followsSpec = new QuerySpec()
              .withKeyConditionExpression("follower_handle = :v_id")
              .withValueMap(new ValueMap()
                      .withString(":v_id", request.getFollowerAlias()));

      ItemCollection<QueryOutcome> items = followsTable.query(followsSpec);
      Iterator<Item> iterator = items.iterator();
      while (iterator.hasNext()) {
        Item item = iterator.next();

        GetItemSpec usersSpec = new GetItemSpec()
                .withPrimaryKey("user_handle", item.getString("followee_handle"));
        Item userItem = usersTable.getItem(usersSpec);

        String firstName = userItem.getString("first_name");
        String lastName = userItem.getString("last_name");
        String alias = userItem.getString("user_handle");
        String imageURL = userItem.getString("image_url");

        User tempUser = new User(firstName, lastName, alias, imageURL);
        allFollowees.add(tempUser);
      }

      boolean hasMorePages = false;

      if (request.getLimit() > 0) {
        if (allFollowees != null) {
          int followeesIndex = getFolloweesStartingIndex(request.getLastFollowingAlias(), allFollowees);

          for (int limitCounter = 0; followeesIndex < allFollowees.size() && limitCounter < request.getLimit(); followeesIndex++, limitCounter++) {
            responseFollowees.add(allFollowees.get(followeesIndex));
          }

          hasMorePages = followeesIndex < allFollowees.size();
        }
      }

      return new FollowingPageResponse(responseFollowees, hasMorePages);

    }

  @Override
  public FollowerPageResponse getFollowers(FollowerPageRequest request) {
    assert request.getLimit() > 0;
    assert request.getFollowerAlias() != null;

    List<User> allFollowees = new ArrayList<>();
    List<User> responseFollowees = new ArrayList<>(request.getLimit());

    AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
            .withRegion("us-west-2").build();
    DynamoDB dynamoDB = new DynamoDB(client);

    Table followsTable = dynamoDB.getTable("follows");
    Index index = followsTable.getIndex("follows_index");

    Table usersTable = dynamoDB.getTable("users");

    QuerySpec followsSpec = new QuerySpec()
            .withKeyConditionExpression("followee_handle = :v_id")
            .withValueMap(new ValueMap()
                    .withString(":v_id", request.getFollowerAlias()));

    ItemCollection<QueryOutcome> items = index.query(followsSpec);
    Iterator<Item> iterator = items.iterator();
    while (iterator.hasNext()) {
      Item item = iterator.next();

      GetItemSpec usersSpec = new GetItemSpec()
              .withPrimaryKey("user_handle", item.getString("follower_handle"));
      Item userItem = usersTable.getItem(usersSpec);

      String firstName = userItem.getString("first_name");
      String lastName = userItem.getString("last_name");
      String alias = userItem.getString("user_handle");
      String imageURL = userItem.getString("image_url");

      User tempUser = new User(firstName, lastName, alias, imageURL);
      allFollowees.add(tempUser);
    }

    boolean hasMorePages = false;

    if (request.getLimit() > 0) {
      if (allFollowees != null) {
        int followeesIndex = getFolloweesStartingIndex(request.getLastFollowingAlias(), allFollowees);

        for (int limitCounter = 0; followeesIndex < allFollowees.size() && limitCounter < request.getLimit(); followeesIndex++, limitCounter++) {
          responseFollowees.add(allFollowees.get(followeesIndex));
        }

        hasMorePages = followeesIndex < allFollowees.size();
      }
    }

    return new FollowerPageResponse(responseFollowees, hasMorePages);

  }



  private int getFolloweesStartingIndex(String lastFolloweeAlias, List<User> allFollowees) {

    int followeesIndex = 0;

    if (lastFolloweeAlias != null) {
      for (int i = 0; i < allFollowees.size(); i++) {
        if (lastFolloweeAlias.equals(allFollowees.get(i).getAlias())) {
          followeesIndex = i + 1;
          break;
        }
      }
    }

    return followeesIndex;
  }


  public String getFollowerHandle() {
    return followerHandle;
  }

  public void setFollowerHandle(String followerHandle) {
    this.followerHandle = followerHandle;
  }

  public String getFolloweeHandle() {
    return followeeHandle;
  }

  public void setFolloweeHandle(String followeeHandle) {
    this.followeeHandle = followeeHandle;
  }

  public String getFollowerName() {
    return followerName;
  }

  public void setFollowerName(String followerName) {
    this.followerName = followerName;
  }

  public String getFolloweeName() {
    return followeeName;
  }

  public void setFolloweeName(String followeeName) {
    this.followeeName = followeeName;
  }
}
