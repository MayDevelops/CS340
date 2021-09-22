package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService {

  public interface GetFollowingObserver {
    void getFollowingSucceeded(List<User> users, boolean hasMorePages);

    void getFollowingFailed(String e);

    void getFollowingThrewException(Exception e);
  }


  /**
   * @param authToken
   * @param targetUser
   * @param pageLimit
   * @param lastFollowee know where to start when we ask for more
   */
  public void getFollowing(AuthToken authToken, User targetUser, int pageLimit, User lastFollowee, GetFollowingObserver observer) { //get the next page of data when secrolling
    GetFollowingTask getFollowingTask = new GetFollowingTask(authToken,
            targetUser, pageLimit, lastFollowee, new GetFollowingHandler(observer));
    ExecutorService executor = Executors.newSingleThreadExecutor();
    executor.execute(getFollowingTask);
  }

  /**
   * Message handler (i.e., observer) for GetFollowingTask.
   */
  private class GetFollowingHandler extends Handler {
    private GetFollowingObserver observer;

    public GetFollowingHandler(GetFollowingObserver observer) {
      this.observer = observer;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
      boolean success = msg.getData().getBoolean(GetFollowingTask.SUCCESS_KEY);
      if (success) {
        List<User> followees = (List<User>) msg.getData().getSerializable(GetFollowingTask.FOLLOWEES_KEY);
        boolean hasMorePages = msg.getData().getBoolean(GetFollowingTask.MORE_PAGES_KEY);

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


