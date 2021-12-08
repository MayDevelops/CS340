package edu.byu.cs.tweeter.server.dao.config;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;

public class DAOConfig {
  public int MAX_ITEMS = 10;
  AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
          .withRegion("us-west-2").build();
  DynamoDB dynamoDB = new DynamoDB(client);

  protected Table storyTable = null;
  protected Table followsTable = null;
  protected Table usersTable = null;
  protected Table feedTable = null;
  protected Table authTable = null;

  public DAOConfig() {
    if (storyTable == null) {
      storyTable = dynamoDB.getTable("story");
    }
    if (followsTable == null) {
      followsTable = dynamoDB.getTable("follows");
    }
    if (usersTable == null) {
      usersTable = dynamoDB.getTable("users");
    }
    if (feedTable == null) {
      feedTable = dynamoDB.getTable("feed");
    }
    if (authTable == null) {
      authTable = dynamoDB.getTable("auth");
    }
  }









}
