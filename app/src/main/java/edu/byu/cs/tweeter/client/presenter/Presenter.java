package edu.byu.cs.tweeter.client.presenter;

import java.net.MalformedURLException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class Presenter {

  public abstract void loadMoreItems(AuthToken authToken, User user) throws MalformedURLException;
  public abstract void getUser(AuthToken authToken, String alias);

}
