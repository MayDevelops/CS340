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
        FeedService.StatusObserver,
        FollowService.CheckFollowerObserver {

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

  public void isFollower(AuthToken authToken, User user, User selectedUser) {
    new FollowService().isFollower(authToken, user, selectedUser, this);
  }

  //GetFollowObserver Implementation
  @Override
  public void getFollowSucceeded() {
    view.followButtonUpdates(false);
    view.setFollowButton(true);
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
  public void unfollow(AuthToken authToken, User user) {
    new FollowService().unfollowTask(authToken, user, this, this, this);

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
  public interface View {
    void displayToast(String message);

    void followButtonUpdates(boolean value);

    void setFollowButton(boolean value);

    void setFollowerCount(int count);

    void setFolloweeCount(int count);

    void logout();

    void updateFollowButtonColor(boolean value);
  }
}
