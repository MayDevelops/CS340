package edu.byu.cs.tweeter.server.factories;

import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.request.Request;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.server.dao.UserDAOProvider;

public class UserDAOFactory extends AbstractFactory {
  @Override
  public Response query(Request request) {
    return new UserDAOProvider().login((LoginRequest) request);
  }

  @Override
  public Response put(Request request) {
    return new UserDAOProvider().register((RegisterRequest) request);
  }


}
