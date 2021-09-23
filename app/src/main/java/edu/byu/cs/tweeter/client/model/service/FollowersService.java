package edu.byu.cs.tweeter.client.model.service;

import java.util.List;

import edu.byu.cs.tweeter.client.presenter.FollowersPresenter;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersService {

  public interface FollowersObserver {

    void getFollowersSucceeded(List<User> users, boolean hasMorePages);

    void getFollowersFailed(String e);

    void getFollowersThrewException(Exception e);
  }
}
