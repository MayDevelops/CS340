package edu.byu.cs.tweeter.model.net.response;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowUserResponse extends Response {

  private User user;
  private AuthToken authToken;

  public FollowUserResponse(String message) {
    super(false, message);
  }

  public FollowUserResponse(AuthToken authToken, User user) {
    super(true, null);
    this.user = user;
    this.authToken = authToken;
  }

  public FollowUserResponse(boolean success) {
    super(success, null);
  }

  public User getUser() {
    return user;
  }

  public AuthToken getAuthToken() {
    return authToken;
  }
}

