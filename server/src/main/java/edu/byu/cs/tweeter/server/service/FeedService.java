package edu.byu.cs.tweeter.server.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.BatchWriteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.FollowerPageRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingPageRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
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

  public FeedService() {
    super();
  }

  public PostStatusResponse postStatus(PostStatusRequest request) {
    storyDAO.postStatus(request);

    feedDAO.addToFeed(request);

    return new PostStatusResponse(request.getStatus());
  }

  public FeedResponse getFeed(FeedRequest request) {

    assert request.getLimit() > 0;
    assert request.getUser() != null;
    List<Status> feedStatuses = new ArrayList<>();
    ItemCollection<QueryOutcome> allFeed = feedDAO.getFeed(request);
    Iterator<Item> iterator = allFeed.iterator();

    while (iterator.hasNext()) {
      Item item = iterator.next();
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

    Pair<List<Status>, Boolean> feed = getPageOfStatus(request.getLastStatus(), request.getLimit(), feedStatuses);

    List<Status> allStatuses = feed.getFirst();
    boolean hasMorePages = feed.getSecond();

    List<Status> responseFeed = new ArrayList<>(request.getLimit());

    if (request.getLimit() > 0) {
      if (allStatuses != null) {
        int feedIndex = getStatusStartingIndex(request.getLastStatus(), allStatuses);

        for (int limitCounter = 0; feedIndex < allStatuses.size() && limitCounter < request.getLimit(); feedIndex++, limitCounter++) {
          responseFeed.add(allStatuses.get(feedIndex));
        }
      }
    }

    return new FeedResponse(responseFeed, hasMorePages);
  }



}

