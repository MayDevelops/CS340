package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.parents.CountRequest;

public class FollowingCountRequest extends CountRequest {

  private FollowingCountRequest() {
    super();
  }

  public FollowingCountRequest(AuthToken authToken, User user) {
    super(authToken, user);
  }

  public FollowingCountRequest(User user, Integer newFollowingCount) {
    super(user, newFollowingCount);
  }
}
