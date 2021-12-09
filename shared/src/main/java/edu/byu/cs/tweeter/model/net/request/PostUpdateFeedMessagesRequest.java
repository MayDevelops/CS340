package edu.byu.cs.tweeter.model.net.request;

import java.util.List;

import edu.byu.cs.tweeter.model.net.request.parents.Request;

public class PostUpdateFeedMessagesRequest extends Request {

  private List<String> allAlias;
  private PostStatusRequest postStatusRequest;

  public PostUpdateFeedMessagesRequest() {
  }

  public PostUpdateFeedMessagesRequest(List<String> allAlias, PostStatusRequest postStatusRequest) {
    this.allAlias = allAlias;
    this.postStatusRequest = postStatusRequest;
  }

  public List<String> getAllAlias() {
    return allAlias;
  }

  public void setAllAlias(List<String> allAlias) {
    this.allAlias = allAlias;
  }

  public PostStatusRequest getPostStatusRequest() {
    return postStatusRequest;
  }

  public void setPostStatusRequest(PostStatusRequest postStatusRequest) {
    this.postStatusRequest = postStatusRequest;
  }
}
