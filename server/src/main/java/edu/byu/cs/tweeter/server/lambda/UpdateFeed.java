package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.BatchWriteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.PostUpdateFeedMessagesRequest;

public class UpdateFeed implements RequestHandler<SQSEvent, Void> {
  @Override
  public Void handleRequest(SQSEvent event, Context context) {
    if (event.getRecords() == null) {
      return null;
    }
    int count = 1;
    for (SQSEvent.SQSMessage msg : event.getRecords()) {
      PostUpdateFeedMessagesRequest request = new Gson().fromJson(msg.getBody(), PostUpdateFeedMessagesRequest.class);

      System.out.println("Adding user batch..." + count++);
      addUserBatch(request.getAllAlias(), request.getPostStatusRequest());

    }
    return null;
  }

  public void addUserBatch(List<String> users, PostStatusRequest request) {
    String postUser = request.getStatus().getUser().getAlias();
    String post = request.getStatus().getPost();
    String dateTime = request.getStatus().getDate();
    List<String> urls = request.getStatus().getUrls();
    List<String> mentions = request.getStatus().getMentions();

    TableWriteItems items = new TableWriteItems("feed");

    for (String userAlias : users) {
      Item item = new Item().withPrimaryKey("user_handle", userAlias)
              .with("post_user", postUser)
              .with("post", post)
              .with("datetime", dateTime)
              .with("urls", urls)
              .with("mentions", mentions);

      items.addItemToPut(item);

      if (items.getItemsToPut() != null && items.getItemsToPut().size() == 25) {
        loopBatchWrite(items);
        items = new TableWriteItems("feed");
      }
    }

    if (items.getItemsToPut() != null && items.getItemsToPut().size() > 0) {
      loopBatchWrite(items);
    }
  }

  private void loopBatchWrite(TableWriteItems items) {
    AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
            .withRegion("us-west-2").build();
    DynamoDB dynamoDB = new DynamoDB(client);

    BatchWriteItemOutcome outcome = dynamoDB.batchWriteItem(items);

    while (outcome.getUnprocessedItems().size() > 0) {
      Map<String, List<WriteRequest>> unprocessedItems = outcome.getUnprocessedItems();
      outcome = dynamoDB.batchWriteItemUnprocessed(unprocessedItems);
    }
  }
}
