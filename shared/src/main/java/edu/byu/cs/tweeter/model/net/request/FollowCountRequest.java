package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowCountRequest extends Request{
  AuthToken authToken;
  User user;

  private FollowCountRequest() {
  }

  public FollowCountRequest(AuthToken authToken, User user) {
    this.authToken = authToken;
    this.user = user;
    if(user != null) {
      user.setImageBytes(null);
    }
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
