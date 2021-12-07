package edu.byu.cs.tweeter.server.factories.abstracts;

import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;

import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;

public abstract class FeedAbstractFactory {
  public abstract ItemCollection<QueryOutcome> getFeed(FeedRequest request);

  public abstract Boolean addToFeed(PostStatusRequest request);

}
