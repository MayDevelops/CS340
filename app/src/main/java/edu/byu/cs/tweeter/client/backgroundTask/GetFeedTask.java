package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of statuses from a user's feed.
 */
public class GetFeedTask extends PagedStatusTask {

  public GetFeedTask(FeedRequest feedRequest,
                     Handler messageHandler) {
    super(feedRequest.getAuthToken(), feedRequest.getUser(), 10, feedRequest.getLastStatus(), messageHandler);
  }

  @Override
  protected Pair<List<Status>, Boolean> getItems() {
    return getFakeData().getPageOfStatus(getLastItem(), getLimit());
  }
}
