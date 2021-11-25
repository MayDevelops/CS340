package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.TokenRequest;
import edu.byu.cs.tweeter.model.net.response.TokenResponse;
import edu.byu.cs.tweeter.server.service.UserService;

public class GetAuthHandler implements RequestHandler<TokenRequest, TokenResponse> {

  @Override
  public TokenResponse handleRequest(TokenRequest tokenRequest, Context context) {
    UserService userService = new UserService();
    return userService.getToken(tokenRequest);
  }
}
