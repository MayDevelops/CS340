package edu.byu.cs.tweeter.server.service;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.server.factories.abstracts.StoryAbstractFactory;
import edu.byu.cs.tweeter.server.factories.abstracts.UserAbstractFactory;
import edu.byu.cs.tweeter.server.util.Pair;

public class StoryService extends FactoryConfig {
  StoryAbstractFactory storyDAO = FactoryConfig.storyDAO;
  UserAbstractFactory userDAO = FactoryConfig.userDAO;

  public StoryService() {
    super();
  }

  public StoryResponse getStories(StoryRequest request) {

    assert request.getLimit() > 0;
    assert request.getUser() != null;
    List<Status> storyStatuses = new ArrayList<>();
    ItemCollection<QueryOutcome> userStories = storyDAO.getStories(request);
    Iterator<Item> storyIterator = userStories.iterator();

    Item userItem = userDAO.getUser(new UserRequest(request.getUser().getAlias()));
    String userHandle = userItem.getString("user_handle");
    String firstName = userItem.getString("first_name");
    String lastName = userItem.getString("last_name");
    String imageURL = userItem.getString("image_url");
    User user = new User(firstName, lastName, userHandle, imageURL);


    while (storyIterator.hasNext()) {
      Item storyItem = storyIterator.next();

      String post = storyItem.getString("post");
      String dateTime = storyItem.getString("datetime");
      List<String> mentions = storyItem.getList("mentions");
      List<String> urls = storyItem.getList("urls");

      Status tempStatus = new Status(post, user, dateTime, urls, mentions);
      storyStatuses.add(tempStatus);
    }

    Pair<List<Status>, Boolean> stories = getPageOfStatus(request.getLastStatus(), request.getLimit(), storyStatuses);

    List<Status> allStatuses = stories.getFirst();
    boolean hasMorePages = stories.getSecond();

    List<Status> responseFeed = new ArrayList<>(request.getLimit());

    if (request.getLimit() > 0) {
      if (allStatuses != null) {
        int feedIndex = getStoryStartingIndex(request.getLastStatus(), allStatuses);

        for (int limitCounter = 0; feedIndex < allStatuses.size() && limitCounter < request.getLimit(); feedIndex++, limitCounter++) {
          responseFeed.add(allStatuses.get(feedIndex));
        }
      }
    }

    return new StoryResponse(responseFeed, hasMorePages);
  }

  private int getStoryStartingIndex(Status lastStatus, List<Status> allFeed) {

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

  public Pair<List<Status>, Boolean> getPageOfStatus(Status lastStatus, int limit, List<Status> storyStatuses) {

    Pair<List<Status>, Boolean> result = new Pair<>(new ArrayList<>(), false);

    int index = 0;

    if (lastStatus != null) {
      for (int i = 0; i < storyStatuses.size(); ++i) {
        Status curStatus = storyStatuses.get(i);
        if (curStatus.getUser().getAlias().equals(lastStatus.getUser().getAlias()) &&
                curStatus.getDate().equals(lastStatus.getDate())) {
          index = i + 1;
          break;
        }
      }
    }

    for (int count = 0; index < storyStatuses.size() && count < limit; ++count, ++index) {
      Status curStatus = storyStatuses.get(index);
      result.getFirst().add(curStatus);
    }

    result.setSecond(index < storyStatuses.size());

    return result;
  }

}
