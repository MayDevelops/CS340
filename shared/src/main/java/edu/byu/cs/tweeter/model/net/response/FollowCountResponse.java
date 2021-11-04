package edu.byu.cs.tweeter.model.net.response;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowCountRequest;

public class FollowCountResponse extends Response {

  private AuthToken authToken;
  private User user;

  public FollowCountResponse(String message) {
    super(false, message);
  }

  public FollowCountResponse(FollowCountRequest request) {
    super(true, null);
    this.authToken = request.getAuthToken();
    this.user = request.getUser();
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

  public int getFollowerCount() {
    return 20;
  }

  public int getFollowingCount() {
    return 20;
  }
}
