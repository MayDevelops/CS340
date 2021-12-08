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
import edu.byu.cs.tweeter.server.util.Pair;

public abstract class FollowsServiceHelper extends ServiceHelper {
  public FollowsServiceHelper() {
    super();
  }

  public Response runRequest(FollowsPageRequest request) {

    assert request.getLimit() > 0;
    assert request.getFollowerAlias() != null;

    List<User> allFollows = new ArrayList<>(request.getLimit());
    Integer count;

    Pair<ItemCollection<QueryOutcome>, Boolean> items = getQueryOutcome(request);
    Iterator<Item> iterator = items.getFirst().iterator();
    Boolean hasMorePages = items.getSecond();

    while (iterator.hasNext()) {
      Item item = iterator.next();

      Item findUser = findUser(item);
      User tempUser = buildUser(findUser);

      allFollows.add(tempUser);
    }

    count = allFollows.size();
    Item requestUserItem = userDAO.getUser(new UserRequest(request.getFollowerAlias()));
    User tempRequestUser = buildUser(requestUserItem);

    updateCount(new CountRequest(tempRequestUser, count));
    return returnResponse(allFollows, hasMorePages);
  }

  public abstract Pair<ItemCollection<QueryOutcome>, Boolean> getQueryOutcome(FollowsPageRequest request);

  public abstract Item findUser(Item item);

  public abstract void updateCount(CountRequest request);

  public abstract Response returnResponse(List<User> responseFollows, boolean hasMorePages);

}
