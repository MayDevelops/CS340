package edu.byu.cs.tweeter.model.net.response;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowerCountRequest;

public class FollowerCountResponse extends Response {

  private AuthToken authToken;
  private User user;
  private Integer followerCount;

  public FollowerCountResponse(String message) {
    super(false, message);
  }

  public FollowerCountResponse(FollowerCountRequest request) {
    super(true, null);
    this.authToken = request.getAuthToken();
    this.user = request.getUser();
  }

  public FollowerCountResponse(Integer followerCount) {
    super(true, null);
    this.followerCount = followerCount;
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
