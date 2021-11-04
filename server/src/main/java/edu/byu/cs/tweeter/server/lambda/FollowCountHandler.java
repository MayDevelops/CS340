package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.FollowCountRequest;
import edu.byu.cs.tweeter.model.net.response.FollowCountResponse;
import edu.byu.cs.tweeter.server.service.FollowService;

public class FollowCountHandler implements RequestHandler<FollowCountRequest, FollowCountResponse> {

  @Override
  public FollowCountResponse handleRequest(FollowCountRequest request, Context context) {
    FollowService service = new FollowService();
    return service.getFollowCount(request);
  }
}
