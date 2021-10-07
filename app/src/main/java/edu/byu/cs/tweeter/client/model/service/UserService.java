package edu.byu.cs.tweeter.client.model.service;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.backgroundTask.handler.BackgroundTaskHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.TaskExecutor;
import edu.byu.cs.tweeter.client.backgroundTask.observer.ServiceObserver;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class UserService {

  public static void getUsers() {
  }

  public void login(String alias, String password, LoginObserver observer) {
    LoginTask loginTask = new LoginTask(alias, password, new LoginHandler(observer));
    new TaskExecutor<>(loginTask);
  }

  public void logout(LogoutObserver observer) {
    LogoutTask logoutTask = new LogoutTask(Cache.getInstance().getCurrUserAuthToken(), new LogoutHandler(observer));
    new TaskExecutor<>(logoutTask);
  }

  public void register(String firstName, String lastName, String alias, String password, String imageBytes, RegisterObserver observer) {
    RegisterTask registerTask = new RegisterTask(firstName, lastName,
            alias, password, imageBytes, new RegisterHandler(observer));

    new TaskExecutor<>(registerTask);
  }

  public void getUser(AuthToken authToken, String alias, GetUserObserver observer) {
    GetUserTask getUserTask = new GetUserTask(authToken, alias, new GetUserHandler(observer));
    new TaskExecutor<>(getUserTask);
  }


  public interface LoginObserver extends ServiceObserver {
    void loginSucceeded(AuthToken authToken, User user);
  }

  public interface LogoutObserver extends ServiceObserver {
    void logoutSucceeded();

    void logout();
  }


  public interface GetUserObserver extends ServiceObserver {
    void getUserSucceeded(User user);
  }

  public interface RegisterObserver extends ServiceObserver {
    void registerSucceeded(User registeredUser, AuthToken authToken);
  }

  private static class LoginHandler extends BackgroundTaskHandler {

    public LoginHandler(UserService.LoginObserver loginObserver) {
      super(loginObserver);
    }

    @Override
    protected String getFailedMessagePrefix() {
      return null;
    }

    @Override
    protected void handleSuccessMessage(ServiceObserver observer, Bundle data) {
      User loggedInUser = (User) data.getSerializable(LoginTask.USER_KEY);
      AuthToken authToken = (AuthToken) data.getSerializable(LoginTask.AUTH_TOKEN_KEY);

      Cache.getInstance().setCurrUser(loggedInUser);
      Cache.getInstance().setCurrUserAuthToken(authToken);

      ((LoginObserver) observer).loginSucceeded(authToken, loggedInUser);
    }
  }

  private static class LogoutHandler extends BackgroundTaskHandler {

    public LogoutHandler(LogoutObserver observer) {
      super(observer);
    }

    @Override
    protected String getFailedMessagePrefix() {
      return "User Service";
    }

    @Override
    protected void handleSuccessMessage(ServiceObserver observer, Bundle data) {
      ((LogoutObserver) observer).logoutSucceeded();
    }
  }

  private static class RegisterHandler extends BackgroundTaskHandler {

    public RegisterHandler(RegisterObserver observer) {
      super(observer);
    }

    @Override
    protected String getFailedMessagePrefix() {
      return "User Service";
    }

    @Override
    protected void handleSuccessMessage(ServiceObserver observer, Bundle data) {
      User registeredUser = (User) data.getSerializable(RegisterTask.USER_KEY);
      AuthToken authToken = (AuthToken) data.getSerializable(RegisterTask.AUTH_TOKEN_KEY);
      ((RegisterObserver) observer).registerSucceeded(registeredUser, authToken);
    }
  }

  private static class GetUserHandler extends BackgroundTaskHandler {

    public GetUserHandler(GetUserObserver observer) {
      super(observer);
    }

    @Override
    protected String getFailedMessagePrefix() {
      return "User Service";
    }

    @Override
    protected void handleSuccessMessage(ServiceObserver observer, Bundle data) {
      User user = (User) data.getSerializable(GetUserTask.USER_KEY);
      ((GetUserObserver) observer).getUserSucceeded(user);
    }
  }

}


