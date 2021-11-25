package edu.byu.cs.tweeter.server.factories.dao;

import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;

import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.server.dao.StoryDAO;
import edu.byu.cs.tweeter.server.factories.abstracts.StoryAbstractFactory;

public class StoryDAOFactory extends StoryAbstractFactory {
  @Override
  public ItemCollection<QueryOutcome> getStories(StoryRequest request) {
    return new StoryDAO().getStories(request);
  }

  @Override
  public PutItemOutcome postStatus(PostStatusRequest request) {
    return new StoryDAO().postStatus(request);
  }
}
