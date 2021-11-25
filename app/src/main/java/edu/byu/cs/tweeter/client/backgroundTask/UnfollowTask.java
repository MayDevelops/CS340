package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.request.UnfollowUserRequest;
import edu.byu.cs.tweeter.model.net.response.UnfollowUserResponse;

public class UnfollowTask extends AuthorizedTask {
  UnfollowUserRequest unfollowUserRequest;
  private static final String URL_PATH = "/unfollowuser";

  public UnfollowTask(UnfollowUserRequest unfollowUserRequest, Handler messageHandler) {
    super(unfollowUserRequest.getAuthToken(), messageHandler);
    this.unfollowUserRequest = unfollowUserRequest;
  }

  @Override
  protected void runTask() {
    try {
      UnfollowUserResponse response = ServerFacade.getServerFacade().unfollowUser(unfollowUserRequest, URL_PATH);

      if (response.isSuccess()) {
        BackgroundTaskUtils.loadImage(unfollowUserRequest.getSelectedUser());
      } else {
        sendFailedMessage("Failed to unfollow user");
      }
    } catch (Exception e) {
      Log.e("UnfollowTask", e.getMessage(), e);
      sendExceptionMessage(e);
    }
  }

  @Override
  protected void loadBundle(Bundle msgBundle) {
    // Nothing to load
  }
}
