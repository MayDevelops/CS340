package edu.byu.cs.tweeter.server.service.config;

import com.amazonaws.services.dynamodbv2.document.Item;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.factories.abstracts.AuthAbstractFactory;
import edu.byu.cs.tweeter.server.factories.abstracts.FeedAbstractFactory;
import edu.byu.cs.tweeter.server.factories.abstracts.FollowsAbstractFactory;
import edu.byu.cs.tweeter.server.factories.abstracts.StoryAbstractFactory;
import edu.byu.cs.tweeter.server.factories.abstracts.UserAbstractFactory;
import edu.byu.cs.tweeter.server.factories.dao.AuthDAOFactory;
import edu.byu.cs.tweeter.server.factories.dao.FeedDAOFactory;
import edu.byu.cs.tweeter.server.factories.dao.FollowsDAOFactory;
import edu.byu.cs.tweeter.server.factories.dao.StoryDAOFactory;
import edu.byu.cs.tweeter.server.factories.dao.UserDAOFactory;
import edu.byu.cs.tweeter.server.util.Pair;

public class ServiceHelper {
  public static FollowsAbstractFactory followDAO;
  public static UserAbstractFactory userDAO;
  public static FeedAbstractFactory feedDAO;
  public static StoryAbstractFactory storyDAO;
  public static AuthAbstractFactory authDAO;

  public ServiceHelper() {
    if (followDAO == null) {
      followDAO = new FollowsDAOFactory();
    }
    if (userDAO == null) {
      userDAO = new UserDAOFactory();
    }
    if (feedDAO == null) {
      feedDAO = new FeedDAOFactory();
    }
    if (storyDAO == null) {
      storyDAO = new StoryDAOFactory();
    }
    if (authDAO == null) {
      authDAO = new AuthDAOFactory();
    }
  }

  public int getStatusStartingIndex(Status lastStatus, List<Status> allStatuses) {

    int statusIndex = 0;

    if (lastStatus != null) {
      for (int i = 0; i < allStatuses.size(); i++) {
        if (lastStatus.equals(allStatuses.get(i).getUser())) {
          statusIndex = i + 1;
          break;
        }
      }
    }
    return statusIndex;
  }

  public int getUserStartingIndex(String lastUserAlias, List<User> allUsers) {

    int userIndex = 0;

    if (lastUserAlias != null) {
      for (int i = 0; i < allUsers.size(); i++) {
        if (lastUserAlias.equals(allUsers.get(i).getAlias())) {
          userIndex = i + 1;
          break;
        }
      }
    }
    return userIndex;
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

  protected User buildUser(Item item) {
    String userHandle = item.getString("user_handle");
    String firstName = item.getString("first_name");
    String lastName = item.getString("last_name");
    String imageURL = item.getString("image_url");
    return new User(firstName, lastName, userHandle, imageURL);
  }


}
