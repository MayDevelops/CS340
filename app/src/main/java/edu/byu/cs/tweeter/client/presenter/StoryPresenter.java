package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter implements UserService.GetUserObserver, StatusService.GetStatusObserver {
  private final View view;
  private final User user;
  private final AuthToken authToken;
  private boolean hasMorePages = true;
  private boolean isLoading = false;


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
      new StatusService().getStatus(authToken, user, this);
      isLoading = false;
      view.setLoading(false);
    }
  }

  @Override
  public void getUserSucceeded(User user) {
    view.navigateToUser(user);
  }

  @Override
  public void getStatusSucceeded(List<Status> statuses) {
    view.addItems(statuses);
  }

  @Override
  public void handleFailure(String message) {
    view.displayToast(message);
  }

  public interface View {
    void navigateToUser(User user);

    void setLoading(boolean value);

    void displayToast(String message);

    void addItems(List<Status> statuses);

  }
}
