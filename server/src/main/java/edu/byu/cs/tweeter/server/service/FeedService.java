package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;

public class FeedService {

  public PostStatusResponse postStatus(PostStatusRequest request) {

    return new PostStatusResponse(request.getStatus());
  }

}
