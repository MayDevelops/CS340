package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.request.parents.FollowsPageRequest;

public class FollowingPageRequest extends FollowsPageRequest {

  private FollowingPageRequest() {
    super();
  }

  public FollowingPageRequest(AuthToken authToken, String followerAlias, int limit, String lastFollowingAlias) {
    super(authToken, followerAlias, limit, lastFollowingAlias);
  }

  public FollowingPageRequest(String followerAlias, int limit, String lastFollowingAlias) {
    super(followerAlias, limit, lastFollowingAlias);
  }

  public FollowingPageRequest(String followerAlias) {
    super(followerAlias);
  }
}
