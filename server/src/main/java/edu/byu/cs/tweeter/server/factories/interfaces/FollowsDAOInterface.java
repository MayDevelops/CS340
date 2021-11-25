package edu.byu.cs.tweeter.server.factories.interfaces;


import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.model.DeleteItemResult;

import edu.byu.cs.tweeter.model.net.request.FollowUserRequest;
import edu.byu.cs.tweeter.model.net.request.FollowerPageRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingPageRequest;
import edu.byu.cs.tweeter.model.net.request.UnfollowUserRequest;

public interface FollowsDAOInterface {

  ItemCollection<QueryOutcome> getFollowing(FollowingPageRequest request);

  ItemCollection<QueryOutcome> getFollowers(FollowerPageRequest request);

  PutItemOutcome follow(FollowUserRequest request);

  DeleteItemResult unfollow(UnfollowUserRequest request);

}
