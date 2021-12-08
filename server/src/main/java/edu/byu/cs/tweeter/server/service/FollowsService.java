package edu.byu.cs.tweeter.server.service;

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
import edu.byu.cs.tweeter.server.service.config.FollowsServiceHelper;
import edu.byu.cs.tweeter.server.service.config.ServiceHelper;
import edu.byu.cs.tweeter.server.util.Pair;

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
    public Pair<ItemCollection<QueryOutcome>, Boolean> getQueryOutcome(FollowsPageRequest request) {
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
    public Pair<ItemCollection<QueryOutcome>, Boolean> getQueryOutcome(FollowsPageRequest request) {
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

}
