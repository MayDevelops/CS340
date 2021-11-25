package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryRequest extends Request {
  private AuthToken authToken;
  private User user;
  private int limit;
  private Status lastStatus;

  private StoryRequest() {
  }

  public StoryRequest(AuthToken authToken, User user, int limit, Status lastStatus) {
    this.authToken = authToken;
    this.user = user;
    this.limit = limit;
    this.lastStatus = lastStatus;
  }

  public StoryRequest(User user, int limit, Status lastStatus) {
    this.user = user;
    this.limit = limit;
    this.lastStatus = lastStatus;
  }

  public StoryRequest(User user) {
    this.user = user;
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

}

