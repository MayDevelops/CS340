package edu.byu.cs.tweeter.server.factories;

import edu.byu.cs.tweeter.model.net.request.FollowerPageRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingPageRequest;
import edu.byu.cs.tweeter.model.net.response.FollowerPageResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingPageResponse;

public abstract class FollowsAbstractFactory {
  public abstract FollowerPageResponse query(FollowerPageRequest request);

  public abstract FollowingPageResponse query(FollowingPageRequest request);

}
