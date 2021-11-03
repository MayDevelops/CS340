package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of followers.
 */
public class GetFollowersTask extends PagedUserTask {

  public GetFollowersTask(AuthToken authToken, User targetUser, int limit, User lastFollower,
                          Handler messageHandler) {
    super(authToken, targetUser, limit, lastFollower, messageHandler);
  }

  @Override
  protected Pair<List<User>, Boolean> runSubTask(User targetUser, int limit, User lastItem) throws IOException {
    Pair<List<User>, Boolean> pageOfItems = getItems();
    List<User> items;
    boolean hasMorePages;


    items = pageOfItems.getFirst();
    hasMorePages = pageOfItems.getSecond();

//    for (User user : getUsersForItems(items)) {
//      BackgroundTaskUtils.loadImage(user);
//    }

    return new Pair<List<User>, Boolean>(items, hasMorePages);

  }

  @Override
  protected Pair<List<User>, Boolean> getItems() {
    return getFakeData().getPageOfUsers(getLastItem(), getLimit(), getTargetUser());
  }
}
