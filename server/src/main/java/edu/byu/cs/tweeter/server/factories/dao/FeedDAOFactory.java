package edu.byu.cs.tweeter.server.factories.dao;

import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;

import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.server.dao.FeedDAO;
import edu.byu.cs.tweeter.server.factories.abstracts.FeedAbstractFactory;

public class FeedDAOFactory extends FeedAbstractFactory {
  @Override
  public ItemCollection<QueryOutcome> getFeed(FeedRequest request) {
    return new FeedDAO().getFeed(request);
  }
}
