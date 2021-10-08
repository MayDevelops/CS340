package edu.byu.cs.tweeter.client.presenter.paged;

import java.net.MalformedURLException;
import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FeedService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter implements FeedService.FeedObserver, UserService.GetUserObserver {
  private boolean hasMorePages = true;
  private boolean isLoading = false;

  private FeedView view;
  private User user;
  private AuthToken authToken;
  private Status lastStatus;


  public FeedPresenter(FeedView view, AuthToken authToken, User user) {
    this.view = view;
    this.authToken = authToken;
    this.user = user;
  }

  public void getUser(AuthToken authToken, String alias) {
    new UserService().getUser(authToken, alias, this);
  }

  public void loadMoreItems() throws MalformedURLException {
    if (!isLoading && hasMorePages) {
      isLoading = true;
      view.setLoading(true);
      new FeedService().getFeed(authToken, user, lastStatus, this);
      isLoading = false;
      view.setLoading(false);
    }
  }

  //FeedObserver Implementation
  @Override
  public void feedSucceeded(List<Status> statuses) {
    view.addItems(statuses);
  }

  @Override
  public void setLastStatus(List<Status> statuses, boolean hasMorePages) {
    this.hasMorePages = hasMorePages;
    lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
  }

  @Override
  public void getUserSucceeded(User user) {
    view.navigateToUser(user);
  }

  @Override
  public void handleFailure(String message) {
    view.displayToast(message);
  }

  //View Implementation
  public interface FeedView extends PagedView<Status> {
  }
}
