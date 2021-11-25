package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.request.parents.Request;

public class TokenRequest extends Request {

  String alias;
  AuthToken authToken;

  public TokenRequest(AuthToken authToken, String alias) {
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
