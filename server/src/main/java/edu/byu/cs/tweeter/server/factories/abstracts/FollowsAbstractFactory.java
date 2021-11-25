package edu.byu.cs.tweeter.server.factories.abstracts;

import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.model.DeleteItemResult;

import edu.byu.cs.tweeter.model.net.request.FollowUserRequest;
import edu.byu.cs.tweeter.model.net.request.FollowerPageRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingPageRequest;
import edu.byu.cs.tweeter.model.net.request.UnfollowUserRequest;

public abstract class FollowsAbstractFactory {
  public abstract ItemCollection<QueryOutcome> query(FollowerPageRequest request);

  public abstract ItemCollection<QueryOutcome> query(FollowingPageRequest request);

  public abstract PutItemOutcome follow(FollowUserRequest request);

  public abstract DeleteItemResult unfollow(UnfollowUserRequest request);

}
