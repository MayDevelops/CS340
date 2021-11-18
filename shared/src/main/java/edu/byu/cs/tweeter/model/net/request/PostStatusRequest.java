package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.Status;

public class PostStatusRequest extends Request{

  public PostStatusRequest() {
  }

  private Status status;

  public PostStatusRequest(Status status) {
    this.status = status;
    //todo how to transalte image bytes back and forth between aws. 
    /*
    When aws sends them back right now it is a string and then gson
    freaks out and doesn't like it

    com.google.gson.JsonSyntaxException: java.lang.IllegalStateException: Expected BEGIN_ARRAY but was STRING at line 1 column 226 path $.status.user.imageByte
    */

    status.user.setImageBytes(null);
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }
}
