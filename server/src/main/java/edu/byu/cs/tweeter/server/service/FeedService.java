package edu.byu.cs.tweeter.server.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.BatchWriteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.BatchResultErrorEntry;
import com.amazonaws.services.sqs.model.SendMessageBatchRequest;
import com.amazonaws.services.sqs.model.SendMessageBatchRequestEntry;
import com.amazonaws.services.sqs.model.SendMessageBatchResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.FollowerPageRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.PostUpdateFeedMessagesRequest;
import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.server.factories.abstracts.FeedAbstractFactory;
import edu.byu.cs.tweeter.server.factories.abstracts.FollowsAbstractFactory;
import edu.byu.cs.tweeter.server.factories.abstracts.StoryAbstractFactory;
import edu.byu.cs.tweeter.server.factories.abstracts.UserAbstractFactory;
import edu.byu.cs.tweeter.server.service.config.ServiceHelper;
import edu.byu.cs.tweeter.server.util.Pair;

public class FeedService extends ServiceHelper {
  UserAbstractFactory userDAO = ServiceHelper.userDAO;
  StoryAbstractFactory storyDAO = ServiceHelper.storyDAO;
  FeedAbstractFactory feedDAO = ServiceHelper.feedDAO;
  FollowsAbstractFactory followDAO = ServiceHelper.followDAO;

  public FeedService() {
    super();
  }

  public PostStatusResponse postStatus(PostStatusRequest request) {
    storyDAO.postStatus(request);

    String queueUrl = "https://sqs.us-west-2.amazonaws.com/245031076589/PostStatusQueue";

    SendMessageRequest send_msg_request = new SendMessageRequest()
            .withQueueUrl(queueUrl)
            .withMessageBody(new Gson().toJson(request))
            .withDelaySeconds(5);

    AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
    SendMessageResult send_msg_result = sqs.sendMessage(send_msg_request);
    String msgId = send_msg_result.getMessageId();
    System.out.println("request -> json: Message ID: " + msgId + " \n\n JSON Object: " + new Gson().toJson(request));

    return new PostStatusResponse(request.getStatus());
  }

  public void sendFeed(PostStatusRequest request) {
    FollowsAbstractFactory followDAO = ServiceHelper.followDAO;
    Boolean hasMorePages = true;

    Pair<ItemCollection<QueryOutcome>, Boolean> result;
    Iterator<Item> iterator;
    List<String> followers = new ArrayList<>();
    String lastFollowerAlias = null;

    int counter = 1;


    while (hasMorePages) {
      System.out.println("Counter: " + counter);
      counter += 1;
      result = followDAO.getFollowers(new FollowerPageRequest(request.getStatus().getUser().getAlias(), 2500, lastFollowerAlias));
      iterator = result.getFirst().iterator();
      hasMorePages = result.getSecond();
      followers = populateFollowers(iterator);
      System.out.println("Followers Size: " + followers.size());

      lastFollowerAlias = sendToQueue(new PostUpdateFeedMessagesRequest(followers, request));

      System.out.println("Last alias: " + lastFollowerAlias);

    }
  }

  private void sendBatchedMessages(List<PostUpdateFeedMessagesRequest> requestBatch) {
    String queueUrl = "https://sqs.us-west-2.amazonaws.com/245031076589/UpdateFeedQueue";

    System.out.println("Batch Size: " + requestBatch.size());
    String body;
    Collection<SendMessageBatchRequestEntry> entries = new ArrayList<>();

    for (PostUpdateFeedMessagesRequest r : requestBatch) {
      body = new Gson().toJson(r);
      String firstFollowerAsID = r.getAllAlias().get(0).substring(1);
      entries.add(new SendMessageBatchRequestEntry(firstFollowerAsID, body));
    }

    System.out.println("BatchedFollowerEntries: " + entries);
    SendMessageBatchRequest sendMessageBatchRequest = new SendMessageBatchRequest()
            .withQueueUrl(queueUrl)
            .withEntries(entries);

    AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
    SendMessageBatchResult sendMessageBatchResult = sqs.sendMessageBatch(sendMessageBatchRequest);

    List<BatchResultErrorEntry> failedResults = sendMessageBatchResult.getFailed();
    if (!failedResults.isEmpty()) {
      System.out.println("Failed to write " + failedResults.size() + " messaged to feed");
    }

  }

  private List<String> populateFollowers(Iterator<Item> iterator) {
    List<String> followers = new ArrayList<>();
    while (iterator.hasNext()) {
      Item item = iterator.next();
      followers.add(item.getString("follower_handle"));
    }
    return followers;
  }


