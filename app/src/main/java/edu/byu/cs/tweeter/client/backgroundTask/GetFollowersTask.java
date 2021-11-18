package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;
import android.util.Log;

import java.util.List;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowerPageRequest;
import edu.byu.cs.tweeter.model.net.response.FollowerPageResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of followers.
 */
public class GetFollowersTask extends PagedUserTask {
  private static final String URL_PATH = "/getfollowers";

  public GetFollowersTask(AuthToken authToken, User targetUser, int limit, User lastFollower,
                          Handler messageHandler) {
    super(authToken, targetUser, limit, lastFollower, messageHandler);
  }

  @Override
  protected final Pair<List<User>, Boolean> getItems(User targetUser, int limit, User lastItem) {
    try {
      FollowerPageRequest request;
      if (lastItem != null) {
        request = new FollowerPageRequest(targetUser.getAlias(), limit, lastItem.getAlias());

      } else {
        request = new FollowerPageRequest(targetUser.getAlias(), limit, null);

      }
      FollowerPageResponse response = ServerFacade.getServerFacade().getFollowerPage(request, URL_PATH);

      if (response.isSuccess()) {
        return new Pair<List<User>, Boolean>(response.getFollows(), response.getHasMorePages());

      } else {
        sendFailedMessage(response.getMessage());
      }
    } catch (Exception e) {
      Log.e("PagedUserTask", e.getMessage(), e);
      sendExceptionMessage(e);
    }
    return null;
  }
}
