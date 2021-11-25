package edu.byu.cs.tweeter.model.net.response;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.response.parents.FollowPageResponse;

public class FollowingPageResponse extends FollowPageResponse {
  public FollowingPageResponse(String message) {
    super(message);
  }

  public FollowingPageResponse(List<User> follows, boolean hasMorePages) {
    super(follows, hasMorePages);
  }
}
