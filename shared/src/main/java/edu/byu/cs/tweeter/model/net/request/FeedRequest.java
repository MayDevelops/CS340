package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.parents.Request;

public class FeedRequest extends Request {
  private AuthToken authToken;
  private User user;
  private int limit;
  private Status lastStatus;
  private String userAlias;

  private FeedRequest() {
  }

  public FeedRequest(String userAlias) {
    this.userAlias = userAlias;
  }

  public FeedRequest(AuthToken authToken, User user, int limit, Status lastStatus) {
    this.authToken = authToken;
    this.user = user;
    this.limit = limit;
    this.lastStatus = lastStatus;
  }

  public FeedRequest(User user, int limit, Status lastStatus) {
    this.user = user;
    this.limit = limit;
    this.lastStatus = lastStatus;
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

  public Status getLastStatus() {
    return lastStatus;
  }

  public void setLastStatus(Status lastStatus) {
    this.lastStatus = lastStatus;
  }

  public int getLimit() {
    return limit;
  }

  public void setLimit(int limit) {
    this.limit = limit;
  }

  public String getUserAlias() {
    return userAlias;
  }

  public void setUserAlias(String userAlias) {
    this.userAlias = userAlias;
  }
}
