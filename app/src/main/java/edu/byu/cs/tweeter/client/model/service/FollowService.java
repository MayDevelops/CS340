package edu.byu.cs.tweeter.client.model.service;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService {

  private static final int PAGE_SIZE = 10;

  private User selectedUser;
  private AuthToken authToken;

  public void getFollowers(AuthToken authToken, User user, User lastFollower, GetFollowerObserver observer) {
    GetFollowersTask getFollowersTask = new GetFollowersTask(authToken,
            user, PAGE_SIZE, lastFollower, new GetFollowersHandler(observer));
    ExecutorService executor = Executors.newSingleThreadExecutor();
    executor.execute(getFollowersTask);
  }

  public interface GetFollowingObserver {
    void getFollowingSucceeded(List<User> users, boolean hasMorePages);

    void getFollowingFailed(String e);

    void getFollowingThrewException(Exception e);

    void setLastFollowee(List<User> followees, boolean hasMorepages);
  }

  public interface GetFollowerObserver {
    void getFollowerSucceeded(List<User> users, boolean hasMorePages);

    void getFollowerFailed(String e);

    void getFollowerThrewException(Exception e);

    void setLastFollowee(List<User> followees, boolean hasMorepages);
  }

  public interface GetFollowObserver {
    void getFollowSucceeded();

    void getFollowFailed(String message);

    void getFollowThrewException(Exception e);

    void follow(AuthToken authToken, User user);
  }

  public interface GetUnfollowObserver {
    void getUnfollowSucceeded();

    void getUnfollowFailed(String e);

    void getUnfollowThrewException(Exception e);

    void unfollow(AuthToken authToken, User user);
  }

  public interface GetFollowerCountObserver {

    void getFollowerCountFailed(String e);

    void getFollowerCountThrewException(Exception e);

    void setFollowerCount(int count);
  }

  public interface GetFolloweeCountObserver {

    void getFolloweeCountFailed(String e);

    void getFolloweeCountThrewException(Exception e);

    void setFolloweeCount(int count);

  }

  public interface CheckFollowerObserver {
    void checkFollowerSucceeded(boolean isFollower);

    void checkFollowerFailed(String message);

    void checkFollowerThrewException(Exception ex);
  }

  public void isFollower(AuthToken authToken, User user, User selectedUser, CheckFollowerObserver observer) {
    IsFollowerTask isFollowerTask = new IsFollowerTask(authToken,
            user, selectedUser, new IsFollowerHandler(observer));
    ExecutorService executor = Executors.newSingleThreadExecutor();
    executor.execute(isFollowerTask);
  }

  // IsFollowerHandler

  private static class IsFollowerHandler extends Handler {

    private final CheckFollowerObserver observer;

    public IsFollowerHandler(CheckFollowerObserver observer) {
      this.observer = observer;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
      boolean success = msg.getData().getBoolean(IsFollowerTask.SUCCESS_KEY);
      if (success) {
        boolean isFollower = msg.getData().getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
        observer.checkFollowerSucceeded(isFollower);
      } else if (msg.getData().containsKey(IsFollowerTask.MESSAGE_KEY)) {
        String message = msg.getData().getString(IsFollowerTask.MESSAGE_KEY);
        observer.checkFollowerFailed(message);
      } else if (msg.getData().containsKey(IsFollowerTask.EXCEPTION_KEY)) {
        Exception ex = (Exception) msg.getData().getSerializable(IsFollowerTask.EXCEPTION_KEY);
        observer.checkFollowerThrewException(ex);
      }
    }
  }


  public void getFollowing(AuthToken authToken, User targetUser, User lastFollowee, GetFollowingObserver observer) {
    GetFollowingTask getFollowingTask = new GetFollowingTask(authToken,
            targetUser, PAGE_SIZE, lastFollowee, new GetFollowingHandler(observer));
    ExecutorService executor = Executors.newSingleThreadExecutor();
    executor.execute(getFollowingTask);
  }

  private static class GetFollowersHandler extends Handler {
    GetFollowerObserver observer;

    GetFollowersHandler(GetFollowerObserver observer) {
      this.observer = observer;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
      boolean success = msg.getData().getBoolean(GetFollowersTask.SUCCESS_KEY);
      if (success) {
        List<User> followers = (List<User>) msg.getData().getSerializable(GetFollowersTask.FOLLOWERS_KEY);
        boolean hasMorePages = msg.getData().getBoolean(GetFollowersTask.MORE_PAGES_KEY);
        observer.setLastFollowee(followers, hasMorePages);
        observer.getFollowerSucceeded(followers, hasMorePages);
      } else if (msg.getData().containsKey(GetFollowersTask.MESSAGE_KEY)) {
        String message = msg.getData().getString(GetFollowersTask.MESSAGE_KEY);
        observer.getFollowerFailed(message);
      } else if (msg.getData().containsKey(GetFollowersTask.EXCEPTION_KEY)) {
        Exception ex = (Exception) msg.getData().getSerializable(GetFollowersTask.EXCEPTION_KEY);
        observer.getFollowerThrewException(ex);
      }
    }
  }

  private static class GetFollowingHandler extends Handler {
    private final GetFollowingObserver observer;

    public GetFollowingHandler(GetFollowingObserver observer) {
      this.observer = observer;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
      boolean success = msg.getData().getBoolean(GetFollowingTask.SUCCESS_KEY);
      if (success) {
        List<User> followees = (List<User>) msg.getData().getSerializable(GetFollowingTask.FOLLOWEES_KEY);
        boolean hasMorePages = msg.getData().getBoolean(GetFollowingTask.MORE_PAGES_KEY);
        observer.setLastFollowee(followees, hasMorePages);
        observer.getFollowingSucceeded(followees, hasMorePages);
      } else if (msg.getData().containsKey(GetFollowingTask.MESSAGE_KEY)) {
        String message = msg.getData().getString(GetFollowingTask.MESSAGE_KEY);
        observer.getFollowingFailed(message);
      } else if (msg.getData().containsKey(GetFollowingTask.EXCEPTION_KEY)) {
        Exception ex = (Exception) msg.getData().getSerializable(GetFollowingTask.EXCEPTION_KEY);
        observer.getFollowingThrewException(ex);
      }
    }
  }

  @SuppressLint("HandlerLeak")
  private class FollowHandler extends Handler {

    GetFollowObserver observer;
    GetFolloweeCountObserver followeeCountObserver;
    GetFollowerCountObserver followerCountObserver;

    FollowHandler(GetFollowObserver observer,
                  GetFolloweeCountObserver followeeCountObserver,
                  GetFollowerCountObserver followerCountObserver) {
      this.observer = observer;
      this.followeeCountObserver = followeeCountObserver;
      this.followerCountObserver = followerCountObserver;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
      boolean success = msg.getData().getBoolean(FollowTask.SUCCESS_KEY);
      if (success) {
        updateSelectedUserFollowingAndFollowers(followerCountObserver, followeeCountObserver);
        observer.getFollowSucceeded();
      } else if (msg.getData().containsKey(FollowTask.MESSAGE_KEY)) {
        String message = msg.getData().getString(FollowTask.MESSAGE_KEY);
        observer.getFollowFailed(message);
      } else if (msg.getData().containsKey(FollowTask.EXCEPTION_KEY)) {
        Exception ex = (Exception) msg.getData().getSerializable(FollowTask.EXCEPTION_KEY);
        observer.getFollowThrewException(ex);
      }
    }
  }

  @SuppressLint("HandlerLeak")
  private class UnfollowHandler extends Handler {

    GetUnfollowObserver observer;
    GetFolloweeCountObserver followeeCountObserver;
    GetFollowerCountObserver followerCountObserver;

    UnfollowHandler(GetUnfollowObserver observer,
                    GetFolloweeCountObserver followeeCountObserver,
                    GetFollowerCountObserver followerCountObserver) {
      this.observer = observer;
      this.followeeCountObserver = followeeCountObserver;
      this.followerCountObserver = followerCountObserver;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
      boolean success = msg.getData().getBoolean(UnfollowTask.SUCCESS_KEY);
      if (success) {
        updateSelectedUserFollowingAndFollowers(followerCountObserver, followeeCountObserver);
        observer.getUnfollowSucceeded();
      } else if (msg.getData().containsKey(UnfollowTask.MESSAGE_KEY)) {
        String message = msg.getData().getString(UnfollowTask.MESSAGE_KEY);
        observer.getUnfollowFailed(message);
      } else if (msg.getData().containsKey(UnfollowTask.EXCEPTION_KEY)) {
        Exception ex = (Exception) msg.getData().getSerializable(UnfollowTask.EXCEPTION_KEY);
        observer.getUnfollowThrewException(ex);
      }
    }
  }

  public void followTask(AuthToken authtoken, User selectedUser, GetFollowObserver observer,
                         GetFolloweeCountObserver followeeCountObserver,
                         GetFollowerCountObserver followerCountObserver) {
    this.authToken = authtoken;
    this.selectedUser = selectedUser;
    FollowTask followTask = new FollowTask(authtoken,
            selectedUser, new FollowHandler(observer, followeeCountObserver, followerCountObserver));
    ExecutorService executor = Executors.newSingleThreadExecutor();
    executor.execute(followTask);
  }

  public void unfollowTask(AuthToken authtoken, User selectedUser,
                           GetUnfollowObserver observer,
                           GetFolloweeCountObserver followeeCountObserver,
                           GetFollowerCountObserver followerCountObserver) {
    this.authToken = authtoken;
    this.selectedUser = selectedUser;
    UnfollowTask unfollowTask = new UnfollowTask(authtoken,
            selectedUser, new UnfollowHandler(observer, followeeCountObserver, followerCountObserver));
    ExecutorService executor = Executors.newSingleThreadExecutor();
    executor.execute(unfollowTask);
  }

  private static class GetFollowersCountHandler extends Handler {

    private final GetFollowerCountObserver observer;

    public GetFollowersCountHandler(GetFollowerCountObserver observer) {
      this.observer = observer;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
      boolean success = msg.getData().getBoolean(GetFollowersCountTask.SUCCESS_KEY);
      if (success) {
        int count = msg.getData().getInt(GetFollowersCountTask.COUNT_KEY);
        observer.setFollowerCount(count);
      } else if (msg.getData().containsKey(GetFollowersCountTask.MESSAGE_KEY)) {
        String message = msg.getData().getString(GetFollowersCountTask.MESSAGE_KEY);
        observer.getFollowerCountFailed(message);
      } else if (msg.getData().containsKey(GetFollowersCountTask.EXCEPTION_KEY)) {
        Exception ex = (Exception) msg.getData().getSerializable(GetFollowersCountTask.EXCEPTION_KEY);
        observer.getFollowerCountThrewException(ex);
      }
    }
  }

  private static class GetFollowingCountHandler extends Handler {

    private final GetFolloweeCountObserver observer;

    public GetFollowingCountHandler(GetFolloweeCountObserver observer) {
      this.observer = observer;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
      boolean success = msg.getData().getBoolean(GetFollowingCountTask.SUCCESS_KEY);
      if (success) {
        int count = msg.getData().getInt(GetFollowingCountTask.COUNT_KEY);
        observer.setFolloweeCount(count);
      } else if (msg.getData().containsKey(GetFollowingCountTask.MESSAGE_KEY)) {
        String message = msg.getData().getString(GetFollowingCountTask.MESSAGE_KEY);
        observer.getFolloweeCountFailed(message);
      } else if (msg.getData().containsKey(GetFollowingCountTask.EXCEPTION_KEY)) {
        Exception ex = (Exception) msg.getData().getSerializable(GetFollowingCountTask.EXCEPTION_KEY);
        observer.getFolloweeCountThrewException(ex);
      }
    }
  }

  public void updateSelectedUserFollowingAndFollowers(GetFollowerCountObserver followerCountObserver, GetFolloweeCountObserver followeeCountObserver) {
    ExecutorService executor = Executors.newFixedThreadPool(2);

    GetFollowersCountTask followersCountTask = new GetFollowersCountTask(authToken,
            selectedUser, new GetFollowersCountHandler(followerCountObserver));
    executor.execute(followersCountTask);

    GetFollowingCountTask followingCountTask = new GetFollowingCountTask(authToken,
            selectedUser, new GetFollowingCountHandler(followeeCountObserver));
    executor.execute(followingCountTask);
  }
}