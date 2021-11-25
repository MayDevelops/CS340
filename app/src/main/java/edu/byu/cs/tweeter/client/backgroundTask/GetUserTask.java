package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.UserResponse;

/**
 * Background task that returns the profile for a specified user.
 */
public class GetUserTask extends AuthorizedTask {

  public static final String USER_KEY = "user";
  private static final String URL_PATH = "/user";


  /**
   * Alias (or handle) for user whose profile is being retrieved.
   */
  private final String alias;

  private User user;

  public GetUserTask(AuthToken authToken, String alias, Handler messageHandler) {
    super(authToken, messageHandler);
    this.alias = alias;
  }

  @Override
  protected void runTask() throws IOException {

    try {
      UserRequest request = new UserRequest(alias);
      UserResponse response = ServerFacade.getServerFacade().getUser(request, URL_PATH);

      if (response.isSuccess()) {
        user = response.getUser();
        BackgroundTaskUtils.loadImage(user);
      } else {
        sendFailedMessage(response.getMessage());
      }
    } catch (Exception e) {
      Log.e("GetStoryTask", e.getMessage(), e);
      sendExceptionMessage(e);
    }
  }


  @Override
  protected void loadBundle(Bundle msgBundle) {
    msgBundle.putSerializable(USER_KEY, user);
  }

}
