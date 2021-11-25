package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.state.State;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;

/**
 * Background task that creates a new user account and logs in the new user (i.e., starts a session).
 */
public class RegisterTask extends BackgroundTask {

  private final String firstName;
  private final String lastName;
  private final String username;
  private final String password;
  private final String imageBytes;

  private static final String URL_PATH = "/register";
  public static final String USER_KEY = "user";
  public static final String AUTH_TOKEN_KEY = "auth-token";

  public RegisterTask(RegisterRequest registerRequest, Handler messageHandler) {
    super(messageHandler);
    this.firstName = registerRequest.getFirstName();
    this.lastName = registerRequest.getLastName();
    this.username = registerRequest.getUsername();
    this.password = registerRequest.getPassword();
    this.imageBytes = registerRequest.getImageBytes();
  }

  @Override
  protected void runTask() throws IOException {
    try {
      RegisterRequest request = new RegisterRequest(firstName, lastName, username, password, imageBytes);
      RegisterResponse response = ServerFacade.getServerFacade().register(request, URL_PATH);

      if (response.isSuccess()) {
        State.user = response.getUser();
        State.authToken = response.getAuthToken();
        BackgroundTaskUtils.loadImage(State.user);
      } else {
        sendFailedMessage(response.getMessage());
      }
    } catch (Exception e) {
      Log.e("RegisterTask", e.getMessage(), e);
      sendExceptionMessage(e);
    }
  }

  @Override
  protected void loadBundle(Bundle msgBundle) {
    msgBundle.putSerializable(USER_KEY, State.user);
    msgBundle.putSerializable(AUTH_TOKEN_KEY, State.authToken);
  }
}
