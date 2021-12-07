package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.BatchWriteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.FollowerPageRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.server.dao.config.DAOConfig;
import edu.byu.cs.tweeter.server.factories.abstracts.FollowsAbstractFactory;
import edu.byu.cs.tweeter.server.factories.interfaces.FeedDAOInterface;
import edu.byu.cs.tweeter.server.service.config.ServiceHelper;

public class FeedDAO extends DAOConfig implements FeedDAOInterface {

  FollowsAbstractFactory followDAO = ServiceHelper.followDAO;

  public FeedDAO() {
    super();
  }

  public ItemCollection<QueryOutcome> getFeed(FeedRequest request) {
    QuerySpec feedSpec = new QuerySpec()
            .withKeyConditionExpression("user_handle = :v_id")
            .withValueMap(new ValueMap()
                    .withString(":v_id", request.getUser().getAlias()));

    return feedTable.query(feedSpec);
  }

  public Boolean addToFeed(PostStatusRequest request) {
    List<String> allFollowerAlias = new ArrayList<>();

    try {

      ItemCollection<QueryOutcome> allFollowers = followDAO.getFollowers(new FollowerPageRequest(request.getStatus().getUser().getAlias()));

      for (Item follower : allFollowers) {

        String followerAlias = follower.getString("follower_handle");
        allFollowerAlias.add(followerAlias);
      }

      if (allFollowerAlias.size() > 0) {
        addUserBatch(allFollowerAlias, request);
      }

      return true;
    } catch (Exception e) {
      return null;
    }
  }


  public void addUserBatch(List<String> users, PostStatusRequest request) {

    TableWriteItems items = new TableWriteItems("feed");

    for (String userAlias : users) {
      Item item = new Item().withPrimaryKey("user_handle", userAlias)
              .with("post_user", request.getStatus().getUser().getAlias())
              .with("post", request.getStatus().getPost())
              .with("datetime", request.getStatus().getDate())
              .with("urls", request.getStatus().getUrls())
              .with("mentions", request.getStatus().getMentions());
      items.addItemToPut(item);
      if (items.getItemsToPut() != null && items.getItemsToPut().size() == 25) {
        loopBatchWrite(items);
        items = new TableWriteItems("feed");
      }
    }

    if (items.getItemsToPut() != null && items.getItemsToPut().size() > 0) {
      loopBatchWrite(items);
    }
  }

  private void loopBatchWrite(TableWriteItems items) {
    AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
            .withRegion("us-west-2").build();
    DynamoDB dynamoDB = new DynamoDB(client);

    BatchWriteItemOutcome outcome = dynamoDB.batchWriteItem(items);

    while (outcome.getUnprocessedItems().size() > 0) {
      Map<String, List<WriteRequest>> unprocessedItems = outcome.getUnprocessedItems();
      outcome = dynamoDB.batchWriteItemUnprocessed(unprocessedItems);
    }
  }

}
