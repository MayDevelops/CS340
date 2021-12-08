package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.server.dao.config.DAOConfig;
import edu.byu.cs.tweeter.server.factories.interfaces.StoryDAOInterface;
import edu.byu.cs.tweeter.server.util.Pair;

public class StoryDAO extends DAOConfig implements StoryDAOInterface {

  public StoryDAO() {
    super();
  }

  @Override
  public Pair<ItemCollection<QueryOutcome>, Boolean> getStories(StoryRequest request) {
    ItemCollection<QueryOutcome> results;
    QuerySpec storySpec;
    Boolean hasMorePages;

    if (request.getLastStatus() == null) {
      storySpec = new QuerySpec()
              .withKeyConditionExpression("user_handle = :v_id")
              .withValueMap(new ValueMap()
                      .withString(":v_id", request.getUser().getAlias()))
              .withMaxResultSize(request.getLimit());

      results = storyTable.query(storySpec);
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
      PrimaryKey lastKey = new PrimaryKey("user_handle", request.getLastStatus().getUser().getAlias(),
              "datetime", request.getLastStatus().getDate());

      storySpec = new QuerySpec()
              .withKeyConditionExpression("user_handle = :v_id")
              .withValueMap(new ValueMap()
                      .withString(":v_id", request.getUser().getAlias()))
              .withMaxResultSize(request.getLimit())
              .withExclusiveStartKey(lastKey);

      results = storyTable.query(storySpec);
      List<Item> a = new ArrayList<>();
      results.pages().forEach(p -> {
        p.iterator().forEachRemaining(o -> a.add(o));
      });
      hasMorePages = (results.getLastLowLevelResult().getQueryResult().getLastEvaluatedKey() != null);
      return new Pair<>(results, hasMorePages);
    }
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
