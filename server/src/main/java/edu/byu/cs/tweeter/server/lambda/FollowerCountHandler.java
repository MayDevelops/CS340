package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.FollowerCountRequest;
import edu.byu.cs.tweeter.model.net.response.FollowerCountResponse;
import edu.byu.cs.tweeter.server.service.UserService;

public class FollowerCountHandler implements RequestHandler<FollowerCountRequest, FollowerCountResponse> {

  @Override
  public FollowerCountResponse handleRequest(FollowerCountRequest request, Context context) {
    UserService service = new UserService();
    return service.getFollowerCount(request);
  }
}
