package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.parents.CountRequest;

public class FollowerCountRequest extends CountRequest {
  private FollowerCountRequest() {
    super();
  }

  public FollowerCountRequest(AuthToken authToken, User user) {
    super(authToken, user);
  }

  public FollowerCountRequest(User user, Integer newFollowerCount) {
    super(user, newFollowerCount);
  }
}
