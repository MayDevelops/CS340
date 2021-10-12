package edu.byu.cs.tweeter.client.presenter.paged;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FeedService;
import edu.byu.cs.tweeter.client.state.State;
import edu.byu.cs.tweeter.model.domain.Status;

public class FeedPresenter extends PagedPresenter<Status> {
  public FeedPresenter(FeedView view) {
    super(view);
  }

  public interface FeedView extends PagedView<Status> {
  }

  @Override
  void ServiceLoader() {
    new FeedService().getFeed(State.authToken, State.user, ((Status) lastItem), new FeedService.FeedObserver() {

      @Override
      public void handleFailure(String message) {
        view.displayToast(message);
      }

      @Override
      public void feedSucceeded(List<Status> statuses) {
        view.addItems(statuses);
        lastItem = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
      }
    });
  }


}
