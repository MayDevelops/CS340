package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowUserRequest;
import edu.byu.cs.tweeter.model.net.response.FollowUserResponse;

/**
 * Background task that establishes a following relationship between two users.
 */
public class FollowTask extends AuthorizedTask {
  /**
   * The user that is being followed.
   */
  private final User user;
  private final AuthToken authToken;
  private static final String URL_PATH = "/followuser";


  public FollowTask(FollowUserRequest followUserRequest, Handler messageHandler) {
    super(followUserRequest.getAuthToken(), messageHandler);
    this.authToken = followUserRequest.getAuthToken();
    this.user = followUserRequest.getUser();
  }

  @Override
  protected void runTask() {
    // We could do this from the presenter, without a task and handler, but we will
    // eventually access the database from here when we aren't using dummy data.

    try {
      FollowUserRequest request = new FollowUserRequest(authToken, user);
      user.getImageBytes().toString();
      FollowUserResponse response = ServerFacade.getServerFacade().followUser(request, URL_PATH);

      if (response.isSuccess()) {
        //do the logic here
      } else {
        sendFailedMessage(response.getMessage());
      }
    } catch (Exception e) {
      Log.e("FollowTask", e.getMessage(), e);
      sendExceptionMessage(e);
    }
  }

  @Override
  protected void loadBundle(Bundle msgBundle) {
    // Nothing to load
  }
}
