package edu.byu.cs.tweeter.client.model.net;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.FollowCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowUserRequest;
import edu.byu.cs.tweeter.model.net.request.FollowerPageRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingPageRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.FollowCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowUserResponse;
import edu.byu.cs.tweeter.model.net.response.FollowerPageResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingPageResponse;
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
  private static final String SERVER_URL = "https://dqijc97m3e.execute-api.us-west-2.amazonaws.com/Fixin";
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

  public FollowerPageResponse getFollowerPage(FollowerPageRequest request, String urlPath)
          throws IOException, TweeterRemoteException {

    FollowerPageResponse response = clientCommunicator.doPost(urlPath, request, null, FollowerPageResponse.class);

    if (response.isSuccess()) {
      return response;
    } else {
      throw new RuntimeException(response.getMessage());
    }
  }

  public FollowingPageResponse getFollowingPage(FollowingPageRequest request, String urlPath)
          throws IOException, TweeterRemoteException {

    FollowingPageResponse response = clientCommunicator.doPost(urlPath, request, null, FollowingPageResponse.class);

    if (response.isSuccess()) {
      return response;
    } else {
      throw new RuntimeException(response.getMessage());
    }
  }

  public FollowUserResponse followUser(FollowUserRequest request, String urlPath) throws IOException, TweeterRemoteException{
    FollowUserResponse response = clientCommunicator.doPost(urlPath, request, null, FollowUserResponse.class);

    if (response.isSuccess()){
      return response;
    } else {
      throw new RuntimeException(response.getMessage());
    }
  }

  public FollowCountResponse getFollowCount(FollowCountRequest request, String urlPath) throws IOException, TweeterRemoteException {
    FollowCountResponse response = clientCommunicator.doPost(urlPath, request, null, FollowCountResponse.class);

    if (response.isSuccess()) {
      return response;
    } else {
      throw new RuntimeException(response.getMessage());
    }
  }



}