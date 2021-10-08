package edu.byu.cs.tweeter.client.presenter.paged;

import edu.byu.cs.tweeter.model.domain.Status;

public class FeedPresenter extends PagedPresenter<Status> {

  //View Implementation
  public interface FeedView extends PagedView<Status> {
  }

  public FeedPresenter(FeedView view) {
    super(view);
  }

}
