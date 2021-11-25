package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.request.FollowUserRequest;
import edu.byu.cs.tweeter.model.net.response.FollowUserResponse;

public class FollowTask extends AuthorizedTask {
  FollowUserRequest followUserRequest;
  private static final String URL_PATH = "/followuser";


  public FollowTask(FollowUserRequest followUserRequest, Handler messageHandler) {
    super(followUserRequest.getAuthToken(), messageHandler);
    this.followUserRequest = followUserRequest;
  }

  @Override
  protected void runTask() {
    try {
      FollowUserResponse response = ServerFacade.getServerFacade().followUser(followUserRequest, URL_PATH);

      if (response.isSuccess()) {
        BackgroundTaskUtils.loadImage(followUserRequest.getSelectedUser());
      } else {
        sendFailedMessage("Failed to follow user");
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
