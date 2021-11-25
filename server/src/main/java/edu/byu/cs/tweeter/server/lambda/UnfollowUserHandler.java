package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.UnfollowUserRequest;
import edu.byu.cs.tweeter.model.net.response.UnfollowUserResponse;
import edu.byu.cs.tweeter.server.service.config.FollowsService;


public class UnfollowUserHandler implements RequestHandler<UnfollowUserRequest, UnfollowUserResponse> {

  @Override
  public UnfollowUserResponse handleRequest(UnfollowUserRequest request, Context context) {
    FollowsService service = new FollowsService();
    return service.unfollowUser(request);
  }
}
