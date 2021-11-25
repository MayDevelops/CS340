package edu.byu.cs.tweeter.client.presenter.paged;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.StoryService;
import edu.byu.cs.tweeter.client.state.State;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;

public class StoryPresenter extends PagedPresenter<Status> {

  public StoryPresenter(StoryView view) {
    super(view);
  }

  public interface StoryView extends PagedView<Status> {
  }

  @Override
  void ServiceLoader() {
    StoryService service = new StoryService();
    StoryRequest storyRequest = new StoryRequest(State.authToken, State.user, 10, ((Status) lastItem));

    service.getStoryTask(storyRequest, new StoryService.StoryObserver() {
      
      @Override
      public void storySucceeded(List<Status> statuses, boolean pages) {
        view.setFooterAndLoading(false);
        view.setPages(pages);
        lastItem = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
        view.addItems(statuses);
      }

      @Override
      public void handleFailure(String message) {
        view.displayToast(message);
      }
    });
  }
}
