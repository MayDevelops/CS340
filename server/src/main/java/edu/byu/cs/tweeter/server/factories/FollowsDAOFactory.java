package edu.byu.cs.tweeter.server.factories;


import edu.byu.cs.tweeter.model.net.request.FollowerPageRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingPageRequest;
import edu.byu.cs.tweeter.model.net.response.FollowerPageResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingPageResponse;
import edu.byu.cs.tweeter.server.dao.FollowsDAOProvider;

public class FollowsDAOFactory extends FollowsAbstractFactory {

  @Override
  public FollowingPageResponse query(FollowingPageRequest request) {
    return new FollowsDAOProvider().getFollowing(request);
  }

  @Override
  public FollowerPageResponse query(FollowerPageRequest request) {
    return new FollowsDAOProvider().getFollowers(request);
  }
}
