package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingCountRequest extends Request {
  AuthToken authToken;
  User user;
  Integer followingCount;

  private FollowingCountRequest() {
  }

  public FollowingCountRequest(AuthToken authToken, User user) {
    this.authToken = authToken;
    this.user = user;
  }

  public FollowingCountRequest(User user, Integer newFollowingCount) {
    this.user = user;
    this.followingCount = newFollowingCount;
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

  public Integer getFollowingCount() {
    return followingCount;
  }

  public void setFollowingCount(Integer followingCount) {
    this.followingCount = followingCount;
  }
}
