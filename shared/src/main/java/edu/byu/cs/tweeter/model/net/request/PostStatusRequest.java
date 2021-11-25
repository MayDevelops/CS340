package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.Status;

public class PostStatusRequest extends Request {

  public PostStatusRequest() {
  }

  private Status status;

  public PostStatusRequest(Status status) {
    this.status = status;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }
}
