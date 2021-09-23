package edu.byu.cs.tweeter.client.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.client.R;
import edu.byu.cs.tweeter.client.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;
import edu.byu.cs.tweeter.client.view.login.LoginActivity;
import edu.byu.cs.tweeter.client.view.login.StatusDialogFragment;
import edu.byu.cs.tweeter.client.view.util.ImageUtils;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * The main activity for the application. Contains tabs for feed, story, following, and followers.
 */
public class MainActivity extends AppCompatActivity implements StatusDialogFragment.Observer, MainPresenter.View {

  private static final String LOG_TAG = "MainActivity";

  public static final String CURRENT_USER_KEY = "CurrentUser";

  private Toast logOutToast;
  private Toast postingToast;
  private User selectedUser;
  private TextView followeeCount;
  private TextView followerCount;
  private Button followButton;

  private final MainPresenter presenter = new MainPresenter(this);

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    selectedUser = (User) getIntent().getSerializableExtra(CURRENT_USER_KEY);
    if (selectedUser == null) {
      throw new RuntimeException("User not passed to activity");
    }

    SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), selectedUser);
    ViewPager viewPager = findViewById(R.id.view_pager);
    viewPager.setOffscreenPageLimit(1);
    viewPager.setAdapter(sectionsPagerAdapter);
    TabLayout tabs = findViewById(R.id.tabs);
    tabs.setupWithViewPager(viewPager);

    FloatingActionButton fab = findViewById(R.id.fab);

    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        StatusDialogFragment statusDialogFragment = new StatusDialogFragment();
        statusDialogFragment.show(getSupportFragmentManager(), "post-status-dialog");
      }
    });

    presenter.updateFollowingAndFollowers();

    TextView userName = findViewById(R.id.userName);
    userName.setText(selectedUser.getName());

    TextView userAlias = findViewById(R.id.userAlias);
    userAlias.setText(selectedUser.getAlias());

    ImageView userImageView = findViewById(R.id.userImage);
    userImageView.setImageDrawable(ImageUtils.drawableFromByteArray(selectedUser.getImageBytes()));

    followeeCount = findViewById(R.id.followeeCount);
    followeeCount.setText(getString(R.string.followeeCount, "..."));

    followerCount = findViewById(R.id.followerCount);
    followerCount.setText(getString(R.string.followerCount, "..."));

    followButton = findViewById(R.id.followButton);

    if (selectedUser.compareTo(Cache.getInstance().getCurrUser()) == 0) {
      followButton.setVisibility(View.GONE);
    } else {
      followButton.setVisibility(View.VISIBLE);
      IsFollowerTask isFollowerTask = new IsFollowerTask(Cache.getInstance().getCurrUserAuthToken(),
              Cache.getInstance().getCurrUser(), selectedUser, new IsFollowerHandler());
      ExecutorService executor = Executors.newSingleThreadExecutor();
      executor.execute(isFollowerTask);
    }

    followButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        followButton.setEnabled(false);

        if (followButton.getText().toString().equals(v.getContext().getString(R.string.following))) {
          presenter.unfollow(Cache.getInstance().getCurrUserAuthToken(), selectedUser);
          Toast.makeText(MainActivity.this, "Removing " + selectedUser.getName() + "...", Toast.LENGTH_LONG).show();
        } else {
          presenter.follow(Cache.getInstance().getCurrUserAuthToken(), selectedUser);

          Toast.makeText(MainActivity.this, "Adding " + selectedUser.getName() + "...", Toast.LENGTH_LONG).show();
        }
      }
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.main_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.logoutMenu) {
      logOutToast = Toast.makeText(this, "Logging Out...", Toast.LENGTH_LONG);
      logOutToast.show();

      LogoutTask logoutTask = new LogoutTask(Cache.getInstance().getCurrUserAuthToken(), new LogoutHandler());
      ExecutorService executor = Executors.newSingleThreadExecutor();
      executor.execute(logoutTask);

      return true;
    } else {
      return super.onOptionsItemSelected(item);
    }
  }

  public void logoutUser() {
    //Revert to login screen.
    Intent intent = new Intent(this, LoginActivity.class);
    //Clear everything so that the main activity is recreated with the login page.
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    //Clear user data (cached data).
    Cache.getInstance().clearCache();
    startActivity(intent);
  }

  @Override
  public void onStatusPosted(String post) {
    postingToast = Toast.makeText(this, "Posting Status...", Toast.LENGTH_LONG);
    postingToast.show();

    try {
      Status newStatus = new Status(post, Cache.getInstance().getCurrUser(), getFormattedDateTime(), parseURLs(post), parseMentions(post));
      PostStatusTask statusTask = new PostStatusTask(Cache.getInstance().getCurrUserAuthToken(),
              newStatus, new PostStatusHandler());
      ExecutorService executor = Executors.newSingleThreadExecutor();
      executor.execute(statusTask);
    } catch (Exception ex) {
      Log.e(LOG_TAG, ex.getMessage(), ex);
      Toast.makeText(this, "Failed to post the status because of exception: " + ex.getMessage(), Toast.LENGTH_LONG).show();
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

  public void updateFollowButton(boolean removed) {
    // If follow relationship was removed.
    if (removed) {
      followButton.setText(R.string.follow);
      followButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
    } else {
      followButton.setText(R.string.following);
      followButton.setBackgroundColor(getResources().getColor(R.color.white));
      followButton.setTextColor(getResources().getColor(R.color.lightGray));
    }
  }

  // LogoutHandler

  private class LogoutHandler extends Handler {
    @Override
    public void handleMessage(@NonNull Message msg) {
      boolean success = msg.getData().getBoolean(LogoutTask.SUCCESS_KEY);
      if (success) {
        logOutToast.cancel();
        logoutUser();
      } else if (msg.getData().containsKey(LogoutTask.MESSAGE_KEY)) {
        String message = msg.getData().getString(LogoutTask.MESSAGE_KEY);
        Toast.makeText(MainActivity.this, "Failed to logout: " + message, Toast.LENGTH_LONG).show();
      } else if (msg.getData().containsKey(LogoutTask.EXCEPTION_KEY)) {
        Exception ex = (Exception) msg.getData().getSerializable(LogoutTask.EXCEPTION_KEY);
        Toast.makeText(MainActivity.this, "Failed to logout because of exception: " + ex.getMessage(), Toast.LENGTH_LONG).show();
      }
    }
  }


  // IsFollowerHandler

  private class IsFollowerHandler extends Handler {
    @Override
    public void handleMessage(@NonNull Message msg) {
      boolean success = msg.getData().getBoolean(IsFollowerTask.SUCCESS_KEY);
      if (success) {
        boolean isFollower = msg.getData().getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);

        // If logged in user if a follower of the selected user, display the follow button as "following"
        if (isFollower) {
          followButton.setText(R.string.following);
          followButton.setBackgroundColor(getResources().getColor(R.color.white));
          followButton.setTextColor(getResources().getColor(R.color.lightGray));
        } else {
          followButton.setText(R.string.follow);
          followButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }
      } else if (msg.getData().containsKey(IsFollowerTask.MESSAGE_KEY)) {
        String message = msg.getData().getString(IsFollowerTask.MESSAGE_KEY);
        Toast.makeText(MainActivity.this, "Failed to determine following relationship: " + message, Toast.LENGTH_LONG).show();
      } else if (msg.getData().containsKey(IsFollowerTask.EXCEPTION_KEY)) {
        Exception ex = (Exception) msg.getData().getSerializable(IsFollowerTask.EXCEPTION_KEY);
        Toast.makeText(MainActivity.this, "Failed to determine following relationship because of exception: " + ex.getMessage(), Toast.LENGTH_LONG).show();
      }
    }
  }

  // PostStatusHandler

  private class PostStatusHandler extends Handler {
    @Override
    public void handleMessage(@NonNull Message msg) {
      boolean success = msg.getData().getBoolean(PostStatusTask.SUCCESS_KEY);
      if (success) {
        postingToast.cancel();
        Toast.makeText(MainActivity.this, "Successfully Posted!", Toast.LENGTH_LONG).show();
      } else if (msg.getData().containsKey(PostStatusTask.MESSAGE_KEY)) {
        String message = msg.getData().getString(PostStatusTask.MESSAGE_KEY);
        Toast.makeText(MainActivity.this, "Failed to post status: " + message, Toast.LENGTH_LONG).show();
      } else if (msg.getData().containsKey(PostStatusTask.EXCEPTION_KEY)) {
        Exception ex = (Exception) msg.getData().getSerializable(PostStatusTask.EXCEPTION_KEY);
        Toast.makeText(MainActivity.this, "Failed to post status because of exception: " + ex.getMessage(), Toast.LENGTH_LONG).show();
      }
    }
  }


  @Override
  public void followButtonUpdates(boolean value) {
    updateFollowButton(value);
  }

  @Override
  public void setFollowButton(boolean value) {
    followButton.setEnabled(value);
  }

  @Override
  public void setFollowerCount(int count) {
    followerCount.setText(getString(R.string.followerCount, String.valueOf(count)));
  }

  @Override
  public void setFolloweeCount(int count) {
    followeeCount.setText(getString(R.string.followeeCount, String.valueOf(count)));
  }

  @Override
  public void displayToast(String message) {
    Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
  }
}
