package edu.byu.cs.tweeter.server.service.config;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.request.parents.CountRequest;
import edu.byu.cs.tweeter.model.net.request.parents.FollowsPageRequest;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.server.service.ServiceHelper;

public abstract class FollowsServiceHelper extends ServiceHelper {
  public FollowsServiceHelper() {
    super();
  }

  Response runRequest(FollowsPageRequest request) {

    assert request.getLimit() > 0;
    assert request.getFollowerAlias() != null;

    List<User> allFollows = new ArrayList<>();
    List<User> responseFollows = new ArrayList<>(request.getLimit());
    Integer count;

    ItemCollection<QueryOutcome> items = getQueryOutcome(request);
    Iterator<Item> iterator = items.iterator();

    while (iterator.hasNext()) {
      Item item = iterator.next();

      Item findUser = findUser(item);
      User tempUser = buildUser(findUser);

      allFollows.add(tempUser);
    }

    count = allFollows.size();
    Item requestUserItem = userDAO.getUser(new UserRequest(request.getFollowerAlias()));
    User tempRequestUser = buildUser(requestUserItem);

//    userDAO.updateFollowerCount(new FollowerCountRequest(tempRequestUser, count));
    updateCount(new CountRequest(tempRequestUser, count));

    boolean hasMorePages = false;

    if (request.getLimit() > 0) {
      if (allFollows != null) {
        int followsIndex = getUserStartingIndex(request.getLastFollowsAlias(), allFollows);

        for (int limitCounter = 0; followsIndex < allFollows.size() && limitCounter < request.getLimit(); followsIndex++, limitCounter++) {
          responseFollows.add(allFollows.get(followsIndex));
        }
        hasMorePages = followsIndex < allFollows.size();
      }
    }
//    return new FollowerPageResponse(responseFollows, hasMorePages);
    return returnResponse(responseFollows, hasMorePages);
  }

  public abstract ItemCollection<QueryOutcome> getQueryOutcome(FollowsPageRequest request);

  public abstract Item findUser(Item item);

  public abstract void updateCount(CountRequest request);

  public abstract Response returnResponse(List<User> responseFollows, boolean hasMorePages);

//  private int getUserStartingIndex(String lastFolloweeAlias, List<User> allFollowees) {
//
//    int followeesIndex = 0;
//
//    if (lastFolloweeAlias != null) {
//      for (int i = 0; i < allFollowees.size(); i++) {
//        if (lastFolloweeAlias.equals(allFollowees.get(i).getAlias())) {
//          followeesIndex = i + 1;
//          break;
//        }
//      }
//    }
//    return followeesIndex;
//  }

//  User buildUser(Item item) {
//    String userHandle = item.getString("user_handle");
//    String firstName = item.getString("first_name");
//    String lastName = item.getString("last_name");
//    String imageURL = item.getString("image_url");
//    return new User(firstName, lastName, userHandle, imageURL);
//  }


}
