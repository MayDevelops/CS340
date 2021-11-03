package edu.byu.cs.tweeter.server.dao;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.server.util.FakeData;
import edu.byu.cs.tweeter.server.util.Pair;

/**
 * A DAO for accessing 'following' data from the database.
 */
public class FeedDAO {

  /**
   * Gets the count of users from the database that the user specified is following. The
   * current implementation uses generated data and doesn't actually access a database.
   *
   * @param follower the User whose count of how many following is desired.
   * @return said count.
   */
  public Integer getFeedCount(User follower) {
    // TODO: uses the dummy data.  Replace with a real implementation.
    assert follower != null;
    return getDummyFollowees().size();
  }

  /**
   * Gets the users from the database that the user specified in the request is following. Uses
   * information in the request object to limit the number of followees returned and to return the
   * next set of followees after any that were returned in a previous request. The current
   * implementation returns generated data and doesn't actually access a database.
   *
   * @param request contains information about the user whose followees are to be returned and any
   *                other information required to satisfy the request.
   * @return the followees.
   */
  public FeedResponse getFeed(FeedRequest request) {
    // TODO: Generates dummy data. Replace with a real implementation.
    assert request.getLimit() > 0;
    assert request.getUser() != null;

    Pair<List<Status>, Boolean> feed = getFakeData().getPageOfStatus(request.getLastStatus(), request.getLimit());
    List<Status> allStatuses = feed.getFirst();
    boolean hasMorePages = feed.getSecond();

    List<Status> responseFeed = new ArrayList<>(request.getLimit());

    if (request.getLimit() > 0) {
      if (allStatuses != null) {
        int feedIndex = getFeedStartingIndex(request.getLastStatus(), allStatuses);

        for (int limitCounter = 0; feedIndex < allStatuses.size() && limitCounter < request.getLimit(); feedIndex++, limitCounter++) {
          responseFeed.add(allStatuses.get(feedIndex));
        }
      }
    }

    return new FeedResponse(responseFeed, hasMorePages);
  }

  /**
   * Determines the index for the first followee in the specified 'allFollowees' list that should
   * be returned in the current request. This will be the index of the next followee after the
   * specified 'lastFollowee'.
   *
   * @param lastStatus the alias of the last followee that was returned in the previous
   *                   request or null if there was no previous request.
   * @param allFeed    the generated list of followees from which we are returning paged results.
   * @return the index of the first followee to be returned.
   */
  private int getFeedStartingIndex(Status lastStatus, List<Status> allFeed) {

    int feedIndex = 0;

    if (lastStatus != null) {
      // This is a paged request for something after the first page. Find the first item
      // we should return
      for (int i = 0; i < allFeed.size(); i++) {
        if (lastStatus.equals(allFeed.get(i).getUser())) {
          // We found the index of the last item returned last time. Increment to get
          // to the first one we should return
          feedIndex = i + 1;
          break;
        }
      }
    }

    return feedIndex;
  }

  /**
   * Returns the list of dummy followee data. This is written as a separate method to allow
   * mocking of the followees.
   *
   * @return the followees.
   */
  List<User> getDummyFollowees() {
    return getFakeData().getFakeUsers();
  }

  /**
   * Returns the {@link FakeData} object used to generate dummy followees.
   * This is written as a separate method to allow mocking of the {@link FakeData}.
   *
   * @return a {@link FakeData} instance.
   */
  FakeData getFakeData() {
    return new FakeData();
  }
}
