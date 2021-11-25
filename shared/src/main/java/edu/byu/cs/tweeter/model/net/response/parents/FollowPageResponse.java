package edu.byu.cs.tweeter.model.net.response.parents;

import java.util.List;
import java.util.Objects;

import edu.byu.cs.tweeter.model.domain.User;

public class FollowPageResponse extends PagedResponse {

  private List<User> follows;

  public FollowPageResponse(String message) {
    super(false, message, false);
  }

  public FollowPageResponse(List<User> follows, boolean hasMorePages) {
    super(true, hasMorePages);
    this.follows = follows;
  }

  public List<User> getFollows() {
    return follows;
  }

  @Override
  public boolean equals(Object param) {
    if (this == param) {
      return true;
    }

    if (param == null || getClass() != param.getClass()) {
      return false;
    }

    FollowPageResponse that = (FollowPageResponse) param;

    return (Objects.equals(follows, that.follows) &&
            Objects.equals(this.getMessage(), that.getMessage()) &&
            this.isSuccess() == that.isSuccess());
  }

  @Override
  public int hashCode() {
    return Objects.hash(follows);
  }
}
