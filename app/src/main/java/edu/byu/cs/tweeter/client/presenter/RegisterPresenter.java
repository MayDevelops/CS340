package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterPresenter implements UserService.RegisterObserver {
  private View view;

  public RegisterPresenter(View view) {
    this.view = view;
  }

  public void register(String firstName, String lastName, String alias, String password, String imageBytes) {
    new UserService().register(firstName, lastName, alias, password, imageBytes, this);
  }

  @Override
  public void registerSucceeded(User registeredUser, AuthToken authToken) {
    String welcomeMessage = "Hello " + registeredUser.getName();
    view.displayToast(welcomeMessage);

    view.register(registeredUser, authToken);
  }

  @Override
  public void registerFailed(String message) {
    String failMessage = "Failed to register: " + message;
    view.displayToast(failMessage);
  }

  @Override
  public void registerThrewException(Exception ex) {
    String exceptionMessage = "Failed to register because of exception: " + ex.getMessage();
    view.displayToast(exceptionMessage);
  }


  public interface View {
    void displayToast(String message);

    void register(User registeredUser, AuthToken authToken);

  }
}
