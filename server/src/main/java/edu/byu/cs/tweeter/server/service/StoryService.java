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
import edu.byu.cs.tweeter.server.service.config.ServiceHelper;
import edu.byu.cs.tweeter.server.util.Pair;

public class StoryService extends ServiceHelper {
  StoryAbstractFactory storyDAO = ServiceHelper.storyDAO;
  UserAbstractFactory userDAO = ServiceHelper.userDAO;

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

    User user = buildUser(userItem);

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
        int feedIndex = getStatusStartingIndex(request.getLastStatus(), allStatuses);

        for (int limitCounter = 0; feedIndex < allStatuses.size() && limitCounter < request.getLimit(); feedIndex++, limitCounter++) {
          responseFeed.add(allStatuses.get(feedIndex));
        }
      }
    }

    return new StoryResponse(responseFeed, hasMorePages);
  }

}
