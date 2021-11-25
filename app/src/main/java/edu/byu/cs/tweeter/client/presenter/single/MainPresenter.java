package edu.byu.cs.tweeter.client.presenter.single;

import edu.byu.cs.tweeter.client.model.service.FeedService;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowUserRequest;
import edu.byu.cs.tweeter.model.net.request.UnfollowUserRequest;

public class MainPresenter implements FollowService.GetFollowObserver,
        FollowService.GetUnfollowObserver,
        FollowService.GetFollowerCountObserver,
        FollowService.GetFolloweeCountObserver,
        UserService.LogoutObserver,
        FeedService.StatusObserver,
        FollowService.CheckFollowerObserver {

  private MainView view;
  private FeedService feedService = null;
  private UserService userService = null;


  public MainPresenter(MainView view) {
    this.view = view;
  }

  public FeedService getFeedService() {
    if (feedService == null) {
      feedService = new FeedService();
    }
    return feedService;
  }

  public UserService getUserService() {
    if (userService == null) {
      userService = new UserService();
    }
    return userService;
  }


  public void updateFollowingAndFollowers(AuthToken authToken, User user) {
    new FollowService(authToken, user).updateSelectedUserFollowingAndFollowers(this, this);
  }

  public void postStatus(String post, User user) {
    getFeedService().postStatusTask(post, user, this);
  }

  public void isFollower(AuthToken authToken, User user, User selectedUser) {
    new FollowService().isFollower(authToken, user, selectedUser, this);
  }

  //GetFollowObserver Implementation
  @Override
  public void getFollowSucceeded() {
    view.followButtonUpdates(false);
  }

  @Override
  public void follow(AuthToken authToken, User selectedUser, User currentUser) {
    FollowService followService = new FollowService();
    FollowUserRequest followUserRequest = new FollowUserRequest(authToken, selectedUser, currentUser);
    followService.followTask(followUserRequest, this, this, this);
  }

  //GetUnfollowObserver Implementation
  @Override
  public void getUnfollowSucceeded() {
    view.followButtonUpdates(true);
  }

  @Override
  public void unfollow(AuthToken authToken, User selectedUser, User currentUser) {
    FollowService followService = new FollowService();
    UnfollowUserRequest unfollowUserRequest = new UnfollowUserRequest(authToken, selectedUser, currentUser);
    followService.unfollowTask(unfollowUserRequest, this, this, this);
  }

  @Override
  public void setFollowerCount(int count) {
    view.setFollowerCount(count);
  }

  @Override
  public void setFolloweeCount(int count) {
    view.setFolloweeCount(count);
  }

  @Override
  public void logoutSucceeded() {
    view.logout();
  }

  @Override
  public void logout() {
    new UserService().logout(this);
  }

  @Override
  public void statusSucceeded() {
    view.displayToast("Successfully Posted!");
  }

  //CheckFollowerObserver Implementation
  @Override
  public void checkFollowerSucceeded(boolean isFollower) {
    if (isFollower) {
      view.updateFollowButtonColor(isFollower);
    } else {
      view.updateFollowButtonColor(isFollower);
    }
  }

  @Override
  public void handleFailure(String message) {
    view.displayToast(message);

  }

  //View Implementation
  public interface MainView extends SingleView{

    void followButtonUpdates(boolean value);

    void setFollowerCount(int count);

    void setFolloweeCount(int count);

    void logout();

    void updateFollowButtonColor(boolean value);
  }
}
