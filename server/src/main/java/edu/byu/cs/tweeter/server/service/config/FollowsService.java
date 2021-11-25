package edu.byu.cs.tweeter.server.service.config;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.model.DeleteItemResult;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.parents.CountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowUserRequest;
import edu.byu.cs.tweeter.model.net.request.FollowerCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowerPageRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingPageRequest;
import edu.byu.cs.tweeter.model.net.request.parents.FollowsPageRequest;
import edu.byu.cs.tweeter.model.net.request.UnfollowUserRequest;
import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.FollowUserResponse;
import edu.byu.cs.tweeter.model.net.response.FollowerPageResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingPageResponse;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.model.net.response.UnfollowUserResponse;
import edu.byu.cs.tweeter.server.factories.abstracts.FollowsAbstractFactory;
import edu.byu.cs.tweeter.server.service.ServiceHelper;
import edu.byu.cs.tweeter.server.service.config.FollowsServiceHelper;

public class FollowsService {
  FollowsAbstractFactory followDAO = ServiceHelper.followDAO;
  FollowerHelper followerHelper;
  FollowingHelper followingHelper;

  public FollowsService() {
    followerHelper = new FollowerHelper();
    followingHelper = new FollowingHelper();
  }

  public FollowerPageResponse getFollowersPage(FollowerPageRequest request) {
    return (FollowerPageResponse) followerHelper.runRequest(request);
  }

  public FollowingPageResponse getFollowingPage(FollowingPageRequest request) {
    return (FollowingPageResponse) followingHelper.runRequest(request);
  }


  class FollowerHelper extends FollowsServiceHelper {
    public FollowerHelper() {
      super();
    }

    @Override
    public ItemCollection<QueryOutcome> getQueryOutcome(FollowsPageRequest request) {
      return followDAO.getFollowers((FollowerPageRequest) request);
    }

    @Override
    public Item findUser(Item item) {
      return userDAO.getUser(new UserRequest(item.getString("follower_handle")));
    }

    @Override
    public void updateCount(CountRequest request) {
      userDAO.updateFollowerCount(new FollowerCountRequest(request.getUser(), request.getCount()));
    }

    @Override
    public Response returnResponse(List<User> responseFollows, boolean hasMorePages) {
      return new FollowerPageResponse(responseFollows, hasMorePages);
    }
  }

  class FollowingHelper extends FollowsServiceHelper {
    public FollowingHelper() {
      super();
    }

    @Override
    public ItemCollection<QueryOutcome> getQueryOutcome(FollowsPageRequest request) {
      return followDAO.getFollowing((FollowingPageRequest) request);
    }

    @Override
    public Item findUser(Item item) {
      return userDAO.getUser(new UserRequest(item.getString("followee_handle")));
    }

    @Override
    public void updateCount(CountRequest request) {
      userDAO.updateFollowingCount(new FollowingCountRequest(request.getUser(), request.getCount()));
    }

