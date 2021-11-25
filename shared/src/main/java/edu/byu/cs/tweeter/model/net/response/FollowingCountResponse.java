package edu.byu.cs.tweeter.model.net.response;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowerCountRequest;

public class FollowingCountResponse extends Response {

  private AuthToken authToken;
  private User user;
  private Integer followingCount;

  public FollowingCountResponse(String message) {
    super(false, message);
  }

  public FollowingCountResponse(FollowerCountRequest request) {
    super(true, null);
    this.authToken = request.getAuthToken();
    this.user = request.getUser();
  }

  public FollowingCountResponse(Integer followingCount) {
    super(true, null);
    this.followingCount = followingCount;
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
