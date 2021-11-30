package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.model.DeleteItemResult;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;

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
            .with("token", request.getAuthToken().getToken()).withNumber("auth_token", request.getEpoch());

    return authTable.putItem(item);
  }

  @Override
  public Item fetchToken(TokenRequest request) {
    GetItemSpec userSpec = new GetItemSpec()
            .withPrimaryKey("user_handle", request.getAlias());

    return authTable.getItem(userSpec);
  }

  @Override
  public DeleteItemResult removeToken(TokenRequest request) {

    DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
            .withPrimaryKey(new PrimaryKey(
                    "user_handle", request.getAlias())).withReturnValues(ReturnValue.ALL_OLD);

    DeleteItemOutcome outcome = followsTable.deleteItem(deleteItemSpec);

    return outcome.getDeleteItemResult();
  }
}
