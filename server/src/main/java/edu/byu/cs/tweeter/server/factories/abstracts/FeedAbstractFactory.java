package edu.byu.cs.tweeter.server.factories.abstracts;

import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;

import edu.byu.cs.tweeter.model.net.request.FeedRequest;

public abstract class FeedAbstractFactory {
  public abstract ItemCollection<QueryOutcome> getFeed(FeedRequest request);

}
