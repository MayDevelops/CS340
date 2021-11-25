package edu.byu.cs.tweeter.model.net.request.parents;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class FollowsPageRequest {
  public AuthToken authToken;
  public String followerAlias;
  public int limit;
  public String lastFollowingAlias;


  public FollowsPageRequest(AuthToken authToken, String followerAlias, int limit, String lastFollowingAlias) {
  }

  public FollowsPageRequest(String followerAlias, int limit, String lastFollowingAlias) {
    this.followerAlias = followerAlias;
    this.limit = limit;
    this.lastFollowingAlias = lastFollowingAlias;

  }

  public FollowsPageRequest(String followerAlias) {
    this.followerAlias = followerAlias;
  }

  public FollowsPageRequest() {

  }

  public AuthToken getAuthToken() {
    return authToken;
  }

  public void setAuthToken(AuthToken authToken) {
    this.authToken = authToken;
  }

  public String getFollowerAlias() {
    return followerAlias;
  }

  public void setFollowerAlias(String followerAlias) {
    this.followerAlias = followerAlias;
  }

  public int getLimit() {
    return limit;
  }

  public void setLimit(int limit) {
    this.limit = limit;
  }

  public String getLastFollowsAlias() {
    return lastFollowingAlias;
  }

  public void setLastFollowingAlias(String lastFollowingAlias) {
    this.lastFollowingAlias = lastFollowingAlias;
  }
}
