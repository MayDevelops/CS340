package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowUserRequest {
  AuthToken authToken;
  User user;

  private FollowUserRequest() {
  }

  public FollowUserRequest(AuthToken authToken, User user) {
    this.authToken = authToken;
    this.user = user;
    //todo what is going on with these image bytes?
    this.user.setImageBytes(null);
  }

  public AuthToken getAuthToken() {
    return authToken;
  }

  public void setAuthToken(AuthToken authToken) {
    this.authToken = authToken;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }


}
