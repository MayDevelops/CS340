package edu.byu.cs.tweeter.server.factories.dao;

import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;

import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.server.dao.FeedDAO;
import edu.byu.cs.tweeter.server.factories.abstracts.FeedAbstractFactory;
import edu.byu.cs.tweeter.server.util.Pair;

public class FeedDAOFactory extends FeedAbstractFactory {
  @Override
  public Pair<ItemCollection<QueryOutcome>, Boolean> getFeed(FeedRequest request) {
    return new FeedDAO().getFeed(request);
  }

  @Override
  public Boolean addToFeed(PostStatusRequest request) {
    return new FeedDAO().addToFeed(request);
  }

}
