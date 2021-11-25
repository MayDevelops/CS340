package edu.byu.cs.tweeter.server.factories.abstracts;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;

import edu.byu.cs.tweeter.model.net.request.TokenRequest;

public abstract class AuthAbstractFactory {
  public abstract PutItemOutcome registerToken(TokenRequest request);

  public abstract Item fetchToken(TokenRequest request);

}
