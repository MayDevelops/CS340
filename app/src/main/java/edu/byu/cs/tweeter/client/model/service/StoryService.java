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
import edu.byu.cs.tweeter.model.net.request.StoryRequest;

public class StoryService {

  public void getStoryTask(StoryRequest storyRequest, StoryObserver observer) {
    GetStoryTask getStoryTask = new GetStoryTask(storyRequest, new GetStoryHandler(observer));
    new TaskExecutor<>(getStoryTask);
  }


  public interface StoryObserver extends ServiceObserver {
    void storySucceeded(List<Status> statuses, boolean pages);
  }

  private static class GetStoryHandler extends BackgroundTaskHandler {

    public GetStoryHandler(StoryObserver observer) {
      super(observer);
    }

    @Override
    protected String getFailedMessagePrefix() {
      return "Story Service";
    }

    @Override
    protected void handleSuccessMessage(ServiceObserver observer, Bundle data) {
      List<Status> statuses = (List<Status>) data.getSerializable(PagedTask.ITEMS_KEY);
      boolean hasMorePages = data.getBoolean(GetStoryTask.MORE_PAGES_KEY);

      ((StoryObserver) observer).storySucceeded(statuses, hasMorePages);
    }
  }
}
