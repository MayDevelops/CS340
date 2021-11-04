package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.net.request.FollowPageRequest;
import edu.byu.cs.tweeter.model.net.request.FollowUserRequest;
import edu.byu.cs.tweeter.model.net.response.FollowPageResponse;
import edu.byu.cs.tweeter.model.net.response.FollowUserResponse;
import edu.byu.cs.tweeter.server.dao.FollowDAO;

public class FollowService {
  FollowDAO followPageDAO() {
    return new FollowDAO();
  }

  public FollowPageResponse getFollowPage(FollowPageRequest request) {
    return followPageDAO().getFollowPage(request);
  }

  public FollowUserResponse getFollowUser(FollowUserRequest request) {
    return new FollowUserResponse(request.getAuthToken(), request.getUser());
  }
}
