package edu.byu.cs.tweeter.server.service;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;

import java.util.Iterator;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowerCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.FollowerCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingCountResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.model.net.response.UserResponse;
import edu.byu.cs.tweeter.server.factories.abstracts.UserAbstractFactory;
import edu.byu.cs.tweeter.server.helpers.Passwords;

public class UserService extends FactoryConfig {

  public UserService() {
    super();
  }

  public LoginResponse login(LoginRequest request) {
    UserAbstractFactory userDAO = FactoryConfig.userDAO;

    ItemCollection<QueryOutcome> items = userDAO.login(request);

    Iterator<Item> iterator = items.iterator();
    Item item = iterator.next();

    byte[] dbSalt = item.getBinary("salt");
    byte[] dbPassword = item.getBinary("password");
    Passwords passwords = new Passwords();
    boolean match = passwords.isExpectedPassword(request.getPassword().toCharArray(), dbSalt, dbPassword);

    if (match) {
      String userHandle = request.getUsername();
      String firstName = item.getString("first_name");
      String lastName = item.getString("last_name");
      String imageURL = item.getString("image_url");
      AuthToken authToken = new AuthToken();
      User user = new User(firstName, lastName, userHandle, imageURL);
      return new LoginResponse(user, authToken);
    } else {
      return new LoginResponse("Failed to Login");
    }
  }

  public RegisterResponse register(RegisterRequest request) {
    UserAbstractFactory userDAO = FactoryConfig.userDAO;

    Item outcome = userDAO.register(request);
    if (outcome != null) {
      User newUser = new User(
              outcome.getString("first_name"),
              outcome.getString("last_name"),
              outcome.getString("user_handle"),
              outcome.getString("image_url"));

      AuthToken authToken = new AuthToken();
      return new RegisterResponse(newUser, authToken);
    } else {
      return new RegisterResponse("Failed to Register");
    }
  }

  public LogoutResponse logout(LogoutRequest request) {
    AuthToken authToken = request.getAuthToken();
    return new LogoutResponse(authToken);
  }

  public UserResponse getUser(UserRequest request) {
    UserAbstractFactory userDAO = FactoryConfig.userDAO;

    Item outcome = userDAO.getUser(request);

    User newUser = new User(
            outcome.getString("first_name"),
            outcome.getString("last_name"),
            outcome.getString("user_handle"),
            outcome.getString("image_url"));


    return new UserResponse(newUser);
  }

  public FollowerCountResponse getFollowerCount(FollowerCountRequest request) {
    UserAbstractFactory userDAO = FactoryConfig.userDAO;

    return new FollowerCountResponse(userDAO.getFollowersCount(request));
  }

  public FollowingCountResponse getFollowingCount(FollowingCountRequest request) {
    UserAbstractFactory userDAO = FactoryConfig.userDAO;

    return new FollowingCountResponse(userDAO.getFollowingCount(request));

  }


}
