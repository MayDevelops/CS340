package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.net.request.FollowCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowUserRequest;
import edu.byu.cs.tweeter.model.net.request.FollowerPageRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingPageRequest;
import edu.byu.cs.tweeter.model.net.response.FollowCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowUserResponse;
import edu.byu.cs.tweeter.model.net.response.FollowerPageResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingPageResponse;
import edu.byu.cs.tweeter.server.factories.FollowsAbstractFactory;
import edu.byu.cs.tweeter.server.factories.FollowsDAOFactory;

public class FollowsService {

  public FollowerPageResponse getFollowersPage(FollowerPageRequest request) {
    FollowsAbstractFactory followDAO = new FollowsDAOFactory();
    return followDAO.query(request);
  }

  public FollowingPageResponse getFollowingPage(FollowingPageRequest request) {
    FollowsAbstractFactory followDAO = new FollowsDAOFactory();
    return followDAO.query(request);
  }

  public FollowUserResponse getFollowUser(FollowUserRequest request) {
    return new FollowUserResponse(request.getAuthToken(), request.getUser());
  }

  public FollowCountResponse getFollowCount(FollowCountRequest request) {
    return new FollowCountResponse(request);
  }
}
