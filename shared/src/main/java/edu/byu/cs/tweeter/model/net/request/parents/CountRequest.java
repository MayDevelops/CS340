package edu.byu.cs.tweeter.model.net.request.parents;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class CountRequest extends Request {
  AuthToken authToken;
  User user;
  Integer followerCount;

  public CountRequest(AuthToken authToken, User user) {
    this.authToken = authToken;
    this.user = user;
  }

  public CountRequest(User user, Integer newFollowerCount) {
    this.user = user;
    this.followerCount = newFollowerCount;
  }

  public CountRequest() {
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

  public Integer getCount() {
    return followerCount;
  }


}
