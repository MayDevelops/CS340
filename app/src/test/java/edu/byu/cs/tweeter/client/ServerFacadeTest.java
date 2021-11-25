//package edu.byu.cs.tweeter.client;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertNull;
//
//import org.junit.jupiter.api.Test;
//
//import java.io.IOException;
//
//import edu.byu.cs.tweeter.client.model.net.ServerFacade;
//import edu.byu.cs.tweeter.model.domain.AuthToken;
//import edu.byu.cs.tweeter.model.domain.User;
//import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
//import edu.byu.cs.tweeter.model.net.request.FollowCountRequest;
//import edu.byu.cs.tweeter.model.net.request.FollowPageRequest;
//import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
//import edu.byu.cs.tweeter.model.net.response.FollowCountResponse;
//import edu.byu.cs.tweeter.model.net.response.parents.FollowPageResponse;
//import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
//
//public class ServerFacadeTest {
//  private final AuthToken testUserAuthToken = new AuthToken();
//  private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
//  private static final String FEMALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png";
//
//  private static final User user1 = new User("Allen", "Anderson", "@allen", MALE_IMAGE_URL);
//  private static final User user2 = new User("Amy", "Ames", "@amy", FEMALE_IMAGE_URL);
//  private static final User user3 = new User("Bob", "Bobson", "@bob", MALE_IMAGE_URL);
//  private static final User user4 = new User("Bonnie", "Beatty", "@bonnie", FEMALE_IMAGE_URL);
//  private static final User user5 = new User("Chris", "Colston", "@chris", MALE_IMAGE_URL);
//  private static final User user6 = new User("Cindy", "Coats", "@cindy", FEMALE_IMAGE_URL);
//  private static final User user7 = new User("Dan", "Donaldson", "@dan", MALE_IMAGE_URL);
//  private static final User user8 = new User("Dee", "Dempsey", "@dee", FEMALE_IMAGE_URL);
//  private static final User user9 = new User("Elliott", "Enderson", "@elliott", MALE_IMAGE_URL);
//  private static final User user10 = new User("Elizabeth", "Engle", "@elizabeth", FEMALE_IMAGE_URL);
//
//
//  @Test
//  void testRegister() throws IOException, TweeterRemoteException {
//    RegisterRequest request = new RegisterRequest(user1.getFirstName(), user1.getLastName(), user1.getAlias(), "pw", null, user1.getImageUrl());
//    RegisterResponse response = ServerFacade.getServerFacade().register(request, "/register");
//
//
//    assertNotNull(response);
//    assertEquals(user1.getFirstName(), response.getUser().getFirstName());
//    assertEquals(user1.getLastName(), response.getUser().getLastName());
//    assertEquals(user1.getAlias(), response.getUser().getAlias());
//    assertNull(response.getUser().getImageBytes());
//    assertEquals(user1.getImageUrl(), response.getUser().getImageUrl());
//
//  }
//
//  @Test
//  void testGetFollowers() throws IOException, TweeterRemoteException {
//    int limit = 2;
//    FollowPageRequest request = new FollowPageRequest(testUserAuthToken, user1.getAlias(), limit, null);
//    FollowPageResponse response = ServerFacade.getServerFacade().getFollowerPage(request, "/followpage");
//
//    assertNotNull(response);
//    assertEquals(2, response.getFollows().size());
//    assertEquals(user1, response.getFollows().get(0));
//    assertEquals(user2, response.getFollows().get(1));
//
//    request = new FollowPageRequest(testUserAuthToken, user1.getAlias(), limit, user2.getAlias());
//    response = ServerFacade.getServerFacade().getFollowerPage(request, "/followpage");
//
//    assertNotNull(response);
//    assertEquals(2, response.getFollows().size());
//    assertEquals(user3, response.getFollows().get(0));
//    assertEquals(user4, response.getFollows().get(1));
//
//    request = new FollowPageRequest(testUserAuthToken, user1.getAlias(), limit, user4.getAlias());
//    response = ServerFacade.getServerFacade().getFollowerPage(request, "/followpage");
//
//    assertNotNull(response);
//    assertEquals(2, response.getFollows().size());
//    assertEquals(user5, response.getFollows().get(0));
//    assertEquals(user6, response.getFollows().get(1));
//
//    request = new FollowPageRequest(testUserAuthToken, user1.getAlias(), limit, user6.getAlias());
//    response = ServerFacade.getServerFacade().getFollowerPage(request, "/followpage");
//
//    assertNotNull(response);
//    assertEquals(2, response.getFollows().size());
//    assertEquals(user7, response.getFollows().get(0));
//    assertEquals(user8, response.getFollows().get(1));
//
//    request = new FollowPageRequest(testUserAuthToken, user1.getAlias(), limit, user8.getAlias());
//    response = ServerFacade.getServerFacade().getFollowerPage(request, "/followpage");
//
//    assertNotNull(response);
//    assertEquals(2, response.getFollows().size());
//    assertEquals(user9, response.getFollows().get(0));
//    assertEquals(user10, response.getFollows().get(1));
//
//  }
//
//  @Test
//  void testFollowerCount() throws IOException, TweeterRemoteException {
//    FollowCountRequest request = new FollowCountRequest(testUserAuthToken, user1);
//    FollowCountResponse response = ServerFacade.getServerFacade().getFollowCount(request, "count");
//
//    assertNotNull(response);
//    assertEquals(20, response.getFollowerCount());
//    assertEquals(20, response.getFollowingCount());
//  }
//}
