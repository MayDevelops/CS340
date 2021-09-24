package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.FeedService;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter implements FollowService.GetFollowObserver,
        FollowService.GetUnfollowObserver,
        FollowService.GetFollowerCountObserver,
        FollowService.GetFolloweeCountObserver,
        UserService.LogoutObserver,
        FeedService.StatusObserver {

  private View view;


  public MainPresenter(View view) {
    this.view = view;
  }

  public void updateFollowingAndFollowers() {
    new FollowService().updateSelectedUserFollowingAndFollowers(this, this);
  }

  public void postStatus(String post, User user) {
    new FeedService().postStatus(post, user, this);
  }

  //GetFollowObserver Implementation
  @Override
  public void getFollowSucceeded() {
    view.followButtonUpdates(false);
    view.setFollowButton(true);
  }

  @Override
  public void getFollowFailed(String message) {
    String failMessage = "Failed to follow: " + message;
    view.displayToast(failMessage);
  }

  @Override
  public void getFollowThrewException(Exception ex) {
    String exceptionMessage = "Failed to follow because of exception: " + ex.getMessage();
    view.displayToast(exceptionMessage);
  }

  @Override
  public void follow(AuthToken authToken, User user) {
    new FollowService().followTask(authToken, user, this, this, this);
  }

  //GetUnfollowObserver Implementation
  @Override
  public void getUnfollowSucceeded() {
    view.followButtonUpdates(true);
    view.setFollowButton(true);
  }

  @Override
  public void getUnfollowFailed(String message) {
    String failMessage = "Failed to unfollow: " + message;
    view.displayToast(failMessage);
  }

  @Override
  public void getUnfollowThrewException(Exception ex) {
    String exceptionMessage = "Failed to unfollow because of exception: " + ex.getMessage();
    view.displayToast(exceptionMessage);
  }

  @Override
  public void unfollow(AuthToken authToken, User user) {
    new FollowService().unfollowTask(authToken, user, this, this, this);

  }

  @Override
  public void getFollowerCountFailed(String message) {
    String failMessage = "Failed to get follower count: " + message;
    view.displayToast(failMessage);
  }

  @Override
  public void getFollowerCountThrewException(Exception ex) {
    String exceptionMessage = "Failed to get follower count because of exception: " + ex.getMessage();
    view.displayToast(exceptionMessage);
  }

  @Override
  public void setFollowerCount(int count) {
    view.setFollowerCount(count);
  }

  @Override
  public void getFolloweeCountFailed(String message) {
    String failMessage = "Failed to get follower count: " + message;
    view.displayToast(failMessage);
  }

  @Override
  public void getFolloweeCountThrewException(Exception ex) {
    String exceptionMessage = "Failed to get following count because of exception: " + ex.getMessage();
    view.displayToast(exceptionMessage);
  }

  @Override
  public void setFolloweeCount(int count) {
    view.setFolloweeCount(count);
  }

  @Override
  public void logoutSuceeded() {
    view.logout();
  }

  @Override
  public void logoutFailed(String message) {
    String failMessage = "Failed to logout: " + message;
    view.displayToast(failMessage);
  }

  @Override
  public void logoutThrewException(Exception ex) {
    String exceptionMessage = "Failed to logout because of exception: " + ex.getMessage();
    view.displayToast(exceptionMessage);
  }

  @Override
  public void logout() {
    new UserService().logout(this);
  }

  @Override
  public void statusSucceeded() {
    view.displayToast("Successfully Posted!");
  }

  @Override
  public void statusFailed(String message) {
    String failMessage = "Failed to post status: " + message;
    view.displayToast(failMessage);
  }

  @Override
  public void statusThrewException(Exception ex) {
    String exceptionMessage = "Failed to post status because of exception: " + ex.getMessage();
    view.displayToast(exceptionMessage);
  }

  //View Implementation
  public interface View {
    void displayToast(String message);

    void followButtonUpdates(boolean value);

    void setFollowButton(boolean value);

    void setFollowerCount(int count);

    void setFolloweeCount(int count);

    void logout();
  }
}
