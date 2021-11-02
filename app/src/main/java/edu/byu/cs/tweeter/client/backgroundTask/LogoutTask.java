package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.state.State;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;

/**
 * Background task that logs out a user (i.e., ends a session).
 */
public class LogoutTask extends BackgroundTask {
  private final AuthToken authToken;

  private static final String URL_PATH = "/logout";


  public LogoutTask(LogoutRequest logoutRequest, Handler messageHandler) {
    super(messageHandler);
    this.authToken = logoutRequest.getAuthToken();
  }

  @Override
  protected void runTask() {
    //logout the user eventually from the DB
    try {
      LogoutRequest request = new LogoutRequest(authToken);
      LogoutResponse response = ServerFacade.getServerFacade().logout(request, URL_PATH);

      if (response.isSuccess()) {
        State.user = null;
        State.authToken = null;
      } else {
        sendFailedMessage(response.getMessage());
      }
    } catch (Exception e) {
      Log.e("Logout Task", e.getMessage(), e);
      sendExceptionMessage(e);
    }
  }

  @Override
  protected void loadBundle(Bundle msgBundle) {
    // Nothing to load
  }
}
