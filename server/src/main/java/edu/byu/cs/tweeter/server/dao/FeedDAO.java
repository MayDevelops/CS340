package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;

import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.server.dao.config.DAOConfig;
import edu.byu.cs.tweeter.server.factories.interfaces.FeedDAOInterface;

public class FeedDAO extends DAOConfig implements FeedDAOInterface {
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
}