  private String sendToQueue(PostUpdateFeedMessagesRequest combo) {

    List<String> allFollowerAlias = combo.getAllAlias();
    PostStatusRequest request = combo.getPostStatusRequest();


    if (allFollowerAlias.size() > 0) {
//      System.out.println("Follower size > 0");
//          addUserBatch(allFollowerAlias, request);

      PostUpdateFeedMessagesRequest updateFeedMessagesRequest = new PostUpdateFeedMessagesRequest(allFollowerAlias, request);
      String queueUrl = "https://sqs.us-west-2.amazonaws.com/245031076589/UpdateFeedQueue";

      SendMessageRequest send_msg_request = new SendMessageRequest()
              .withQueueUrl(queueUrl)
              .withMessageBody(new Gson().toJson(updateFeedMessagesRequest))
              .withDelaySeconds(5);

      AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
      SendMessageResult send_msg_result = sqs.sendMessage(send_msg_request);
      String msgId = send_msg_result.getMessageId();
      System.out.println("request in SEND: " + new Gson().toJson(updateFeedMessagesRequest));

      //clear alias list for IF it has more pages
      String lastAlias = allFollowerAlias.get(allFollowerAlias.size() - 1);
      allFollowerAlias.clear();

      return lastAlias;
    }
    return "";
  }

