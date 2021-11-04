package edu.byu.cs.tweeter.client.model.net;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;

/**
 * Acts as a Facade to the Tweeter server. All network requests to the server should go through
 * this class.
 */
public class ServerFacade {

  // TODO: Set this to the invoke URL of your API. Find it by going to your API in AWS, clicking
  //  on stages in the right-side menu, and clicking on the stage you deployed your API to.
  private static final String SERVER_URL = "https://dqijc97m3e.execute-api.us-west-2.amazonaws.com/Initial";
  private static ServerFacade serverFacade;

  private final ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);

  public static ServerFacade getServerFacade() {
    if (serverFacade == null) {
      serverFacade = new ServerFacade();
    }

    return serverFacade;
  }

  /**
   * Performs a login and if successful, returns the logged in user and an auth token.
   *
   * @param request contains all information needed to perform a login.
   * @return the login response.
   */
  public LoginResponse login(LoginRequest request, String urlPath) throws IOException, TweeterRemoteException {
    LoginResponse response = clientCommunicator.doPost(urlPath, request, null, LoginResponse.class);

    if (response.isSuccess()) {
      return response;
    } else {
      throw new RuntimeException(response.getMessage());
    }
  }

  public RegisterResponse register(RegisterRequest registerRequest, String urlPath) throws IOException, TweeterRemoteException {
    RegisterResponse response = clientCommunicator.doPost(urlPath, registerRequest, null, RegisterResponse.class);

    if (response.isSuccess()) {
      return response;
    } else {
      throw new RuntimeException(response.getMessage());
    }
  }

  public LogoutResponse logout(LogoutRequest logoutRequest, String urlPath) throws IOException, TweeterRemoteException {
    LogoutResponse response = clientCommunicator.doPost(urlPath, logoutRequest, null, LogoutResponse.class);

    if (response.isSuccess()) {
      return response;
    } else {
      throw new RuntimeException(response.getMessage());
    }
  }

  public PostStatusResponse postStatus(PostStatusRequest postStatusRequest, String urlPath) throws  IOException, TweeterRemoteException {
    PostStatusResponse response = clientCommunicator.doPost(urlPath, postStatusRequest, null, PostStatusResponse.class);

    if (response.isSuccess()) {
      return  response;
    } else {
      throw new RuntimeException(response.getMessage());
    }
  }

  public FeedResponse getFeed(FeedRequest feedRequest, String urlPath) throws IOException, TweeterRemoteException {
    FeedResponse response = clientCommunicator.doPost(urlPath, feedRequest, null, FeedResponse.class);

    if(response.isSuccess()) {
      return response;
    } else {
      throw new RuntimeException(response.getMessage());
    }
  }

  /**
   * Returns the users that the user specified in the request is following. Uses information in
   * the request object to limit the number of followees returned and to return the next set of
   * followees after any that were returned in a previous request.
   *
   * @param request contains information about the user whose followees are to be returned and any
   *                other information required to satisfy the request.
   * @return the followees.
   */
  public FollowingResponse getFollowing(FollowingRequest request, String urlPath)
          throws IOException, TweeterRemoteException {

    FollowingResponse response = clientCommunicator.doPost(urlPath, request, null, FollowingResponse.class);

    if (response.isSuccess()) {
      return response;
    } else {
      throw new RuntimeException(response.getMessage());
    }
  }
}