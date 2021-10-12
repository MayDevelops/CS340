package edu.byu.cs.tweeter.client.presenter.single;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter implements UserService.LoginObserver {

  private LoginView view;

  public LoginPresenter(LoginView v) {
    this.view = v;
  }

  @Override
  public void handleFailure(String message) {
    view.displayToast(message);
  }

  private String validateLogin(String alias, String password) {
    if (alias.charAt(0) != '@') {
      return ("Alias must begin with @.");
    }
    if (alias.length() < 2) {
      return ("Alias must contain 1 or more characters after the @.");
    }
    if (password.length() == 0) {
      return ("Password cannot be empty.");
    }
    return null;
  }

  @Override
  public void loginSucceeded(AuthToken authToken, User user) {
    view.launchIntent(user);
    view.displayToast("Hello " + user.getFirstName());
  }

  public void login(String alias, String pw) {
    String message = validateLogin(alias, pw);
    if (message == null) {
      view.displayToast("Logging In...:");
      new UserService().login(alias, pw, this);
    } else {
      view.displayToast("Login failed: " + message);
    }
  }

  public interface LoginView extends SingleView {
    void launchIntent(User user);
  }
}
