package edu.byu.cs.tweeter.client.presenter.paged;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.state.State;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter extends PagedPresenter<User> {

  public FollowingPresenter(FollowingView view) {
    super(view);
  }

  public interface FollowingView extends PagedView<User> {
  }

  @Override
  void ServiceLoader() {
    new FollowService().getFollowing(State.authToken, State.user, ((User) lastItem), new FollowService.GetFollowingObserver() {

      @Override
      public void getFollowingSucceeded(List<User> users, boolean pages) {
        view.setFooterAndLoading(false);
        view.setPages(pages);
        lastItem = (users.size() > 0) ? users.get(users.size() - 1) : null;
        view.addItems(users);
      }

      @Override
      public void handleFailure(String message) {
        view.displayToast(message);
      }
    });
  }
}
