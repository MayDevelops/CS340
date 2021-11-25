package edu.byu.cs.tweeter.model.net.response;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class TokenResponse extends Response {

  AuthToken authToken;
  String alias;

  public TokenResponse(boolean success) {
    super(success);
  }

  public TokenResponse(boolean success, String message) {
    super(success, message);
  }

  public TokenResponse(AuthToken authToken, String alias) {
    super(true, null);
    this.authToken = authToken;
    this.alias = alias;

  }

  public AuthToken getAuthToken() {
    return authToken;
  }

  public void setAuthToken(AuthToken authToken) {
    this.authToken = authToken;
  }

  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }
}
