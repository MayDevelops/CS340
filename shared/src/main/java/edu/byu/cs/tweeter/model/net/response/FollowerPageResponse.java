package edu.byu.cs.tweeter.model.net.response;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.response.parents.FollowPageResponse;

public class FollowerPageResponse extends FollowPageResponse {
  public FollowerPageResponse(String message) {
    super(message);
  }

  public FollowerPageResponse(List<User> follows, boolean hasMorePages) {
    super(follows, hasMorePages);
  }
}
