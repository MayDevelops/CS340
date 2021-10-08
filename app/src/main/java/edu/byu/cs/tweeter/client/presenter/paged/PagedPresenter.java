package edu.byu.cs.tweeter.client.presenter.paged;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.presenter.Presenter;
import edu.byu.cs.tweeter.client.presenter.View;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedPresenter<T> extends Presenter implements UserService.GetUserObserver {

  public PagedPresenter(View v) {
    super(v);
  }

  public void getUser(AuthToken authToken, String alias) {
    new UserService().getUser(authToken, alias, this);
  }

  @Override
  public void handleFailure(String message) {

  }

  @Override
  public void getUserSucceeded(User user) {

  }
}
