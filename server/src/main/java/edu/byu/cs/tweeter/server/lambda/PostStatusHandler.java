package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.server.service.FeedService;

public class PostStatusHandler implements RequestHandler<PostStatusRequest, PostStatusResponse> {
  @Override
  public PostStatusResponse handleRequest(PostStatusRequest postStatusRequest, Context context) {
    FeedService feedService = new FeedService();
    return feedService.postStatus(postStatusRequest);
  }
}
