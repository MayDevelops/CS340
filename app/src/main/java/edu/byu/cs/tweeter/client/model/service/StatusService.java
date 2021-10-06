package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.backgroundTask.PagedTask;
import edu.byu.cs.tweeter.client.backgroundTask.handler.TaskExecutor;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusService {
  private static final int PAGE_SIZE = 10;

  public interface GetStatusObserver {
    void getStatusSucceeded(List<Status> statuses, boolean hasMorePages);

    void getStatusFailed(String message);

    void getStatusThrewException(Exception ex);

    void setLastStatus(Status lastStatus, boolean hasMorePages);
  }

  public void getStatus(AuthToken authToken, User user, Status lastStatus, GetStatusObserver observer) {
    GetStoryTask getStoryTask = new GetStoryTask(authToken,
            user, PAGE_SIZE, lastStatus, new GetStoryHandler(observer));
    new TaskExecutor<>(getStoryTask);
  }

  private static class GetStoryHandler extends Handler {
    GetStatusObserver observer;

    GetStoryHandler(GetStatusObserver observer) {
      this.observer = observer;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
      boolean success = msg.getData().getBoolean(GetStoryTask.SUCCESS_KEY);
      if (success) {
        List<Status> statuses = (List<Status>) msg.getData().getSerializable(PagedTask.ITEMS_KEY);
        boolean hasMorePages = msg.getData().getBoolean(GetStoryTask.MORE_PAGES_KEY);

        Status lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
        observer.setLastStatus(lastStatus, hasMorePages);
        observer.getStatusSucceeded(statuses, hasMorePages);
      } else if (msg.getData().containsKey(GetStoryTask.MESSAGE_KEY)) {
        String message = msg.getData().getString(GetStoryTask.MESSAGE_KEY);
        observer.getStatusFailed(message);
      } else if (msg.getData().containsKey(GetStoryTask.EXCEPTION_KEY)) {
        Exception ex = (Exception) msg.getData().getSerializable(GetStoryTask.EXCEPTION_KEY);
        observer.getStatusThrewException(ex);
      }
    }
  }
}
