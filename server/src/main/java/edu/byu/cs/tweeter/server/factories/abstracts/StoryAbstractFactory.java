package edu.byu.cs.tweeter.server.factories.abstracts;

import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;

import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.server.util.Pair;

public abstract class StoryAbstractFactory {
  public abstract Pair<ItemCollection<QueryOutcome>, Boolean> getStories(StoryRequest request);

  public abstract PutItemOutcome postStatus(PostStatusRequest request);

}