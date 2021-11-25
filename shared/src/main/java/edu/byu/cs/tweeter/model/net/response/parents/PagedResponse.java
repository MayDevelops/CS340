package edu.byu.cs.tweeter.model.net.response.parents;

import edu.byu.cs.tweeter.model.net.response.Response;

public class PagedResponse extends Response {

  private final boolean hasMorePages;

  public PagedResponse(boolean success, boolean hasMorePages) {
    super(success);
    this.hasMorePages = hasMorePages;
  }

  public PagedResponse(boolean success, String message, boolean hasMorePages) {
    super(success, message);
    this.hasMorePages = hasMorePages;
  }

  public boolean getHasMorePages() {
    return hasMorePages;
  }
}
