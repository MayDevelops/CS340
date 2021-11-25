package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;
import android.util.Log;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingCountResponse;

/**
 * Background task that queries how many other users a specified user is following.
 */
public class GetFollowingCountTask extends GetCountTask {

  FollowingCountRequest followingCountRequest;
  private final String URL_PATH = "/getfollowingcount";

  public GetFollowingCountTask(FollowingCountRequest followingCountRequest, Handler messageHandler) {
    super(followingCountRequest.getAuthToken(), followingCountRequest.getUser(), messageHandler);
    this.followingCountRequest = followingCountRequest;
  }

  @Override
  protected int runCountTask() {
    try {
      FollowingCountRequest request = this.followingCountRequest;
      FollowingCountResponse response = ServerFacade.getServerFacade().getFollowingCount(request, URL_PATH);

      if (response.isSuccess()) {
        return response.getFollowingCount();
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
