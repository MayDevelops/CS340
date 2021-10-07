package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter implements UserService.LoginObserver {

  private View view;

  public LoginPresenter(View v) {
    this.view = v;
  }

  @Override
  public void handleFailure(String message) {
    view.displayErrorMessage(message);
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
    view.navigateToUser(user);
    view.clearErrorMessage();
    view.displayInfoMessage("Hello " + user);
  }

  public void login(String alias, String pw) {
    view.clearErrorMessage();
    view.clearInfoMessage();
    String message = validateLogin(alias, pw);
    if (message == null) {
      view.displayInfoMessage("Logging In...:");
      new UserService().login(alias, pw, this);
    } else {
      view.displayErrorMessage("Login failed: " + message);
    }
  }


  public interface View {

    void navigateToUser(User user);

    void displayErrorMessage(String message);

    void clearErrorMessage();

    void displayInfoMessage(String message);

    void clearInfoMessage();

  }


}
