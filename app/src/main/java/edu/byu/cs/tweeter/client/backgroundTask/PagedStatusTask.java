package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;
import android.util.Log;

import java.util.List;
import java.util.stream.Collectors;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.util.Pair;

public abstract class PagedStatusTask extends PagedTask<Status> {
  private static final String URL_PATH = "/feed";

  protected PagedStatusTask(AuthToken authToken, User targetUser, int limit, Status lastItem, Handler messageHandler) {
    super(authToken, targetUser, limit, lastItem, messageHandler);
  }

  @Override
  protected final List<User> getUsersForItems(List<Status> items) {
    return items.stream().map(x -> x.user).collect(Collectors.toList());
  }

  @Override
  protected final Pair<List<Status>, Boolean> runSubTask(User targetUser, int limit, Status lastItem) {
    try {
      FeedRequest request = new FeedRequest(targetUser, limit, lastItem);
      FeedResponse response = ServerFacade.getServerFacade().getFeed(request, URL_PATH);

      if (response.isSuccess()) {
        return new Pair<List<Status>, Boolean>(response.getStatuses(), response.getHasMorePages());

      } else {
        sendFailedMessage(response.getMessage());
      }
    } catch (Exception e) {
      Log.e("PagedStatusTask", e.getMessage(), e);
      sendExceptionMessage(e);
    }
    return null;
  }
}
