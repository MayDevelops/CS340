package edu.byu.cs.tweeter.server.service;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import edu.byu.cs.tweeter.model.domain.AuthToken;
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
import edu.byu.cs.tweeter.server.service.config.ServiceHelper;

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
      AuthToken authToken = new AuthToken();
      authDAO.registerToken(new TokenRequest(authToken, request.getUsername(), getEpoch()));
      return new LoginResponse(buildUser(item), authToken);
    } else {
      return new LoginResponse("Failed to Login");
    }
  }

  public RegisterResponse register(RegisterRequest request) {
    Item outcome = userDAO.register(request);

    if (outcome != null) {

      AuthToken authToken = new AuthToken();
      authDAO.registerToken(new TokenRequest(authToken, request.getUsername(), getEpoch()));
      return new RegisterResponse(buildUser(outcome), authToken);
    } else {
      return new RegisterResponse("Failed to Register");
    }
  }

  public LogoutResponse logout(LogoutRequest request) {
    AuthToken authToken = request.getAuthToken();
    authDAO.removeToken(new TokenRequest(request.getAuthToken(), request.getAlias()));

    return new LogoutResponse(authToken);
  }

  public UserResponse getUser(UserRequest request) {
    Item outcome = userDAO.getUser(request);

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
    AuthToken authToken = new AuthToken(item.getString("token"));

    return new TokenResponse(authToken, item.getString("user_handle"));
  }

  public long getEpoch() {
    long time = System.currentTimeMillis()/1000;
    time += 10 * 60;
    return time;
  }

}
