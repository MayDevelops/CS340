package edu.byu.cs.tweeter.server.factories.interfaces;

import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;

public interface UserDAOInterface {

  RegisterResponse register(RegisterRequest request);

  LoginResponse login(LoginRequest request);

}
