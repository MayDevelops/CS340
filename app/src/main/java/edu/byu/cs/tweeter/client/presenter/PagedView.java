package edu.byu.cs.tweeter.client.presenter;

import java.net.MalformedURLException;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;

public interface PagedView<T>{
  void navigateToUser(User user);

  void setLoading(boolean value) throws MalformedURLException;

  void displayToast(String message);

  void addItems(List<T> list);
}
