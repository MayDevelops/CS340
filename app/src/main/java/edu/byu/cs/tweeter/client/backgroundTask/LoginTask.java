package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.state.State;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;

/**
 * Background task that logs in a user (i.e., starts a session).
 */
public class LoginTask extends BackgroundTask {

  private final String username;
  private final String password;

  private static final String URL_PATH = "/login";
  public static final String USER_KEY = "user";
  public static final String AUTH_TOKEN_KEY = "auth-token";


  public LoginTask(LoginRequest loginRequest, Handler messageHandler) {
    super(messageHandler);
    this.username = loginRequest.getUsername();
    this.password = loginRequest.getPassword();
  }

  @Override
  protected void runTask() throws IOException {
    try {
      LoginRequest request = new LoginRequest(username, password);
      LoginResponse response = ServerFacade.getServerFacade().login(request, URL_PATH);

      if (response.isSuccess()) {
        BackgroundTaskUtils.loadImage(response.getUser());
        State.user = response.getUser();
        State.authToken = response.getAuthToken();
        BackgroundTaskUtils.loadImage(State.user);
      } else {
        sendFailedMessage(response.getMessage());
      }

    } catch (IOException | TweeterRemoteException e) {
      Log.e("LoginTask", e.getMessage(), e);
      sendExceptionMessage(e);
    }
  }

  @Override
  protected void loadBundle(Bundle msgBundle) {
    msgBundle.putSerializable(USER_KEY, State.user);
    msgBundle.putSerializable(AUTH_TOKEN_KEY, State.authToken);
  }
}
