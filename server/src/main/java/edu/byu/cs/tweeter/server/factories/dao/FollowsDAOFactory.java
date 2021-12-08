package edu.byu.cs.tweeter.server.factories.dao;


import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.model.DeleteItemResult;

import edu.byu.cs.tweeter.model.net.request.FollowUserRequest;
import edu.byu.cs.tweeter.model.net.request.FollowerPageRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingPageRequest;
import edu.byu.cs.tweeter.model.net.request.UnfollowUserRequest;
import edu.byu.cs.tweeter.server.dao.FollowsDAO;
import edu.byu.cs.tweeter.server.factories.abstracts.FollowsAbstractFactory;
import edu.byu.cs.tweeter.server.util.Pair;

public class FollowsDAOFactory extends FollowsAbstractFactory {

  @Override
  public Pair<ItemCollection<QueryOutcome>, Boolean> getFollowing(FollowingPageRequest request) {
    return new FollowsDAO().getFollowing(request);
  }

  @Override
  public ItemCollection<QueryOutcome> getAllFollowing(FollowingPageRequest request) {
    return new FollowsDAO().getAllFollowing(request);
  }

  @Override
  public ItemCollection<QueryOutcome> getAllFollowers(FollowerPageRequest request) {
    return new FollowsDAO().getAllFollowers(request);
  }

  @Override
  public Pair<ItemCollection<QueryOutcome>, Boolean> getFollowers(FollowerPageRequest request) {
    return new FollowsDAO().getFollowers(request);
  }

  @Override
  public PutItemOutcome follow(FollowUserRequest request) {
    return new FollowsDAO().follow(request);
  }

  @Override
  public DeleteItemResult unfollow(UnfollowUserRequest request) {
    return new FollowsDAO().unfollow(request);
  }
}
