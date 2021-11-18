package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;
import android.util.Log;

import java.util.List;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowingPageRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingPageResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of other users being followed by a specified user.
 */
public class GetFollowingTask extends PagedUserTask {
  private static final String URL_PATH = "/getfollowing";

  public GetFollowingTask(AuthToken authToken, User targetUser, int limit, User lastFollowee,
                          Handler messageHandler) {
    super(authToken, targetUser, limit, lastFollowee, messageHandler);
  }

  @Override
  protected final Pair<List<User>, Boolean> getItems(User targetUser, int limit, User lastItem) {
    try {
      FollowingPageRequest request;
      if (lastItem != null) {
        request = new FollowingPageRequest(targetUser.getAlias(), limit, lastItem.getAlias());

      } else {
        request = new FollowingPageRequest(targetUser.getAlias(), limit, null);

      }
      FollowingPageResponse response = ServerFacade.getServerFacade().getFollowingPage(request, URL_PATH);

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

