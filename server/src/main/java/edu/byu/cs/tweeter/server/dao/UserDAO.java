package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import edu.byu.cs.tweeter.model.net.request.FollowerCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.server.factories.interfaces.UserDAOInterface;
import edu.byu.cs.tweeter.server.helpers.Passwords;

public class UserDAO implements UserDAOInterface {
  AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
          .withRegion("us-west-2").build();
  DynamoDB dynamoDB = new DynamoDB(client);
  Table table = dynamoDB.getTable("users");
  String userHandle;
  String password;
  String firstName;
  String lastName;
  String imageURL;

  public UserDAO() {
  }

  @Override
  public Item register(RegisterRequest request) {

    AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion("us-west-2").build();
    String bucket = "tweeterphoties";
    String key = "image_" + request.getFirstName() + "_" + request.getLastName();
    byte[] pic = Base64.decodeBase64(request.getImageBytes());
    InputStream stream = new ByteArrayInputStream(pic);
    PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, key, stream, null);

    s3.putObject(putObjectRequest);
    String url = s3.getUrl(bucket, key).toExternalForm();

    this.userHandle = request.getUsername();
    this.firstName = request.getFirstName();
    this.lastName = request.getLastName();
    this.password = request.getPassword();
    this.imageURL = url;

    // https://stackoverflow.com/questions/18142745/how-do-i-generate-a-salt-in-java-for-salted-hash
    Passwords passwords = new Passwords();
    byte[] nextSalt = passwords.getNextSalt();
    byte[] hashedPassword = passwords.hash(password.toCharArray(), nextSalt);

    try {
      Item item = new Item().withPrimaryKey("user_handle", userHandle)
              .with("first_name", firstName)
              .with("last_name", lastName)
              .with("password", hashedPassword)
              .with("salt", nextSalt)
              .with("image_url", imageURL)
              .with("follower_count", "0")
              .with("following_count", "0");

      table.putItem(item);
      return item;
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public ItemCollection<QueryOutcome> login(LoginRequest request) {
    this.userHandle = request.getUsername();
    this.password = request.getPassword();

    QuerySpec spec = new QuerySpec()
            .withKeyConditionExpression("user_handle = :v_id")
            .withValueMap(new ValueMap()
                    .withString(":v_id", userHandle));

    return table.query(spec);
  }

  @Override
  public Item getUser(UserRequest request) {
    GetItemSpec usersSpec = new GetItemSpec()
            .withPrimaryKey("user_handle", request.getAlias());

    return table.getItem(usersSpec);
  }

  @Override
  public Integer getFollowersCount(FollowerCountRequest request) {
    Item userItem = getUser(new UserRequest(request.getUser().getAlias()));

    return userItem.getInt("follower_count");
  }

  @Override
  public Integer getFollowingCount(FollowingCountRequest request) {
    Item userItem = getUser(new UserRequest(request.getUser().getAlias()));

    return userItem.getInt("following_count");
  }

  @Override
  public Integer updateFollowerCount(FollowerCountRequest request) {
    UpdateItemSpec update = new UpdateItemSpec().withPrimaryKey("user_handle", request.getUser().getAlias())
            .withUpdateExpression("set follower_count = :r").withValueMap(new ValueMap()
                    .withInt(":r", request.getFollowerCount()))
            .withReturnValues(ReturnValue.UPDATED_NEW);

    UpdateItemOutcome count = table.updateItem(update);
    Item item = count.getItem();

    return item.getInt("follower_count");
  }

  @Override
  public Integer updateFollowingCount(FollowingCountRequest request) {
    UpdateItemSpec update = new UpdateItemSpec().withPrimaryKey("user_handle", request.getUser().getAlias())
            .withUpdateExpression("set following_count = :r").withValueMap(new ValueMap()
                    .withInt(":r", request.getFollowingCount()))
            .withReturnValues(ReturnValue.UPDATED_NEW);

    UpdateItemOutcome count = table.updateItem(update);
    Item item = count.getItem();

    return item.getInt("following_count");
  }
}


