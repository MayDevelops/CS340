package edu.byu.cs.tweeter.server.factories.abstracts;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;

import edu.byu.cs.tweeter.model.net.request.FollowerCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.Request;

public abstract class UserAbstractFactory {

  public abstract ItemCollection<QueryOutcome> login(Request request);

  public abstract Item register(Request request);

  public abstract Item getUser(Request request);

  public abstract Integer getFollowersCount(FollowerCountRequest request);

  public abstract Integer getFollowingCount(FollowingCountRequest request);

  public abstract Integer updateFollowerCount(FollowerCountRequest request);

  public abstract Integer updateFollowingCount(FollowingCountRequest request);

}
