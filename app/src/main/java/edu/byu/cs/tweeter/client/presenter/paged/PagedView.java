package edu.byu.cs.tweeter.client.presenter.paged;


import java.util.List;

import edu.byu.cs.tweeter.client.presenter.View;
import edu.byu.cs.tweeter.model.domain.User;

public interface PagedView<T> extends View<T> {
  void setFooterAndLoading(boolean b);

  void addItems(List<T> list);

  void navigateToUser(User user);

  void setPages(boolean pages);

}