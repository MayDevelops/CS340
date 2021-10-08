package edu.byu.cs.tweeter.client.model.service;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.backgroundTask.PagedTask;
import edu.byu.cs.tweeter.client.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.backgroundTask.handler.BackgroundTaskHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.TaskExecutor;
import edu.byu.cs.tweeter.client.backgroundTask.observer.ServiceObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService {

  private static final int PAGE_SIZE = 10;

  private User selectedUser;
  private AuthToken authToken;

  public void getFollowers(AuthToken authToken, User user, User lastFollower, GetFollowerObserver observer) {
    GetFollowersTask getFollowersTask = new GetFollowersTask(authToken,
            user, PAGE_SIZE, lastFollower, new GetFollowersHandler(observer));
    new TaskExecutor<>(getFollowersTask);
  }

  public void isFollower(AuthToken authToken, User user, User selectedUser, CheckFollowerObserver observer) {
    IsFollowerTask isFollowerTask = new IsFollowerTask(authToken,
            user, selectedUser, new IsFollowerHandler(observer));
    new TaskExecutor<>(isFollowerTask);
  }

  public void getFollowing(AuthToken authToken, User targetUser, User lastFollowee, GetFollowingObserver observer) {
    GetFollowingTask getFollowingTask = new GetFollowingTask(authToken,
            targetUser, PAGE_SIZE, lastFollowee, new GetFollowingHandler(observer));
    new TaskExecutor<>(getFollowingTask);
  }

  public void followTask(AuthToken authtoken, User selectedUser, GetFollowObserver observer,
                         GetFolloweeCountObserver followeeCountObserver,
                         GetFollowerCountObserver followerCountObserver) {
    this.authToken = authtoken;
    this.selectedUser = selectedUser;
    FollowTask followTask = new FollowTask(authtoken,
            selectedUser, new FollowHandler(observer, followeeCountObserver, followerCountObserver));
    new TaskExecutor<>(followTask);
  }

  public void unfollowTask(AuthToken authtoken, User selectedUser,
                           GetUnfollowObserver observer,
                           GetFolloweeCountObserver followeeCountObserver,
                           GetFollowerCountObserver followerCountObserver) {
    this.authToken = authtoken;
    this.selectedUser = selectedUser;
    UnfollowTask unfollowTask = new UnfollowTask(authtoken,
            selectedUser, new UnfollowHandler(observer, followeeCountObserver, followerCountObserver));
    new TaskExecutor<>(unfollowTask);
  }

  public void updateSelectedUserFollowingAndFollowers(GetFollowerCountObserver followerCountObserver, GetFolloweeCountObserver followeeCountObserver) {
    GetFollowersCountTask followersCountTask = new GetFollowersCountTask(authToken,
            selectedUser, new GetFollowersCountHandler(followerCountObserver));
    GetFollowingCountTask followingCountTask = new GetFollowingCountTask(authToken,
            selectedUser, new GetFollowingCountHandler(followeeCountObserver));
    new TaskExecutor<>(followersCountTask, followingCountTask);
  }

  public interface GetFollowingObserver extends ServiceObserver {
    void getFollowingSucceeded(List<User> users);

    void setLastFollowee(List<User> followees, boolean hasMorepages);
  }

  public interface GetFollowerObserver extends ServiceObserver {
    void getFollowerSucceeded(List<User> users);
  }

  public interface GetFollowObserver extends ServiceObserver {
    void getFollowSucceeded();

    void follow(AuthToken authToken, User user);
  }

  public interface GetUnfollowObserver extends ServiceObserver {
    void getUnfollowSucceeded();

    void unfollow(AuthToken authToken, User user);
  }

  public interface GetFollowerCountObserver extends ServiceObserver {
    void setFollowerCount(int count);
  }

  public interface GetFolloweeCountObserver extends ServiceObserver {
    void setFolloweeCount(int count);

  }

  public interface CheckFollowerObserver extends ServiceObserver {
    void checkFollowerSucceeded(boolean isFollower);
  }

  private static class IsFollowerHandler extends BackgroundTaskHandler {

    public IsFollowerHandler(CheckFollowerObserver observer) {
      super(observer);
    }

    @Override
    protected String getFailedMessagePrefix() {
      return "Follow Service";
    }

    @Override
    protected void handleSuccessMessage(ServiceObserver observer, Bundle data) {
      boolean isFollower = data.getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
      ((CheckFollowerObserver) observer).checkFollowerSucceeded(isFollower);
    }
  }

  private static class GetFollowersHandler extends BackgroundTaskHandler {

    GetFollowersHandler(GetFollowerObserver observer) {
      super(observer);
    }


    @Override
    protected String getFailedMessagePrefix() {
      return "Follow Service";
    }

    @Override
    protected void handleSuccessMessage(ServiceObserver observer, Bundle data) {
      List<User> followers = (List<User>) data.getSerializable(PagedTask.ITEMS_KEY);
      ((GetFollowerObserver) observer).getFollowerSucceeded(followers);
    }
  }

  private static class GetFollowingHandler extends BackgroundTaskHandler {

    public GetFollowingHandler(GetFollowingObserver observer) {
      super(observer);
    }

    @Override
    protected String getFailedMessagePrefix() {
      return "Follow Service";
    }

    @Override
    protected void handleSuccessMessage(ServiceObserver observer, Bundle data) {
      List<User> followees = (List<User>) data.getSerializable(PagedTask.ITEMS_KEY);
      boolean hasMorePages = data.getBoolean(GetFollowingTask.MORE_PAGES_KEY);
      ((GetFollowingObserver) observer).setLastFollowee(followees, hasMorePages);
      ((GetFollowingObserver) observer).getFollowingSucceeded(followees);
    }
  }

  private static class GetFollowersCountHandler extends BackgroundTaskHandler {


    public GetFollowersCountHandler(GetFollowerCountObserver observer) {
      super(observer);
    }

    @Override
    protected String getFailedMessagePrefix() {
      return "Follow Service";
    }

    @Override
    protected void handleSuccessMessage(ServiceObserver observer, Bundle data) {
      int count = data.getInt(GetFollowersCountTask.COUNT_KEY);
      ((GetFollowerCountObserver) observer).setFollowerCount(count);
    }
  }

  private static class GetFollowingCountHandler extends BackgroundTaskHandler {


    public GetFollowingCountHandler(GetFolloweeCountObserver observer) {
      super(observer);
    }

    @Override
    protected String getFailedMessagePrefix() {
      return "Follow Service";
    }

    @Override
    protected void handleSuccessMessage(ServiceObserver observer, Bundle data) {
      int count = data.getInt(GetFollowingCountTask.COUNT_KEY);
      ((GetFolloweeCountObserver) observer).setFolloweeCount(count);
    }
  }

  private class FollowHandler extends BackgroundTaskHandler {

    GetFolloweeCountObserver followeeCountObserver;
    GetFollowerCountObserver followerCountObserver;

    FollowHandler(GetFollowObserver observer,
                  GetFolloweeCountObserver followeeCountObserver,
                  GetFollowerCountObserver followerCountObserver) {
      super(observer);
      this.followeeCountObserver = followeeCountObserver;
      this.followerCountObserver = followerCountObserver;
    }

    @Override
    protected String getFailedMessagePrefix() {
      return "Follow Service";
    }

    @Override
    protected void handleSuccessMessage(ServiceObserver observer, Bundle data) {
      updateSelectedUserFollowingAndFollowers(followerCountObserver, followeeCountObserver);
      ((GetFollowObserver) observer).getFollowSucceeded();
    }
  }

  private class UnfollowHandler extends BackgroundTaskHandler {

    GetFolloweeCountObserver followeeCountObserver;
    GetFollowerCountObserver followerCountObserver;

    UnfollowHandler(GetUnfollowObserver observer,
                    GetFolloweeCountObserver followeeCountObserver,
                    GetFollowerCountObserver followerCountObserver) {
      super(observer);
      this.followeeCountObserver = followeeCountObserver;
      this.followerCountObserver = followerCountObserver;
    }

    @Override
    protected String getFailedMessagePrefix() {
      return null;
    }

    @Override
    protected void handleSuccessMessage(ServiceObserver observer, Bundle data) {
      updateSelectedUserFollowingAndFollowers(followerCountObserver, followeeCountObserver);
      ((GetUnfollowObserver) observer).getUnfollowSucceeded();
    }
  }
}