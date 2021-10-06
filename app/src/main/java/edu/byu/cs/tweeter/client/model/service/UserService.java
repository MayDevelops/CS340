package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.presenter.FollowersPresenter;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class UserService {

  public static void getUsers(AuthToken currUserAuthToken, String alias, FollowersPresenter followersPresenter) {
  }

  public interface LoginObserver {
    void loginSucceeded(AuthToken authToken, User user);

    void loginFailed(String message);

    void loginThrewException(Exception e);

  }

  public interface LogoutObserver {
    void logoutSuceeded();

    void logoutFailed(String message);

    void logoutThrewException(Exception ex);

    void logout();
  }

  public interface GetUserObserver {
    void getUserSucceeded(User user);

    void getUserFailed(String string);

    void getUserThrewException(Exception ex);
  }

  public interface RegisterObserver {
    void registerSucceeded(User registeredUser, AuthToken authToken);

    void registerFailed(String message);

    void registerThrewException(Exception ex);

  }

  public void logout(LogoutObserver observer) {
    LogoutTask logoutTask = new LogoutTask(Cache.getInstance().getCurrUserAuthToken(), new LogoutHandler(observer));
    ExecutorService executor = Executors.newSingleThreadExecutor();
    executor.execute(logoutTask);
  }

  public void register(String firstName, String lastName, String alias, String password, String imageBytes, RegisterObserver observer) {
    RegisterTask registerTask = new RegisterTask(firstName, lastName,
            alias, password, imageBytes, new RegisterHandler(observer));

    ExecutorService executor = Executors.newSingleThreadExecutor();
    executor.execute(registerTask);
  }

  public void getUser(AuthToken authToken, String alias, GetUserObserver observer) {
    GetUserTask getUserTask = new GetUserTask(authToken, alias, new GetUserHandler(observer));
    ExecutorService executor = Executors.newSingleThreadExecutor();
    executor.execute(getUserTask);

  }

  private static class GetUserHandler extends Handler {
    private final GetUserObserver observer;

    public GetUserHandler(GetUserObserver observer) {
      this.observer = observer;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
      boolean success = msg.getData().getBoolean(GetUserTask.SUCCESS_KEY);
      if (success) {
        User user = (User) msg.getData().getSerializable(GetUserTask.USER_KEY);
        observer.getUserSucceeded(user);
      } else if (msg.getData().containsKey(GetUserTask.MESSAGE_KEY)) {
        String message = msg.getData().getString(GetUserTask.MESSAGE_KEY);
        observer.getUserFailed(message);
      } else if (msg.getData().containsKey(GetUserTask.EXCEPTION_KEY)) {
        Exception ex = (Exception) msg.getData().getSerializable(GetUserTask.EXCEPTION_KEY);
        observer.getUserThrewException(ex);
      }
    }
  }

  public void login(String alias, String password, LoginObserver observer) {
    // Send the login request.
    LoginTask loginTask = new LoginTask(alias, password, new LoginHandler(observer));
    ExecutorService executor = Executors.newSingleThreadExecutor();
    executor.execute(loginTask);
  }

  private static class LoginHandler extends Handler {
    private final LoginObserver observer;

    public LoginHandler(UserService.LoginObserver loginObserver) {
      this.observer = loginObserver;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
      boolean success = msg.getData().getBoolean(LoginTask.SUCCESS_KEY);
      if (success) {
        User loggedInUser = (User) msg.getData().getSerializable(LoginTask.USER_KEY);
        AuthToken authToken = (AuthToken) msg.getData().getSerializable(LoginTask.AUTH_TOKEN_KEY);

        Cache.getInstance().setCurrUser(loggedInUser);
        Cache.getInstance().setCurrUserAuthToken(authToken);

        observer.loginSucceeded(authToken, loggedInUser);
      } else if (msg.getData().containsKey(LoginTask.MESSAGE_KEY)) {
        String message = msg.getData().getString(LoginTask.MESSAGE_KEY);
        observer.loginFailed(message);
      } else if (msg.getData().containsKey(LoginTask.EXCEPTION_KEY)) {
        Exception ex = (Exception) msg.getData().getSerializable(LoginTask.EXCEPTION_KEY);
        observer.loginThrewException(ex);
      }
    }
  }

  private static class LogoutHandler extends Handler {
    LogoutObserver observer;

    public LogoutHandler(LogoutObserver observer) {
      this.observer = observer;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
      boolean success = msg.getData().getBoolean(LogoutTask.SUCCESS_KEY);
      if (success) {
        observer.logoutSuceeded();
      } else if (msg.getData().containsKey(LogoutTask.MESSAGE_KEY)) {
        String message = msg.getData().getString(LogoutTask.MESSAGE_KEY);
        observer.logoutFailed(message);
      } else if (msg.getData().containsKey(LogoutTask.EXCEPTION_KEY)) {
        Exception ex = (Exception) msg.getData().getSerializable(LogoutTask.EXCEPTION_KEY);
        observer.logoutThrewException(ex);
      }
    }
  }

  private static class RegisterHandler extends Handler {
    RegisterObserver observer;

    public RegisterHandler(RegisterObserver observer) {
      this.observer = observer;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
      boolean success = msg.getData().getBoolean(RegisterTask.SUCCESS_KEY);
      if (success) {
        User registeredUser = (User) msg.getData().getSerializable(RegisterTask.USER_KEY);
        AuthToken authToken = (AuthToken) msg.getData().getSerializable(RegisterTask.AUTH_TOKEN_KEY);
        observer.registerSucceeded(registeredUser, authToken);
      } else if (msg.getData().containsKey(RegisterTask.MESSAGE_KEY)) {
        String message = msg.getData().getString(RegisterTask.MESSAGE_KEY);
        observer.registerFailed(message);
      } else if (msg.getData().containsKey(RegisterTask.EXCEPTION_KEY)) {
        Exception ex = (Exception) msg.getData().getSerializable(RegisterTask.EXCEPTION_KEY);
        observer.registerThrewException(ex);
      }
    }
  }
}


