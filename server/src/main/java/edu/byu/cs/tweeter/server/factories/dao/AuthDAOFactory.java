package edu.byu.cs.tweeter.server.factories.dao;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.model.DeleteItemResult;

import edu.byu.cs.tweeter.model.net.request.TokenRequest;
import edu.byu.cs.tweeter.server.dao.AuthDAO;
import edu.byu.cs.tweeter.server.factories.abstracts.AuthAbstractFactory;

public class AuthDAOFactory extends AuthAbstractFactory {
  @Override
  public PutItemOutcome registerToken(TokenRequest request) {
    return new AuthDAO().registerToken(request);
  }

  @Override
  public Item fetchToken(TokenRequest request) {
    return new AuthDAO().fetchToken(request);
  }

  @Override
  public DeleteItemResult removeToken(TokenRequest request) {
    return new AuthDAO().removeToken(request);
  }
}
