package edu.byu.cs.tweeter.server.factories.interfaces;


import edu.byu.cs.tweeter.model.net.request.FollowerPageRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingPageRequest;
import edu.byu.cs.tweeter.model.net.response.FollowerPageResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingPageResponse;

public interface FollowsDAOInterface {

  FollowingPageResponse getFollowing(FollowingPageRequest request);

  FollowerPageResponse getFollowers(FollowerPageRequest request);
}