  public void addUserBatch(List<String> users, PostStatusRequest request) {
    String postUser = request.getStatus().getUser().getAlias();
    String post = request.getStatus().getPost();
    String datetime = request.getStatus().getDate();
    List<String> urls = request.getStatus().getUrls();
    List<String> mentions = request.getStatus().getMentions();

    TableWriteItems items = new TableWriteItems("feed");

    for (String userAlias : users) {
      Item item = new Item().withPrimaryKey("user_handle", userAlias)
              .with("post_user", postUser)
              .with("post", post)
              .with("datetime", datetime)
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


  public FeedResponse getFeed(FeedRequest request) {

    assert request.getLimit() > 0;
    assert request.getUser() != null;
    List<Status> feedStatuses = new ArrayList<>();
    Pair<ItemCollection<QueryOutcome>, Boolean> allFeed = feedDAO.getFeed(request);

    Iterator<Item> feedIterator = allFeed.getFirst().iterator();
    Boolean hasMorePages = allFeed.getSecond();

    while (feedIterator.hasNext()) {
      Item item = feedIterator.next();
      Item userItem = userDAO.getUser(new UserRequest(item.getString("post_user")));

      String userHandle = userItem.getString("user_handle");
      String firstName = userItem.getString("first_name");
      String lastName = userItem.getString("last_name");
      String imageURL = userItem.getString("image_url");
      User user = new User(firstName, lastName, userHandle, imageURL);

      String post = item.getString("post");
      String dateTime = item.getString("datetime");
      List<String> mentions = item.getList("mentions");
      List<String> urls = item.getList("urls");

      Status tempStatus = new Status(post, user, dateTime, urls, mentions);
      feedStatuses.add(tempStatus);
    }

    return new FeedResponse(feedStatuses, hasMorePages);
  }



  /*
  AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
            .withRegion("us-west-2").build();
    DynamoDB dynamoDB = new DynamoDB(client);
    Table followsTable = dynamoDB.getTable("follows");

    ItemCollection<QueryOutcome> results;
    QuerySpec followsSpec;
    Boolean hasMorePages;

    followsSpec = new QuerySpec()
            .withKeyConditionExpression("followee_handle = :v_id")
            .withValueMap(new ValueMap()
                    .withString(":v_id", request.getStatus().getUser().getAlias()))
            .withMaxResultSize(25);

    Index index = followsTable.getIndex("follows_index");

    results = index.query(followsSpec);

    List<Item> a = new ArrayList<>();
    List<Item> finalA1 = a;
    results.pages().forEach(p -> {
      p.iterator().forEachRemaining(o -> finalA1.add(o));
    });


    hasMorePages = results.getLastLowLevelResult().getQueryResult().getLastEvaluatedKey() != null;

    List<String> allFollowerAlias = new ArrayList<>();
    Iterator<Item> resulterator = results.iterator();
    while (resulterator.hasNext()) {
      Item item = resulterator.next();
      allFollowerAlias.add(item.getString("follower_handle"));
    }
    System.out.println("Page: 1");
    String lastAlias = sendToQueue(allFollowerAlias, request);
    System.out.println("Last Alias: " + lastAlias);

//    int page = 2;
//    while (hasMorePages) {
//      System.out.println("Page: " + page++);
//      PrimaryKey lastKey = new PrimaryKey("follower_handle", lastAlias,
//              "followee_handle", request.getStatus().getUser().getAlias());
//
//      followsSpec = new QuerySpec()
//              .withKeyConditionExpression("followee_handle = :v_id")
//              .withValueMap(new ValueMap()
//                      .withString(":v_id", request.getStatus().getUser().getAlias()))
//              .withMaxResultSize(25)
//              .withExclusiveStartKey(lastKey);
//      index = followsTable.getIndex("follows_index");
//
//      results = index.query(followsSpec);
//      a = new ArrayList<>();
//      List<Item> finalA = a;
//      results.pages().forEach(p -> {
//        p.iterator().forEachRemaining(o -> finalA.add(o));
//      });
//
//      allFollowerAlias = new ArrayList<>();
//      resulterator = results.iterator();
//
//      while (resulterator.hasNext()) {
//        Item item = resulterator.next();
//        allFollowerAlias.add(item.getString("follower_handle"));
//      }
//      hasMorePages = (results.getLastLowLevelResult().getQueryResult().getLastEvaluatedKey() != null);
//      System.out.println("HasMorePages: " + hasMorePages);
//
//      lastAlias = sendToQueue(allFollowerAlias, request);
//      System.out.println("Last Alias: " + lastAlias);
//
//    }

    return null;
   */




  /*
  List<String> allFollowerAlias = new ArrayList<>();
    try {

//      ItemCollection<QueryOutcome> allFollowers = followDAO.getAllFollowers(new FollowerPageRequest(request.getStatus().getUser().getAlias()));

      FollowerPageRequest followerPageRequest = new FollowerPageRequest(request.getStatus().getUser().getAlias());
      followerPageRequest.setLimit(25);

      //get the 25 followers of the user who posted the statues
      Pair<ItemCollection<QueryOutcome>, Boolean> pairResult = followDAO.getFollowers(followerPageRequest);
      ItemCollection<QueryOutcome> followers = pairResult.getFirst();
      Boolean hasMorePages = pairResult.getSecond();

      int count = 1;
      while (hasMorePages) {
        System.out.println("Pages iteration: " + count++);

        for (Item follower : followers) {

          String followerAlias = follower.getString("follower_handle");
          allFollowerAlias.add(followerAlias);
        }
//
//        for(String s : allFollowerAlias) {
//          System.out.println("Alias: " + s);
//        }


        if (allFollowerAlias.size() > 0) {
          System.out.println("Follower size > 0");
//          addUserBatch(allFollowerAlias, request);

          PostUpdateFeedMessagesRequest updateFeedMessagesRequest = new PostUpdateFeedMessagesRequest(allFollowerAlias, request);
          String queueUrl = "https://sqs.us-west-2.amazonaws.com/245031076589/UpdateFeedQueue";

          SendMessageRequest send_msg_request = new SendMessageRequest()
                  .withQueueUrl(queueUrl)
                  .withMessageBody(new Gson().toJson(updateFeedMessagesRequest))
                  .withDelaySeconds(5);

          AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
          SendMessageResult send_msg_result = sqs.sendMessage(send_msg_request);
          String msgId = send_msg_result.getMessageId();
          System.out.println("request -> json: Message ID: " + msgId + " \n\n JSON Object: " + new Gson().toJson(updateFeedMessagesRequest));
        }

        followerPageRequest = new FollowerPageRequest(allFollowerAlias.get(allFollowerAlias.size() - 1));
        followerPageRequest.setLimit(25);
        System.out.println("New Page Request: " + new Gson().toJson(followerPageRequest));

        pairResult = followDAO.getFollowers(followerPageRequest);
        followers = pairResult.getFirst();
        //todo pages will be false on the last round, need to make it run +1 times to get the last page
        hasMorePages = pairResult.getSecond();

        System.out.println("Pages: " + hasMorePages);

        allFollowerAlias.clear();
      }

      return true;
    } catch (Exception e) {
      return null;
    }
   */


/*
public Boolean addToFeed(PostStatusRequest request) {
    List<String> allFollowerAlias = new ArrayList<>();

    try {

      ItemCollection<QueryOutcome> allFollowers = followDAO.getFollowers(new FollowerPageRequest(request.getStatus().getUser().getAlias()));

      for (Item follower : allFollowers) {

        String followerAlias = follower.getString("follower_handle");
        allFollowerAlias.add(followerAlias);
      }

      if (allFollowerAlias.size() > 0) {
        addUserBatch(allFollowerAlias, request);
      }

      return true;
    } catch (Exception e) {
      return null;
    }
  }


  public void addUserBatch(List<String> users, PostStatusRequest request) {

    TableWriteItems items = new TableWriteItems("feed");

    for (String userAlias : users) {
      Item item = new Item().withPrimaryKey("user_handle", userAlias)
              .with("post_user", request.getStatus().getUser().getAlias())
              .with("post", request.getStatus().getPost())
              .with("datetime", request.getStatus().getDate())
              .with("urls", request.getStatus().getUrls())
              .with("mentions", request.getStatus().getMentions());
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
 */


}

