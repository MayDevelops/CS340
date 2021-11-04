package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;
import android.util.Log;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.request.FollowCountRequest;
import edu.byu.cs.tweeter.model.net.response.FollowCountResponse;

/**
 * Background task that queries how many followers a user has.
 */
public class GetFollowersCountTask extends GetCountTask {
  FollowCountRequest followCountRequest;
  private final String URL_PATH = "/count";

  public GetFollowersCountTask(FollowCountRequest followCountRequest, Handler messageHandler) {
    super(followCountRequest.getAuthToken(), followCountRequest.getUser(), messageHandler);
    this.followCountRequest = followCountRequest;
  }

  @Override
  protected int runCountTask() {
    try {
      FollowCountRequest request = this.followCountRequest;
      FollowCountResponse response = ServerFacade.getServerFacade().getFollowCount(request, URL_PATH);

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
