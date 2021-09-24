package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedService {
  private AuthToken authToken;
  private User user;
  private Status lastStatus;


  public void getFeed(AuthToken authToken, User user, Status lastStatus, FeedObserver observer) {
    int PAGE_SIZE = 10;
    GetFeedTask getFeedTask = new GetFeedTask(authToken,
            user, PAGE_SIZE, lastStatus, new GetFeedHandler(observer));
    ExecutorService executor = Executors.newSingleThreadExecutor();
    executor.execute(getFeedTask);
  }

  public void postStatus(String post, User user, StatusObserver observer) {
    try {
      Status newStatus = new Status(post, user, getFormattedDateTime(), parseURLs(post), parseMentions(post));
      PostStatusTask statusTask = new PostStatusTask(Cache.getInstance().getCurrUserAuthToken(),
              newStatus, new PostStatusHandler(observer));
      ExecutorService executor = Executors.newSingleThreadExecutor();
      executor.execute(statusTask);
    } catch (Exception ex) {
      observer.statusThrewException(ex);
    }
  }

  /**
   * Message handler (i.e., observer) for GetFeedTask.
   */
  private class GetFeedHandler extends Handler {
    private final FeedObserver observer;

    public GetFeedHandler(FeedObserver observer) {
      this.observer = observer;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
      boolean success = msg.getData().getBoolean(GetFeedTask.SUCCESS_KEY);
      if (success) {
        List<Status> statuses = (List<Status>) msg.getData().getSerializable(GetFeedTask.STATUSES_KEY);
        boolean hasMorePages = msg.getData().getBoolean(GetFeedTask.MORE_PAGES_KEY);

        observer.setLastStatus(statuses, hasMorePages);
        observer.feedSucceeded(statuses);
      } else if (msg.getData().containsKey(GetFeedTask.MESSAGE_KEY)) {
        String message = msg.getData().getString(GetFeedTask.MESSAGE_KEY);
        observer.feedFailed(message);
      } else if (msg.getData().containsKey(GetFeedTask.EXCEPTION_KEY)) {
        Exception ex = (Exception) msg.getData().getSerializable(GetFeedTask.EXCEPTION_KEY);
        observer.feedThrewException(ex);
      }
    }
  }

  private class PostStatusHandler extends Handler {
    private final StatusObserver observer;

    public PostStatusHandler(StatusObserver observer) {
      this.observer = observer;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
      boolean success = msg.getData().getBoolean(PostStatusTask.SUCCESS_KEY);
      if (success) {
        observer.statusSucceeded();
      } else if (msg.getData().containsKey(PostStatusTask.MESSAGE_KEY)) {
        String message = msg.getData().getString(PostStatusTask.MESSAGE_KEY);
        observer.statusFailed(message);
      } else if (msg.getData().containsKey(PostStatusTask.EXCEPTION_KEY)) {
        Exception ex = (Exception) msg.getData().getSerializable(PostStatusTask.EXCEPTION_KEY);
        observer.statusThrewException(ex);
      }
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


  public interface FeedObserver {
    void feedSucceeded(List<Status> statuses);

    void feedFailed(String message);

    void feedThrewException(Exception ex);

    void setLastStatus(List<Status> statuses, boolean hasMorePages);
  }

  public interface StatusObserver {
    void statusSucceeded();

    void statusFailed(String message);

    void statusThrewException(Exception ex);

  }


}
