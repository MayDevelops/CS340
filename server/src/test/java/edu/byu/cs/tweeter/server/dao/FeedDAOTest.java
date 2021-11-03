package edu.byu.cs.tweeter.server.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;

class FeedDAOTest {
  private final AuthToken testUserAuthToken = new AuthToken();

  private final User user1 = new User("Daffy", "Duck", "");
  private final User user2 = new User("Fred", "Flintstone", "");
  private final User user3 = new User("Barney", "Rubble", "");
  private final User user4 = new User("Wilma", "Rubble", "");
  private final User user5 = new User("Clint", "Eastwood", "");
  private final User user6 = new User("Mother", "Teresa", "");
  private final User user7 = new User("Harriett", "Hansen", "");
  private final User user8 = new User("Zoe", "Zabriski", "");
  List<String> stringList = Arrays.asList("AAA", "BBB", "CCC");
  Status status1 = new Status("A", user1, "Date A", stringList, stringList);
  Status status2 = new Status("B", user2, "Date B", stringList, stringList);
  Status status3 = new Status("C", user3, "Date C", stringList, stringList);
  Status status4 = new Status("D", user4, "Date D", stringList, stringList);
  Status status5 = new Status("E", user5, "Date E", stringList, stringList);
  Status status6 = new Status("F", user6, "Date F", stringList, stringList);
  Status status7 = new Status("G", user6, "Date G", stringList, stringList);
  Status status8 = new Status("H", user7, "Date H", stringList, stringList);

  private FeedDAO feedDAO;
  FeedRequest feedRequest;
  FeedResponse feedResponse;

  @BeforeEach
  void setup() {
    feedDAO = Mockito.mock(FeedDAO.class);
    feedRequest = new FeedRequest(testUserAuthToken, user1, 10, null);
    feedResponse = feedDAO.getFeed(feedRequest);
  }

  @Test
  void testGetFollowees_noFolloweesForUser() {
    List<Status> statuses = Collections.emptyList();
    FeedResponse response = new FeedResponse(statuses, false);

    Mockito.when(feedDAO.getFeed(Mockito.any())).thenReturn(response);

    Assertions.assertEquals(0, response.getStatuses().size());
    Assertions.assertFalse(response.getHasMorePages());
  }

  @Test
  void testGetFollowees_oneFollowerForUser_limitGreaterThanUsers() {
    List<Status> feed = Collections.singletonList(status1);
    feedResponse = new FeedResponse(feed, false);

    Mockito.when(feedDAO.getFeed(Mockito.any())).thenReturn(feedResponse);

    FeedRequest request = new FeedRequest(testUserAuthToken, user1, 10, null);
    FeedResponse response = feedDAO.getFeed(request);

    Assertions.assertEquals(1, response.getStatuses().size());
    Assertions.assertTrue(response.getStatuses().contains(status1));
    Assertions.assertFalse(response.getHasMorePages());
  }

  @Test
  void testGetFollowees_twoFollowersForUser_limitEqualsUsers() {
    List<Status> feed = Arrays.asList(status1, status2);
    feedResponse = new FeedResponse(feed, false);

    Mockito.when(feedDAO.getFeed(Mockito.any())).thenReturn(feedResponse);

    FeedRequest request = new FeedRequest(testUserAuthToken, user1, 2, null);
    FeedResponse response = feedDAO.getFeed(request);

    Assertions.assertEquals(2, response.getStatuses().size());
    Assertions.assertTrue(response.getStatuses().contains(status1));
    Assertions.assertTrue(response.getStatuses().contains(status2));
    Assertions.assertFalse(response.getHasMorePages());
  }

  @Test
  void testGetFollowees_limitLessThanUsers_endsOnPageBoundary() {
    List<Status> feed = Arrays.asList(status2, status3, status4, status5, status6, status7);
    feedResponse = new FeedResponse(feed, false);

    Mockito.when(feedDAO.getFeed(Mockito.any())).thenReturn(feedResponse);

    FeedRequest request = new FeedRequest(testUserAuthToken, user1, 2, null);
    FeedResponse response = feedDAO.getFeed(request);

    // Verify first page
    Assertions.assertEquals(2, response.getStatuses().size());
    Assertions.assertTrue(response.getStatuses().contains(status2));
    Assertions.assertTrue(response.getStatuses().contains(status3));
    Assertions.assertTrue(response.getHasMorePages());

    // Get and verify second page
    request = new FeedRequest(testUserAuthToken, user5, 2, response.getStatuses().get(1));
    response = feedDAO.getFeed(request);

    Assertions.assertEquals(2, response.getStatuses().size());
    Assertions.assertTrue(response.getStatuses().contains(status4));
    Assertions.assertTrue(response.getStatuses().contains(status5));
    Assertions.assertTrue(response.getHasMorePages());

    // Get and verify third page
    request = new FeedRequest(testUserAuthToken, user5, 2, response.getStatuses().get(1));
    response = feedDAO.getFeed(request);

    Assertions.assertEquals(2, response.getStatuses().size());
    Assertions.assertTrue(response.getStatuses().contains(status6));
    Assertions.assertTrue(response.getStatuses().contains(status7));
    Assertions.assertFalse(response.getHasMorePages());
  }


  @Test
  void testGetFollowees_limitLessThanUsers_notEndsOnPageBoundary() {
    List<User> followees = Arrays.asList(user2, user3, user4, user5, user6, user7, user8);
    Mockito.when(feedDAO.getDummyFollowees()).thenReturn(followees);

    FeedRequest request = new FeedRequest(testUserAuthToken, user6, 2, null);
    FeedResponse response = feedDAO.getFeed(request);

    // Verify first page
    Assertions.assertEquals(2, response.getStatuses().size());
    Assertions.assertTrue(response.getStatuses().contains(user2));
    Assertions.assertTrue(response.getStatuses().contains(user3));
    Assertions.assertTrue(response.getHasMorePages());

    // Get and verify second page
    request = new FeedRequest(testUserAuthToken, user6, 2, response.getStatuses().get(1));
    response = feedDAO.getFeed(request);

    Assertions.assertEquals(2, response.getStatuses().size());
    Assertions.assertTrue(response.getStatuses().contains(user4));
    Assertions.assertTrue(response.getStatuses().contains(user5));
    Assertions.assertTrue(response.getHasMorePages());

    // Get and verify third page
    request = new FeedRequest(testUserAuthToken, user6, 2, response.getStatuses().get(1));
    response = feedDAO.getFeed(request);

    Assertions.assertEquals(2, response.getStatuses().size());
    Assertions.assertTrue(response.getStatuses().contains(user6));
    Assertions.assertTrue(response.getStatuses().contains(user7));
    Assertions.assertTrue(response.getHasMorePages());

    // Get and verify fourth page
    request = new FeedRequest(testUserAuthToken, user6, 2, response.getStatuses().get(1));
    response = feedDAO.getFeed(request);

    Assertions.assertEquals(1, response.getStatuses().size());
    Assertions.assertTrue(response.getStatuses().contains(status8));
    Assertions.assertFalse(response.getHasMorePages());
  }
}
