package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;
import android.util.Log;

import java.util.List;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowPageRequest;
import edu.byu.cs.tweeter.model.net.response.FollowPageResponse;
import edu.byu.cs.tweeter.util.Pair;

public abstract class PagedUserTask extends PagedTask<User> {
  private static final String URL_PATH = "/followpage";

  protected PagedUserTask(AuthToken authToken, User targetUser, int limit, User lastItem, Handler messageHandler) {
    super(authToken, targetUser, limit, lastItem, messageHandler);
  }

  @Override
  protected final List<User> getUsersForItems(List<User> items) {
    return items;
  }

  @Override
  protected final Pair<List<User>, Boolean> runSubTask(User targetUser, int limit, User lastItem) {
    try {
      FollowPageRequest request;
      if (lastItem != null) {
        request = new FollowPageRequest(targetUser.getAlias(), limit, lastItem.getAlias());

      } else {
        request = new FollowPageRequest(targetUser.getAlias(), limit, null);

      }
      FollowPageResponse response = ServerFacade.getServerFacade().followPage(request, URL_PATH);

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
