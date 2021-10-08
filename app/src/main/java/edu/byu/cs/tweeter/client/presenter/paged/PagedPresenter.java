package edu.byu.cs.tweeter.client.presenter.paged;

import java.net.MalformedURLException;
import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FeedService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.presenter.Presenter;
import edu.byu.cs.tweeter.client.presenter.View;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedPresenter<T> extends Presenter {
  View view;
  public T lastItem;
  private boolean hasMorePages = true;
  boolean isLoading = false;

  public PagedPresenter(View v) {
    this.view = v;
  }

  @Override
  public void loadMoreItems(AuthToken authToken, User user) throws MalformedURLException {
    if (!isLoading && hasMorePages) {
      isLoading = true;
      view.setLoading(true);
      ServiceLoader();
      isLoading = false;
      view.setLoading(false);
    }
  }

  abstract void ServiceLoader();

  @Override
  public void getUser(AuthToken authToken, String alias) {
    new UserService().getUser(authToken, alias, new UserService.GetUserObserver() {

      @Override
      public void getUserSucceeded(User user) {
        new UserService().getUser(authToken, alias, this);
      }

      @Override
      public void handleFailure(String message) {
        view.displayToast(message);
      }
    });

  }
}
