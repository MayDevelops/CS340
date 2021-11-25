package edu.byu.cs.tweeter.server.service;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;

import java.util.Iterator;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowerCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.request.TokenRequest;
import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.FollowerCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingCountResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.model.net.response.TokenResponse;
import edu.byu.cs.tweeter.model.net.response.UserResponse;
import edu.byu.cs.tweeter.server.factories.abstracts.AuthAbstractFactory;
import edu.byu.cs.tweeter.server.factories.abstracts.UserAbstractFactory;
import edu.byu.cs.tweeter.server.helpers.Passwords;

public class UserService extends ServiceHelper {
  UserAbstractFactory userDAO = ServiceHelper.userDAO;
  AuthAbstractFactory authDAO = ServiceHelper.authDAO;

  public UserService() {
    super();
  }

  public LoginResponse login(LoginRequest request) {
    ItemCollection<QueryOutcome> items = userDAO.login(request);

    Iterator<Item> iterator = items.iterator();
    Item item = iterator.next();

    byte[] dbSalt = item.getBinary("salt");
    byte[] dbPassword = item.getBinary("password");
    Passwords passwords = new Passwords();
    boolean match = passwords.isExpectedPassword(request.getPassword().toCharArray(), dbSalt, dbPassword);

    if (match) {
//      String userHandle = request.getUsername();
//      String firstName = item.getString("first_name");
//      String lastName = item.getString("last_name");
//      String imageURL = item.getString("image_url");
      AuthToken authToken = new AuthToken();
//      User user = new User(firstName, lastName, userHandle, imageURL);
      return new LoginResponse(buildUser(item), authToken);
    } else {
      return new LoginResponse("Failed to Login");
    }
  }

  public RegisterResponse register(RegisterRequest request) {
    Item outcome = userDAO.register(request);

    if (outcome != null) {
//      User newUser = new User(
//              outcome.getString("first_name"),
//              outcome.getString("last_name"),
//              outcome.getString("user_handle"),
//              outcome.getString("image_url"));

      AuthToken authToken = new AuthToken();
      return new RegisterResponse(buildUser(outcome), authToken);
    } else {
      return new RegisterResponse("Failed to Register");
    }
  }

  public LogoutResponse logout(LogoutRequest request) {
    AuthToken authToken = request.getAuthToken();
    return new LogoutResponse(authToken);
  }

  public UserResponse getUser(UserRequest request) {
    Item outcome = userDAO.getUser(request);
//
//    User newUser = new User(
//            outcome.getString("first_name"),
//            outcome.getString("last_name"),
//            outcome.getString("user_handle"),
//            outcome.getString("image_url"));

    return new UserResponse(buildUser(outcome));
  }

  public FollowerCountResponse getFollowerCount(FollowerCountRequest request) {
    return new FollowerCountResponse(userDAO.getFollowersCount(request));
  }

  public FollowingCountResponse getFollowingCount(FollowingCountRequest request) {
    return new FollowingCountResponse(userDAO.getFollowingCount(request));
  }

  public TokenResponse getToken(TokenRequest request) {
    Item token = authDAO.fetchToken(request);

    AuthToken authToken = new AuthToken(token.getString("auth_token"));

    return new TokenResponse(authToken, token.getString("user_handle"));
  }

  public TokenResponse registerToken(TokenRequest request) {
    PutItemOutcome outcome = authDAO.registerToken(request);

    Item item = outcome.getItem();
    AuthToken authToken = new AuthToken(item.getString("auth_token"));

    return new TokenResponse(authToken, item.getString("user_handle"));
  }

//  User buildUser(Item item) {
//    String userHandle = item.getString("user_handle");
//    String firstName = item.getString("first_name");
//    String lastName = item.getString("last_name");
//    String imageURL = item.getString("image_url");
//    return new User(firstName, lastName, userHandle, imageURL);
//  }


}
