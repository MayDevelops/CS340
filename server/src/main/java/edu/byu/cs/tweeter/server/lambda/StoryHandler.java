package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.server.service.StoryService;

public class StoryHandler implements RequestHandler<StoryRequest, StoryResponse> {

  @Override
  public StoryResponse handleRequest(StoryRequest storyRequest, Context context) {
    StoryService storyService = new StoryService();
    return storyService.getStories(storyRequest);
  }
}
