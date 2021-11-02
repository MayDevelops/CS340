package edu.byu.cs.tweeter.model.net.response;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class LogoutResponse extends Response {

  private AuthToken authToken;

  public LogoutResponse(AuthToken authToken) {
    super(true, null);
    this.authToken = authToken;
  }

  public AuthToken getAuthToken() {
    return authToken;
  }

  public void setAuthToken(AuthToken authToken) {
    this.authToken = authToken;
  }
}
