package edu.byu.cs.tweeter.server.factories.interfaces;

import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;

import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingPageRequest;

public interface FeedDAOInterface {
  ItemCollection<QueryOutcome> getFeed(FeedRequest request);

}
