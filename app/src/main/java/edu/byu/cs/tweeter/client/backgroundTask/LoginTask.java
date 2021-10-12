package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;

import edu.byu.cs.tweeter.client.state.State;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that logs in a user (i.e., starts a session).
 */
public class LoginTask extends AuthenticationTask {

  public LoginTask(String username, String password, Handler messageHandler) {
    super(messageHandler, username, password);
  }

  @Override
  protected Pair<User, AuthToken> runAuthenticationTask() {
    User loggedInUser = getFakeData().getFirstUser();
    AuthToken authToken = getFakeData().getAuthToken();
    State.user = loggedInUser;
    State.authToken = authToken;
    return new Pair<>(loggedInUser, authToken);
  }
}
