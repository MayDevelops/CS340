package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;
import android.util.Log;

import java.util.List;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of statuses from a user's feed.
 */
public class GetFeedTask extends PagedStatusTask {
  private static final String URL_PATH = "/feed";


  public GetFeedTask(FeedRequest feedRequest,
                     Handler messageHandler) {
    super(feedRequest.getAuthToken(), feedRequest.getUser(), 10, feedRequest.getLastStatus(), messageHandler);
  }

  @Override
  protected final Pair<List<Status>, Boolean> getItems(User targetUser, int limit, Status lastItem) {
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
