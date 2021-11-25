package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class UnfollowUserRequest extends Request {
  AuthToken authToken;
  User currentUser;
  User selectedUser;

  private UnfollowUserRequest() {
  }

  public UnfollowUserRequest(AuthToken authToken, User follower) {
    this.authToken = authToken;
    this.currentUser = follower;
  }

  public UnfollowUserRequest(AuthToken authToken, User selectedUser, User currentUser) {
    this.authToken = authToken;
    this.currentUser = currentUser;
    this.selectedUser = selectedUser;
  }

  public AuthToken getAuthToken() {
    return authToken;
  }

  public void setAuthToken(AuthToken authToken) {
    this.authToken = authToken;
  }

  public User getCurrentUser() {
    return currentUser;
  }

  public void setCurrentUser(User currentUser) {
    this.currentUser = currentUser;
  }

  public User getSelectedUser() {
    return selectedUser;
  }

  public void setSelectedUser(User selectedUser) {
    this.selectedUser = selectedUser;
  }
}