    @Override
    public Response returnResponse(List<User> responseFollows, boolean hasMorePages) {
      return new FollowingPageResponse(responseFollows, hasMorePages);
    }
  }

//  public FollowerPageResponse getFollowersPage(FollowerPageRequest request) {
//
//    assert request.getLimit() > 0;
//    assert request.getFollowerAlias() != null;
//
//    List<User> allFollowees = new ArrayList<>();
//    List<User> responseFollowees = new ArrayList<>(request.getLimit());
//    Integer followerCount;
//
//    ItemCollection<QueryOutcome> items = followDAO.query(request);
//    Iterator<Item> iterator = items.iterator();
//
//    while (iterator.hasNext()) {
//      Item item = iterator.next();
//
//      Item findUser = userDAO.getUser(new UserRequest(item.getString("follower_handle")));
//
////      String firstName = findUser.getString("first_name");
////      String lastName = findUser.getString("last_name");
////      String alias = findUser.getString("user_handle");
////      String imageURL = findUser.getString("image_url");
////
////      User tempUser = new User(firstName, lastName, alias, imageURL);
//      User tempUser = buildUser(findUser);
//
//      allFollowees.add(tempUser);
//    }
//
//    followerCount = allFollowees.size();
//    Item requestUserItem = userDAO.getUser(new UserRequest(request.getFollowerAlias()));
//
////    String firstName = requestUserItem.getString("first_name");
////    String lastName = requestUserItem.getString("last_name");
////    String alias = requestUserItem.getString("user_handle");
////    String imageURL = requestUserItem.getString("image_url");
////
////    User tempRequestUser = new User(firstName, lastName, alias, imageURL);
//    User tempRequestUser = buildUser(requestUserItem);
//
//    userDAO.updateFollowerCount(new FollowerCountRequest(tempRequestUser, followerCount));
//
//    boolean hasMorePages = false;
//
//    if (request.getLimit() > 0) {
//      if (allFollowees != null) {
//        int followeesIndex = getFolloweesStartingIndex(request.getLastFollowsAlias(), allFollowees);
//
//        for (int limitCounter = 0; followeesIndex < allFollowees.size() && limitCounter < request.getLimit(); followeesIndex++, limitCounter++) {
//          responseFollowees.add(allFollowees.get(followeesIndex));
//        }
//
//        hasMorePages = followeesIndex < allFollowees.size();
//      }
//    }
//    return new FollowerPageResponse(responseFollowees, hasMorePages);
//  }

//  public FollowingPageResponse getFollowingPage(FollowingPageRequest request) {
//
//    assert request.getLimit() > 0;
//    assert request.getFollowerAlias() != null;
//
//    List<User> allFollowees = new ArrayList<>();
//    List<User> responseFollowees = new ArrayList<>(request.getLimit());
//    Integer followingCount;
//
//    ItemCollection<QueryOutcome> items = followDAO.query(request);
//
//    Iterator<Item> iterator = items.iterator();
//    while (iterator.hasNext()) {
//      Item item = iterator.next();
//
//      Item findUser = userDAO.getUser(new UserRequest(item.getString("followee_handle")));
//
////      String firstName = findUser.getString("first_name");
////      String lastName = findUser.getString("last_name");
////      String alias = findUser.getString("user_handle");
////      String imageURL = findUser.getString("image_url");
////
////      User tempUser = new User(firstName, lastName, alias, imageURL);
//      User tempUser = buildUser(findUser);
//
//      allFollowees.add(tempUser);
//    }
//
//    followingCount = allFollowees.size();
//    Item requestUserItem = userDAO.getUser(new UserRequest(request.getFollowerAlias()));
//
////    String firstName = requestUserItem.getString("first_name");
////    String lastName = requestUserItem.getString("last_name");
////    String alias = requestUserItem.getString("user_handle");
////    String imageURL = requestUserItem.getString("image_url");
////
////    User tempRequestUser = new User(firstName, lastName, alias, imageURL);
//    User tempRequestUser = buildUser(requestUserItem);
//
//    userDAO.updateFollowingCount(new FollowingCountRequest(tempRequestUser, followingCount));
//
//    boolean hasMorePages = false;
//
//    if (request.getLimit() > 0) {
//      if (allFollowees != null) {
//        int followeesIndex = getFolloweesStartingIndex(request.getLastFollowsAlias(), allFollowees);
//
//        for (int limitCounter = 0; followeesIndex < allFollowees.size() && limitCounter < request.getLimit(); followeesIndex++, limitCounter++) {
//          responseFollowees.add(allFollowees.get(followeesIndex));
//        }
//
//        hasMorePages = followeesIndex < allFollowees.size();
//      }
//    }
//
//    return new FollowingPageResponse(responseFollowees, hasMorePages);
//  }

  public FollowUserResponse followUser(FollowUserRequest request) {

    try {
      followDAO.follow(request);
      return new FollowUserResponse(true);
    } catch (Exception e) {
      return new FollowUserResponse(false);
    }
  }

  public UnfollowUserResponse unfollowUser(UnfollowUserRequest request) {

    DeleteItemResult result = followDAO.unfollow(request);

    if (result.getAttributes() == null) {
      return new UnfollowUserResponse(false);
    } else {
      return new UnfollowUserResponse(true);
    }
  }


//  User buildUser(Item item) {
//    String userHandle = item.getString("user_handle");
//    String firstName = item.getString("first_name");
//    String lastName = item.getString("last_name");
//    String imageURL = item.getString("image_url");
//    return new User(firstName, lastName, userHandle, imageURL);
//  }

}
