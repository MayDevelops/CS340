package edu.byu.cs.tweeter.client.presenter.paged;


import java.net.MalformedURLException;
import java.util.List;

import edu.byu.cs.tweeter.client.presenter.View;
import edu.byu.cs.tweeter.model.domain.User;

public interface PagedView<T> extends View<T> {
  void setLoading(boolean b) throws MalformedURLException;

  void addItems(List<T> list);

  void navigateToUser(User user);
}