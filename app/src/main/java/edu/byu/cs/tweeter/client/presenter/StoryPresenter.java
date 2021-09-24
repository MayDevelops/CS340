package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter implements UserService.GetUserObserver, StatusService.GetStatusObserver {
  private final View view;
  private User user;
  private AuthToken authToken;
  private boolean hasMorePages = true;
  private boolean isLoading = false;
  private Status lastStatus;


  public StoryPresenter(View view, AuthToken authToken, User user) {
    this.view = view;
    this.authToken = authToken;
    this.user = user;
  }

  public void getUser(AuthToken authToken, String alias) {
    new UserService().getUser(authToken, alias, this);
  }

  public void loadMoreItems() {
    if (!isLoading && hasMorePages) {
      isLoading = true;
      view.setLoading(true);
      new StatusService().getStatus(authToken, user, lastStatus, this);
      isLoading = false;
      view.setLoading(false);
    }
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

  //GetStatusObserver Implementation
  @Override
  public void getStatusSucceeded(List<Status> statuses, boolean hasMorePages) {
    view.addItems(statuses);
  }

  @Override
  public void getStatusFailed(String message) {
    String failedMessage = "Failed to get story: " + message;
    view.displayToast(failedMessage);
  }

  @Override
  public void getStatusThrewException(Exception ex) {
    String exceptionMessage = "Failed to get story because of exception: " + ex.getMessage();
    view.displayToast(exceptionMessage);
  }

  @Override
  public void setLastStatus(Status lastStatus, boolean hasMorePages) {
    this.hasMorePages = hasMorePages;
    this.lastStatus = lastStatus;
  }

  public interface View {
    void navigateToUser(User user);

    void setLoading(boolean value);

    void setHasMorePages(boolean value);

    void displayToast(String message);

    void addItems(List<Status> statuses);

  }
}
