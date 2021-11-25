package edu.byu.cs.tweeter.server.factories.dao;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;

import edu.byu.cs.tweeter.model.net.request.FollowerCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.request.parents.Request;
import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.server.dao.UserDAO;
import edu.byu.cs.tweeter.server.factories.abstracts.UserAbstractFactory;

public class UserDAOFactory extends UserAbstractFactory {
  @Override
  public ItemCollection<QueryOutcome> login(Request request) {
    return new UserDAO().login((LoginRequest) request);
  }

  @Override
  public Item register(Request request) {
    return new UserDAO().register((RegisterRequest) request);
  }

  @Override
  public Item getUser(Request request) {
    return new UserDAO().getUser((UserRequest) request);
  }

  @Override
  public Integer getFollowersCount(FollowerCountRequest request) {
    return new UserDAO().getFollowersCount(request);
  }

  @Override
  public Integer getFollowingCount(FollowingCountRequest request) {
    return new UserDAO().getFollowingCount(request);

  }

  @Override
  public Integer updateFollowerCount(FollowerCountRequest request) {
    return new UserDAO().updateFollowerCount(request);
  }

  @Override
  public Integer updateFollowingCount(FollowingCountRequest request) {
    return new UserDAO().updateFollowingCount(request);

  }
}
