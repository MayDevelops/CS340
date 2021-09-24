package edu.byu.cs.tweeter.client.presenter;

import android.util.Log;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter implements FollowService.GetFollowObserver,
        FollowService.GetUnfollowObserver,
        FollowService.GetFollowerCountObserver,
        FollowService.GetFolloweeCountObserver {

  private View view;


  public MainPresenter(View view) {
    this.view = view;
  }

  public void updateFollowingAndFollowers() {
    new FollowService().updateSelectedUserFollowingAndFollowers(this, this);
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

  //View Implementation
  public interface View {
    void displayToast(String message);

    void followButtonUpdates(boolean value);

    void setFollowButton(boolean value);

    void setFollowerCount(int count);

    void setFolloweeCount(int count);
  }
}
