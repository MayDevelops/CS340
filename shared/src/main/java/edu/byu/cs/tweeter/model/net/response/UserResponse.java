package edu.byu.cs.tweeter.model.net.response;

import edu.byu.cs.tweeter.model.domain.User;

public class UserResponse extends Response {
  User user;


  UserResponse(boolean success) {
    super(success);
  }

  UserResponse(boolean success, String message) {
    super(success, message);
  }

  public UserResponse(User user) {
    super(true, null);
    this.user = user;
  }


  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
