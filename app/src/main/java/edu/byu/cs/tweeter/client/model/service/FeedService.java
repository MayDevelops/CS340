package edu.byu.cs.tweeter.client.model.service;

import android.os.Bundle;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.backgroundTask.PagedTask;
import edu.byu.cs.tweeter.client.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.backgroundTask.handler.BackgroundTaskHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.TaskExecutor;
import edu.byu.cs.tweeter.client.backgroundTask.observer.ServiceObserver;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedService {

  public void getFeed(AuthToken authToken, User user, Status lastStatus, FeedObserver observer) {
    int PAGE_SIZE = 10;
    GetFeedTask getFeedTask = new GetFeedTask(authToken,
            user, PAGE_SIZE, lastStatus, new GetFeedHandler(observer));
    new TaskExecutor<>(getFeedTask);
  }

  public void postStatus(String post, User user, StatusObserver observer) {
    try {
      Status newStatus = new Status(post, user, getFormattedDateTime(), parseURLs(post), parseMentions(post));
      PostStatusTask statusTask = new PostStatusTask(Cache.getInstance().getCurrUserAuthToken(),
              newStatus, new PostStatusHandler(observer));
      new TaskExecutor<>(statusTask);
    } catch (Exception ex) {
      String message = "Status post failed because of exception: " + ex.getMessage();
      observer.handleFailure(message);
    }
  }

  public String getFormattedDateTime() throws ParseException {
    SimpleDateFormat userFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    SimpleDateFormat statusFormat = new SimpleDateFormat("MMM d yyyy h:mm aaa");

    return statusFormat.format(userFormat.parse(LocalDate.now().toString() + " " + LocalTime.now().toString().substring(0, 8)));
  }

  public List<String> parseURLs(String post) throws MalformedURLException {
    List<String> containedUrls = new ArrayList<>();
    for (String word : post.split("\\s")) {
      if (word.startsWith("http://") || word.startsWith("https://")) {

        int index = findUrlEndIndex(word);

        word = word.substring(0, index);

        containedUrls.add(word);
      }
    }

    return containedUrls;
  }

  public List<String> parseMentions(String post) {
    List<String> containedMentions = new ArrayList<>();

    for (String word : post.split("\\s")) {
      if (word.startsWith("@")) {
        word = word.replaceAll("[^a-zA-Z0-9]", "");
        word = "@".concat(word);

        containedMentions.add(word);
      }
    }

    return containedMentions;
  }

  public int findUrlEndIndex(String word) {
    if (word.contains(".com")) {
      int index = word.indexOf(".com");
      index += 4;
      return index;
    } else if (word.contains(".org")) {
      int index = word.indexOf(".org");
      index += 4;
      return index;
    } else if (word.contains(".edu")) {
      int index = word.indexOf(".edu");
      index += 4;
      return index;
    } else if (word.contains(".net")) {
      int index = word.indexOf(".net");
      index += 4;
      return index;
    } else if (word.contains(".mil")) {
      int index = word.indexOf(".mil");
      index += 4;
      return index;
    } else {
      return word.length();
    }
  }

  public interface FeedObserver extends ServiceObserver {
    void feedSucceeded(List<Status> statuses);
  }

  public interface StatusObserver extends ServiceObserver {
    void statusSucceeded();
  }

  private static class GetFeedHandler extends BackgroundTaskHandler {

    public GetFeedHandler(FeedObserver observer) {
      super(observer);
    }

    @Override
    protected String getFailedMessagePrefix() {
      return "Feed Service";
    }

    @Override
    protected void handleSuccessMessage(ServiceObserver observer, Bundle data) {
      List<Status> statuses = (List<Status>) data.getSerializable(PagedTask.ITEMS_KEY);
      boolean hasMorePages = data.getBoolean(GetFeedTask.MORE_PAGES_KEY);

      ((FeedObserver) observer).feedSucceeded(statuses);
    }
  }

  private static class PostStatusHandler extends BackgroundTaskHandler {

    public PostStatusHandler(StatusObserver observer) {
      super(observer);
    }

    @Override
    protected String getFailedMessagePrefix() {
      return "Feed Service";
    }

    @Override
    protected void handleSuccessMessage(ServiceObserver observer, Bundle data) {
      ((StatusObserver) observer).statusSucceeded();
    }
  }


}
