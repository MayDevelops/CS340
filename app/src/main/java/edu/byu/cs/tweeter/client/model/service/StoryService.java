package edu.byu.cs.tweeter.client.model.service;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.backgroundTask.PagedTask;
import edu.byu.cs.tweeter.client.backgroundTask.handler.BackgroundTaskHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.TaskExecutor;
import edu.byu.cs.tweeter.client.backgroundTask.observer.ServiceObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryService {
  private static final int PAGE_SIZE = 10;
  private static Status lastStatus = null;

  public void getStory(AuthToken authToken, User user, ServiceObserver observer) {
    GetStoryTask getStoryTask = new GetStoryTask(authToken,
            user, PAGE_SIZE, lastStatus, new GetStoryHandler(observer));
    new TaskExecutor<>(getStoryTask);
  }


  public interface GetStoryObserver extends ServiceObserver {
    void getStorySucceeded(List<Status> statuses);
  }

  private static class GetStoryHandler extends BackgroundTaskHandler {

    public GetStoryHandler(ServiceObserver observer) {
      super(observer);
    }

    @Override
    protected String getFailedMessagePrefix() {
      return "Story Service";
    }

    @Override
    protected void handleSuccessMessage(ServiceObserver observer, Bundle data) {
      List<Status> statuses = (List<Status>) data.getSerializable(PagedTask.ITEMS_KEY);

      lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
      ((GetStoryObserver) observer).getStorySucceeded(statuses);
    }
  }
}
