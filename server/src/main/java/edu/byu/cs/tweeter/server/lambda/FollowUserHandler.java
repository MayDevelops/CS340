package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.FollowUserRequest;
import edu.byu.cs.tweeter.model.net.response.FollowUserResponse;
import edu.byu.cs.tweeter.server.service.FollowService;


public class FollowUserHandler implements RequestHandler<FollowUserRequest, FollowUserResponse> {

  @Override
  public FollowUserResponse handleRequest(FollowUserRequest request, Context context) {
    FollowService service = new FollowService();
    return service.getFollowUser(request);
  }
}
