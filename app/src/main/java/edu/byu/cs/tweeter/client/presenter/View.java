package edu.byu.cs.tweeter.client.presenter;

import java.net.MalformedURLException;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;

public interface View<T> {

  void setLoading(boolean b) throws MalformedURLException;
  void displayToast(String message);
  void addItems(List<T> list);
  void navigateToUser(User user);

}
