package edu.byu.cs.tweeter.client.presenter.paged;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.state.State;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter extends PagedPresenter<User> {
  public FollowersPresenter(FollowersView view) {
    super(view);
  }

  public interface FollowersView extends PagedView<User> {
  }

  @Override
  void ServiceLoader() {
    new FollowService().getFollowers(State.authToken, State.user, ((User) lastItem), new FollowService.GetFollowerObserver() {
      @Override
      public void getFollowerSucceeded(List<User> users) {
        view.addItems(users);
        lastItem = (users.size() > 0) ? users.get(users.size() - 1) : null;
      }

      @Override
      public void handleFailure(String message) {
        view.displayToast(message);
      }
    });
  }


}
