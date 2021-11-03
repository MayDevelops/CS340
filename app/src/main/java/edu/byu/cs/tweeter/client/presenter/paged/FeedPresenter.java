package edu.byu.cs.tweeter.client.presenter.paged;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FeedService;
import edu.byu.cs.tweeter.client.state.State;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;

public class FeedPresenter extends PagedPresenter<Status> {

  public FeedPresenter(FeedView view) {
    super(view);
  }

  public interface FeedView extends PagedView<Status> {
  }

  @Override
  void ServiceLoader() {
    FeedService service = new FeedService();
    FeedRequest feedRequest = new FeedRequest(State.authToken, State.user, 10, ((Status) lastItem));

    service.getFeedTask(feedRequest, new FeedService.FeedObserver() {

      @Override
      public void feedSucceeded(List<Status> statuses, boolean pages) {
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
