package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;

import java.util.Iterator;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.server.factories.interfaces.UserDAOInterface;
import edu.byu.cs.tweeter.server.helpers.Passwords;

public class UserDAOProvider implements UserDAOInterface {

  String userHandle;
  String password;
  String firstName;
  String lastName;
  String imageURL;

  public UserDAOProvider() {
  }


  @Override
  public RegisterResponse register(RegisterRequest request) {

    AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
            .withRegion("us-west-2").build();
    DynamoDB dynamoDB = new DynamoDB(client);

    Table table = dynamoDB.getTable("users");

    this.userHandle = request.getUsername();
    this.firstName = request.getFirstName();
    this.lastName = request.getLastName();
    this.password = request.getPassword();
    this.imageURL = request.getImageURL();

    // https://stackoverflow.com/questions/18142745/how-do-i-generate-a-salt-in-java-for-salted-hash
    Passwords passwords = new Passwords();
    byte[] nextSalt = passwords.getNextSalt();
    byte[] hashedPassword = passwords.hash(password.toCharArray(), nextSalt);

//    String stringSalt = new String(nextSalt, StandardCharsets.UTF_8);
//    String stringHashedPassword = new String(hashedPassword, StandardCharsets.UTF_8);


    try {
      PutItemOutcome outcome = table
              .putItem(new Item().withPrimaryKey("user_handle", userHandle)
                      .with("first_name", firstName)
                      .with("last_name", lastName)
                      .with("password", hashedPassword)
                      .with("salt", nextSalt)
                      .with("image_url", imageURL));

      System.out.println("PutItem succeeded:\n" + outcome.getPutItemResult());

      User newUser = new User(firstName, lastName, userHandle, imageURL);
      AuthToken authToken = new AuthToken();
      return new RegisterResponse(newUser, authToken);
    } catch (Exception e) {
      return new RegisterResponse(e.getMessage());
    }
  }

  @Override
  public LoginResponse login(LoginRequest request) {
    AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
            .withRegion("us-west-2").build();
    DynamoDB dynamoDB = new DynamoDB(client);

    this.userHandle = request.getUsername();
    this.password = request.getPassword();

    Table table = dynamoDB.getTable("users");

    QuerySpec spec = new QuerySpec()
            .withKeyConditionExpression("user_handle = :v_id")
            .withValueMap(new ValueMap()
                    .withString(":v_id", userHandle));

    ItemCollection<QueryOutcome> items = table.query(spec);
    Iterator<Item> iterator = items.iterator();
    Item item = iterator.next();


    byte[] dbSalt = item.getBinary("salt");
    byte[] dbPassword = item.getBinary("password");
    Passwords passwords = new Passwords();
    boolean match = passwords.isExpectedPassword(password.toCharArray(), dbSalt, dbPassword);

    if (match) {
      this.firstName = item.getString("first_name");
      this.lastName = item.getString("last_name");
      this.imageURL = item.getString("image_url");
      AuthToken authToken = new AuthToken();
      User user = new User(firstName, lastName, userHandle, imageURL);
      return new LoginResponse(user, authToken);
    } else {
      return new LoginResponse("Failed to Login");
    }
  }
}


