package edu.byu.cs.tweeter.client.model.net;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.FollowerCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowUserRequest;
import edu.byu.cs.tweeter.model.net.request.FollowerPageRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingPageRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.request.UnfollowUserRequest;
import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.FollowerCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowUserResponse;
import edu.byu.cs.tweeter.model.net.response.FollowerPageResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingPageResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.model.net.response.UnfollowUserResponse;
import edu.byu.cs.tweeter.model.net.response.UserResponse;

public class ServerFacade {

  private static final String SERVER_URL = "https://dqijc97m3e.execute-api.us-west-2.amazonaws.com/Fixin";
  private static ServerFacade serverFacade;

  private final ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);

  public static ServerFacade getServerFacade() {
    if (serverFacade == null) {
      serverFacade = new ServerFacade();
    }

    return serverFacade;
  }

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

  public StoryResponse getStory(StoryRequest storyRequest, String urlPath) throws IOException, TweeterRemoteException {
    StoryResponse response = clientCommunicator.doPost(urlPath, storyRequest, null, StoryResponse.class);

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

  public UserResponse getUser(UserRequest request, String urlPath) throws IOException, TweeterRemoteException {
    UserResponse response = clientCommunicator.doPost(urlPath, request, null, UserResponse.class);

    if (response.isSuccess()){
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

  public UnfollowUserResponse unfollowUser(UnfollowUserRequest request, String urlPath) throws IOException, TweeterRemoteException{
    UnfollowUserResponse response = clientCommunicator.doPost(urlPath, request, null, UnfollowUserResponse.class);

    if (response.isSuccess()){
      return response;
    } else {
      throw new RuntimeException(response.getMessage());
    }
  }

  public FollowerCountResponse getFollowerCount(FollowerCountRequest request, String urlPath) throws IOException, TweeterRemoteException {
    FollowerCountResponse response = clientCommunicator.doPost(urlPath, request, null, FollowerCountResponse.class);

    if (response.isSuccess()) {
      return response;
    } else {
      throw new RuntimeException(response.getMessage());
    }
  }

  public FollowingCountResponse getFollowingCount(FollowingCountRequest request, String urlPath) throws IOException, TweeterRemoteException {
    FollowingCountResponse response = clientCommunicator.doPost(urlPath, request, null, FollowingCountResponse.class);

    if (response.isSuccess()) {
      return response;
    } else {
      throw new RuntimeException(response.getMessage());
    }
  }
}