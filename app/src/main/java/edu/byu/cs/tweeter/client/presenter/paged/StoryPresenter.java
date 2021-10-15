package edu.byu.cs.tweeter.client.presenter.paged;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.StoryService;
import edu.byu.cs.tweeter.client.state.State;
import edu.byu.cs.tweeter.model.domain.Status;

public class StoryPresenter extends PagedPresenter<Status> {

  public StoryPresenter(StoryView view) {
    super(view);
  }

  public interface StoryView extends PagedView<Status> {
  }

  @Override
  void ServiceLoader() {
    new StoryService().getStory(State.authToken, State.user, new StoryService.GetStoryObserver() {
      @Override
      public void getStorySucceeded(List<Status> statuses) {
        view.addItems(statuses);
      }

      @Override
      public void handleFailure(String message) {
        view.displayToast(message);
      }
    });
  }
}
