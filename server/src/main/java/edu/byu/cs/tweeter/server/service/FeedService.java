package edu.byu.cs.tweeter.server.service;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingPageRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.server.factories.abstracts.FollowsAbstractFactory;
import edu.byu.cs.tweeter.server.factories.abstracts.StoryAbstractFactory;
import edu.byu.cs.tweeter.server.factories.abstracts.UserAbstractFactory;
import edu.byu.cs.tweeter.server.util.Pair;

public class FeedService extends FactoryConfig {
  FollowsAbstractFactory followDAO = FactoryConfig.followDAO;
  UserAbstractFactory userDAO = FactoryConfig.userDAO;
  StoryAbstractFactory storyDAO = FactoryConfig.storyDAO;

  public FeedService() {
    super();
  }

  public PostStatusResponse postStatus(PostStatusRequest request) {

    try {
      storyDAO.postStatus(request);
    } catch (Exception e) {
      return null;
    }

    return new PostStatusResponse(request.getStatus());
  }

  public FeedResponse getFeed(FeedRequest request) {

    assert request.getLimit() > 0;
    assert request.getUser() != null;
    List<Status> feedStatuses = new ArrayList<>();
    ItemCollection<QueryOutcome> allFeed = followDAO.query(new FollowingPageRequest(request.getUser().getAlias()));
    Iterator<Item> iterator = allFeed.iterator();

    while (iterator.hasNext()) {
      Item item = iterator.next();
      Item userItem = userDAO.getUser(new UserRequest(item.getString("followee_handle")));

      String userHandle = userItem.getString("user_handle");
      String firstName = userItem.getString("first_name");
      String lastName = userItem.getString("last_name");
      String imageURL = userItem.getString("image_url");
      User user = new User(firstName, lastName, userHandle, imageURL);

      ItemCollection<QueryOutcome> userStories = storyDAO.getStories(new StoryRequest(user));
      Iterator<Item> storyIterator = userStories.iterator();

      while (storyIterator.hasNext()) {
        Item storyItem = storyIterator.next();
        String post = storyItem.getString("post");
        String dateTime = storyItem.getString("datetime");
        List<String> mentions = storyItem.getList("mentions");
        List<String> urls = storyItem.getList("urls");

        Status tempStatus = new Status(post, user, dateTime, urls, mentions);
        feedStatuses.add(tempStatus);
      }
    }


    Pair<List<Status>, Boolean> feed = getPageOfStatus(request.getLastStatus(), request.getLimit(), feedStatuses);

    List<Status> allStatuses = feed.getFirst();
    boolean hasMorePages = feed.getSecond();

    List<Status> responseFeed = new ArrayList<>(request.getLimit());

    if (request.getLimit() > 0) {
      if (allStatuses != null) {
        int feedIndex = getFeedStartingIndex(request.getLastStatus(), allStatuses);

        for (int limitCounter = 0; feedIndex < allStatuses.size() && limitCounter < request.getLimit(); feedIndex++, limitCounter++) {
          responseFeed.add(allStatuses.get(feedIndex));
        }
      }
    }

    return new FeedResponse(responseFeed, hasMorePages);
  }

  private int getFeedStartingIndex(Status lastStatus, List<Status> allFeed) {

    int feedIndex = 0;

    if (lastStatus != null) {
      for (int i = 0; i < allFeed.size(); i++) {
        if (lastStatus.equals(allFeed.get(i).getUser())) {
          feedIndex = i + 1;
          break;
        }
      }
    }

    return feedIndex;
  }

  public Pair<List<Status>, Boolean> getPageOfStatus(Status lastStatus, int limit, List<Status> feedStatuses) {

    Pair<List<Status>, Boolean> result = new Pair<>(new ArrayList<>(), false);

    int index = 0;

    if (lastStatus != null) {
      for (int i = 0; i < feedStatuses.size(); ++i) {
        Status curStatus = feedStatuses.get(i);
        if (curStatus.getUser().getAlias().equals(lastStatus.getUser().getAlias()) &&
                curStatus.getDate().equals(lastStatus.getDate())) {
          index = i + 1;
          break;
        }
      }
    }

    for (int count = 0; index < feedStatuses.size() && count < limit; ++count, ++index) {
      Status curStatus = feedStatuses.get(index);
      result.getFirst().add(curStatus);
    }

    result.setSecond(index < feedStatuses.size());

    return result;
  }

}
