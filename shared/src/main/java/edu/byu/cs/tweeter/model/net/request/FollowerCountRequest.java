package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowerCountRequest extends Request {
  AuthToken authToken;
  User user;
  Integer followerCount;

  private FollowerCountRequest() {
  }

  public FollowerCountRequest(AuthToken authToken, User user) {
    this.authToken = authToken;
    this.user = user;
  }

  public FollowerCountRequest(User user, Integer newFollowerCount) {
    this.user = user;
    this.followerCount = newFollowerCount;
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

  public Integer getFollowerCount() {
    return followerCount;
  }

  public void setFollowerCount(Integer followerCount) {
    this.followerCount = followerCount;
  }
}
