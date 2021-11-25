package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;

import edu.byu.cs.tweeter.model.net.request.TokenRequest;
import edu.byu.cs.tweeter.server.dao.config.DAOConfig;
import edu.byu.cs.tweeter.server.factories.interfaces.AuthDAOInterface;

public class AuthDAO extends DAOConfig implements AuthDAOInterface {

  public AuthDAO() {
    super();
  }

  @Override
  public PutItemOutcome registerToken(TokenRequest request) {
    Item item = new Item().withPrimaryKey("user_handle", request.getAlias())
            .with("token", request.getAuthToken());

    return authTable.putItem(item);
  }

  @Override
  public Item fetchToken(TokenRequest request) {
    GetItemSpec userSpec = new GetItemSpec()
            .withPrimaryKey("user_handle", request.getAlias());

    return authTable.getItem(userSpec);
  }
}
