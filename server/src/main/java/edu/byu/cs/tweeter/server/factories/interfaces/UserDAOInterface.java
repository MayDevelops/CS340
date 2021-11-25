package edu.byu.cs.tweeter.server.factories.interfaces;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;

import edu.byu.cs.tweeter.model.net.request.FollowerCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.request.UserRequest;

public interface UserDAOInterface {

  Item register(RegisterRequest request);

  ItemCollection<QueryOutcome> login(LoginRequest request);

  Item getUser(UserRequest request);

  Integer getFollowersCount(FollowerCountRequest request);

  Integer getFollowingCount(FollowingCountRequest request);

  Integer updateFollowerCount(FollowerCountRequest request);

  Integer updateFollowingCount(FollowingCountRequest request);

}
