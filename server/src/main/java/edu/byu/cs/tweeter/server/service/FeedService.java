package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.server.dao.FeedDAO;

public class FeedService {

  public PostStatusResponse postStatus(PostStatusRequest request) {
    return new PostStatusResponse(request.getStatus());
  }

  public FeedResponse getFeed(FeedRequest request) {
    return getFeedDAO().getFeed(request);
  }

  FeedDAO getFeedDAO() {
    return new FeedDAO();
  }

}
