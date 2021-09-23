package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService {

  private static final int PAGE_SIZE = 10;

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


  public void getFollowing(AuthToken authToken, User targetUser, User lastFollowee, GetFollowingObserver observer) { //get the next page of data when secrolling
    Log.e("FollowingService", "Executing using executor");

    GetFollowingTask getFollowingTask = new GetFollowingTask(authToken,
            targetUser, PAGE_SIZE, lastFollowee, new GetFollowingHandler(observer));
    ExecutorService executor = Executors.newSingleThreadExecutor();
    executor.execute(getFollowingTask);

  }

  /**
   * Message handler (i.e., observer) for GetFollowersTask.
   */
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


  /**
   * Message handler (i.e., observer) for GetFollowingTask.
   */
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

}


