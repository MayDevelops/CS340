package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;
import android.util.Log;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.request.FollowerCountRequest;
import edu.byu.cs.tweeter.model.net.response.FollowerCountResponse;

/**
 * Background task that queries how many followers a user has.
 */
public class GetFollowersCountTask extends GetCountTask {
  FollowerCountRequest followerCountRequest;
  private final String URL_PATH = "/getfollowercount";

  public GetFollowersCountTask(FollowerCountRequest followerCountRequest, Handler messageHandler) {
    super(followerCountRequest.getAuthToken(), followerCountRequest.getUser(), messageHandler);
    this.followerCountRequest = followerCountRequest;
  }

  @Override
  protected int runCountTask() {
    try {
      FollowerCountRequest request = this.followerCountRequest;
      FollowerCountResponse response = ServerFacade.getServerFacade().getFollowerCount(request, URL_PATH);

      if (response.isSuccess()) {
        return response.getFollowerCount();
      } else {
        sendFailedMessage(response.getMessage());
      }

    } catch (Exception e) {
      Log.e("GetCountTask", e.getMessage(), e);
      sendExceptionMessage(e);
    }
    return 0;
  }
}
