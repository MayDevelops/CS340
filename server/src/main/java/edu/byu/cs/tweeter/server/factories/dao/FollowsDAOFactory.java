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

public class FollowsDAOFactory extends FollowsAbstractFactory {

  @Override
  public ItemCollection<QueryOutcome> getFollowing(FollowingPageRequest request) {
    return new FollowsDAO().getFollowing(request);
  }

  @Override
  public ItemCollection<QueryOutcome> getFollowers(FollowerPageRequest request) {
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
