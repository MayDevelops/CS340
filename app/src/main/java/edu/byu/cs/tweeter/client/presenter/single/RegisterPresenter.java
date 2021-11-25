package edu.byu.cs.tweeter.client.presenter.single;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;

public class RegisterPresenter implements UserService.RegisterObserver {
  private RegisterView view;

  public RegisterPresenter(RegisterView view) {
    this.view = view;
  }

  public void register(String firstName, String lastName, String alias, String password, String imageBytes) {

    UserService service = new UserService(this);
    RegisterRequest registerRequest = new RegisterRequest(firstName, lastName, alias, password, imageBytes);

    service.registerTask(registerRequest);
  }

  @Override
  public void registerSucceeded(User registeredUser, AuthToken authToken) {
    String welcomeMessage = "Hello " + registeredUser.getName();
    view.displayToast(welcomeMessage);

    view.register(registeredUser, authToken);
  }

  @Override
  public void handleFailure(String message) {
    view.displayToast(message);
  }

  public interface RegisterView extends SingleView {
    void register(User registeredUser, AuthToken authToken);
  }
}
