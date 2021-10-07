package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter implements FollowService.GetFollowingObserver, UserService.GetUserObserver {

  private static final int PAGE_SIZE = 10;
  private User lastFollowee;

  private boolean hasMorePages = true;
  private boolean isLoading = false;

  private View view;
  private User user;
  private AuthToken authToken;

  public FollowingPresenter(View view, AuthToken authToken, User targetUser) {
    this.view = view;
    this.authToken = authToken;
    this.user = targetUser;
  }

  @Override
  public void getFollowingSucceeded(List<User> users) {
    view.addItems(users);
  }

  @Override
  public void setLastFollowee(List<User> followees, boolean hasMorepages) {
    this.hasMorePages = hasMorepages;
    lastFollowee = (followees.size() > 0) ? followees.get(followees.size() - 1) : null;
  }

  @Override
  public void getUserSucceeded(User user) {
    view.navigateToUser(user);
  }

  @Override
  public void handleFailure(String message) {
    view.displayToast(message);
  }

  public void loadMoreItems() {
    if (!isLoading && hasMorePages) {
      isLoading = true;
      view.setLoading(true);
      new FollowService().getFollowing(authToken, user, lastFollowee, this);
      isLoading = false;
      view.setLoading(false);
    }
  }

  public void getUsers(String alias) {
    new UserService().getUser(authToken, alias, this);
  }

  public interface View {

    void addItems(List<User> followees);

    void setLoading(boolean value);

    void navigateToUser(User user);

    void displayToast(String message);

  }
}
