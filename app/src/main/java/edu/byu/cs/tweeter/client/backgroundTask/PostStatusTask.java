package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.state.State;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;

/**
 * Background task that posts a new status sent by a user.
 */
public class PostStatusTask extends AuthorizedTask {

  /**
   * The new status being sent. Contains all properties of the status,
   * including the identity of the user sending the status.
   */
  private final Status status;

  private static final String URL_PATH = "/post";


  public PostStatusTask(AuthToken authToken, PostStatusRequest request, Handler messageHandler) {
    super(authToken, messageHandler);
    this.status = request.getStatus();
  }

  @Override
  protected void runTask() {
    // We could do this from the presenter, without a task and handler, but we will
    // eventually access the database from here when we aren't using dummy data.

    try {
      PostStatusRequest request = new PostStatusRequest(status);
      PostStatusResponse response = ServerFacade.getServerFacade().postStatus(request, URL_PATH);

      if (response.isSuccess()) {
        //do the goodies here when we have db

      } else {
        sendFailedMessage(response.getMessage());
      }
    } catch (Exception e) {
      Log.e("PostStatusTask", e.getMessage(), e);
      sendExceptionMessage(e);
    }

  }

  @Override
  protected void loadBundle(Bundle msgBundle) {
    // Nothing to load
  }
}
