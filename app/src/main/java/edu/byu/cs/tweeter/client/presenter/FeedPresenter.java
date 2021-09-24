package edu.byu.cs.tweeter.client.presenter;

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

  private View view;
  private User user;
  private AuthToken authToken;
  private Status lastStatus;


  public FeedPresenter(View view, AuthToken authToken, User user) {
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
      view.setLoading(true, hasMorePages);
      new FeedService().getFeed(authToken, user, lastStatus, this);
      isLoading = false;
      view.setLoading(false, hasMorePages);
    }
  }

  //FeedObserver Implementation
  @Override
  public void feedSucceeded(List<Status> statuses) {
    view.addStatuses(statuses);
  }

  @Override
  public void feedFailed(String message) {
    String failMessage = "Failed to get feed: " + message;
    view.displayToast(failMessage);
  }

  @Override
  public void feedThrewException(Exception ex) {
    String exceptionMessage = "Failed to get feed because of exception: " + ex.getMessage();
    view.displayToast(exceptionMessage);
  }

  @Override
  public void setLastStatus(List<Status> statuses, boolean hasMorePages) {
    this.hasMorePages = hasMorePages;
    lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
  }

  //GetUserObserver Implementation
  @Override
  public void getUserSucceeded(User user) {
    view.navigateToUser(user);
  }

  @Override
  public void getUserFailed(String string) {

  }

  @Override
  public void getUserThrewException(Exception ex) {

  }

  //View Implementation
  public interface View {

    void navigateToUser(User user);

    void setLoading(boolean value, boolean pages) throws MalformedURLException;

    void displayToast(String message);

    void addStatuses(List<Status> statuses);

  }
}
