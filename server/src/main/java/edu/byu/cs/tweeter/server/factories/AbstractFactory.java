package edu.byu.cs.tweeter.server.factories;

import edu.byu.cs.tweeter.model.net.request.Request;
import edu.byu.cs.tweeter.model.net.response.Response;

public abstract class AbstractFactory {
  public abstract Response query(Request request);

  public abstract Response put(Request request);


}
