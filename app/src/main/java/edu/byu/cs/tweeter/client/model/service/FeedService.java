package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedService {
  private AuthToken authToken;
  private User user;
  private Status lastStatus;


  public void getFeed(AuthToken authToken, User user, Status lastStatus, FeedObserver observer) {
    int PAGE_SIZE = 10;
    GetFeedTask getFeedTask = new GetFeedTask(authToken,
            user, PAGE_SIZE, lastStatus, new GetFeedHandler(observer));
    ExecutorService executor = Executors.newSingleThreadExecutor();
    executor.execute(getFeedTask);
  }

  /**
   * Message handler (i.e., observer) for GetFeedTask.
   */
  private class GetFeedHandler extends Handler {
    private final FeedObserver observer;

    public GetFeedHandler(FeedObserver observer) {
      this.observer = observer;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
      boolean success = msg.getData().getBoolean(GetFeedTask.SUCCESS_KEY);
      if (success) {
        List<Status> statuses = (List<Status>) msg.getData().getSerializable(GetFeedTask.STATUSES_KEY);
        boolean hasMorePages = msg.getData().getBoolean(GetFeedTask.MORE_PAGES_KEY);

        observer.setLastStatus(statuses, hasMorePages);
        observer.feedSucceeded(statuses);
      } else if (msg.getData().containsKey(GetFeedTask.MESSAGE_KEY)) {
        String message = msg.getData().getString(GetFeedTask.MESSAGE_KEY);
        observer.feedFailed(message);
      } else if (msg.getData().containsKey(GetFeedTask.EXCEPTION_KEY)) {
        Exception ex = (Exception) msg.getData().getSerializable(GetFeedTask.EXCEPTION_KEY);
        observer.feedThrewException(ex);
      }
    }
  }


  public interface FeedObserver {
    void feedSucceeded(List<Status> statuses);

    void feedFailed(String message);

    void feedThrewException(Exception ex);

    void setLastStatus(List<Status> statuses, boolean hasMorePages);
  }


}
