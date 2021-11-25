package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingCountResponse;
import edu.byu.cs.tweeter.server.service.UserService;

public class FollowingCountHandler implements RequestHandler<FollowingCountRequest, FollowingCountResponse> {

  @Override
  public FollowingCountResponse handleRequest(FollowingCountRequest request, Context context) {
    UserService service = new UserService();
    return service.getFollowingCount(request);
  }
}
