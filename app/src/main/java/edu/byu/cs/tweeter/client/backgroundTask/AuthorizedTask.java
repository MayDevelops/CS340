package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.model.domain.AuthToken;

public abstract class AuthorizedTask extends BackgroundTask {

  private final AuthToken authToken;

  protected AuthorizedTask(AuthToken authToken, Handler messageHandler) {
    super(messageHandler);
    this.authToken = authToken;
  }

  @Override
  protected void runTask() throws IOException {
//    Cache.getInstance().setCurrUserAuthToken(null);
  }
}
