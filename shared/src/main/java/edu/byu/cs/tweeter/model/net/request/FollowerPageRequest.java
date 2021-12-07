package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.request.parents.FollowsPageRequest;

public class FollowerPageRequest extends FollowsPageRequest {

  private FollowerPageRequest() {
    super();
  }

  public FollowerPageRequest(String followerAlias) {
    super(followerAlias);
  }

  public FollowerPageRequest(AuthToken authToken, String followerAlias, int limit, String lastFollowingAlias) {
    super(authToken, followerAlias, limit, lastFollowingAlias);
  }

  public FollowerPageRequest(String followerAlias, int limit, String lastFollowingAlias) {
    super(followerAlias, limit, lastFollowingAlias);
  }
}
