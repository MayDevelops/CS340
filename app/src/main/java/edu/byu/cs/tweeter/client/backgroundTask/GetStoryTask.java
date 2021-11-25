package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;
import android.util.Log;

import java.util.List;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of statuses from a user's story.
 */
public class GetStoryTask extends PagedStatusTask {
  private static final String URL_PATH = "/story";

  public GetStoryTask(StoryRequest storyRequest,
                      Handler messageHandler) {
    super(storyRequest.getAuthToken(), storyRequest.getUser(), 10, storyRequest.getLastStatus(), messageHandler);
  }

  @Override
  protected final Pair<List<Status>, Boolean> getItems(User targetUser, int limit, Status lastItem) {
    try {
      StoryRequest request = new StoryRequest(targetUser, limit, lastItem);
      StoryResponse response = ServerFacade.getServerFacade().getStory(request, URL_PATH);

      if (response.isSuccess()) {
        return new Pair<List<Status>, Boolean>(response.getStatuses(), response.getHasMorePages());

      } else {
        sendFailedMessage(response.getMessage());
      }
    } catch (Exception e) {
      Log.e("GetStoryTask", e.getMessage(), e);
      sendExceptionMessage(e);
    }
    return null;
  }
}
