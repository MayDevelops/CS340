package edu.byu.cs.tweeter.client.presenter.paged;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FeedService;
import edu.byu.cs.tweeter.client.state.State;
import edu.byu.cs.tweeter.model.domain.Status;

public class FeedPresenter extends PagedPresenter<Status> {
  FeedView feedView;

  public FeedPresenter(FeedView view) {
    super(view);
    this.feedView = view;
  }

  public interface FeedView extends PagedView<Status> {
    void setPages(boolean pages);
  }

  @Override
  void ServiceLoader() {
    new FeedService().getFeedTask(State.authToken, State.user, ((Status) lastItem), new FeedService.FeedObserver() {


      @Override
      public void handleFailure(String message) {
        view.displayToast(message);
      }

      @Override
      public void feedSucceeded(List<Status> statuses, boolean pages) {
        view.setFooterAndLoading(false);
        feedView.setPages(pages);
        lastItem = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
        view.addItems(statuses);
      }
    });
  }


}
