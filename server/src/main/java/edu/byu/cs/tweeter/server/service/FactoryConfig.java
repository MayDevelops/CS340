package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.factories.abstracts.FeedAbstractFactory;
import edu.byu.cs.tweeter.server.factories.abstracts.FollowsAbstractFactory;
import edu.byu.cs.tweeter.server.factories.abstracts.StoryAbstractFactory;
import edu.byu.cs.tweeter.server.factories.abstracts.UserAbstractFactory;
import edu.byu.cs.tweeter.server.factories.dao.FeedDAOFactory;
import edu.byu.cs.tweeter.server.factories.dao.FollowsDAOFactory;
import edu.byu.cs.tweeter.server.factories.dao.StoryDAOFactory;
import edu.byu.cs.tweeter.server.factories.dao.UserDAOFactory;

public class FactoryConfig {
  public static FollowsAbstractFactory followDAO;
  public static UserAbstractFactory userDAO;
  public static FeedAbstractFactory feedDAO;
  public static StoryAbstractFactory storyDAO;

  public FactoryConfig() {
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
  }
}
