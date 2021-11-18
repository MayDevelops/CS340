package edu.byu.cs.tweeter.model.net.response;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;

public class FollowerPageResponse extends FollowPageResponse {
  public FollowerPageResponse(String message) {
    super(message);
  }

  public FollowerPageResponse(List<User> follows, boolean hasMorePages) {
    super(follows, hasMorePages);
  }
}
