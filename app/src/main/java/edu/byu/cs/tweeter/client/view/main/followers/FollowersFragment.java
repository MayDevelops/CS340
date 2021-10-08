package edu.byu.cs.tweeter.client.view.main.followers;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.client.R;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.presenter.paged.FollowersPresenter;
import edu.byu.cs.tweeter.client.state.State;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.client.view.util.ImageUtils;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Implements the "Followers" tab.
 */
public class FollowersFragment extends Fragment implements FollowersPresenter.FollowersView {

  private static final String LOG_TAG = "FollowersFragment";
  private static final String USER_KEY = "UserKey";

  private static final int LOADING_DATA_VIEW = 0;
  private static final int ITEM_VIEW = 1;


  private boolean isLoading = false;

  private FollowersPresenter presenter;
  private FollowersRecyclerViewAdapter FollowersRecyclerViewAdapter;

  /**
   * Creates an instance of the fragment and places the target user in an arguments
   * bundle assigned to the fragment.
   *
   * @param user the user whose Followers is being displayed (not necessarily the logged-in user).
   * @return the fragment.
   */
  public static FollowersFragment newInstance(User user) {
    FollowersFragment fragment = new FollowersFragment();

    Bundle args = new Bundle(1);
    args.putSerializable(USER_KEY, user);

    fragment.setArguments(args);
    return fragment;
  }


  @Override
  public void setLoading(boolean value) {
    isLoading = value;
    if (isLoading) {
      FollowersRecyclerViewAdapter.addLoadingFooter();
    } else {
      FollowersRecyclerViewAdapter.removeLoadingFooter();
    }
  }

  @Override
  public void navigateToUser(User user) {
    Intent intent = new Intent(getContext(), MainActivity.class);
    intent.putExtra(MainActivity.CURRENT_USER_KEY, user);
    startActivity(intent);
  }

  @Override
  public void displayToast(String message) {
    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
  }

  @Override
  public void addItems(List<User> list) {
    FollowersRecyclerViewAdapter.addItems(list);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_followers, container, false);

    User user = (User) getArguments().getSerializable(USER_KEY);
    presenter = new FollowersPresenter(this);

    RecyclerView FollowersRecyclerView = view.findViewById(R.id.followersRecyclerView);

    LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
    FollowersRecyclerView.setLayoutManager(layoutManager);

    FollowersRecyclerViewAdapter = new FollowersRecyclerViewAdapter();
    FollowersRecyclerView.setAdapter(FollowersRecyclerViewAdapter);

    FollowersRecyclerView.addOnScrollListener(new FollowRecyclerViewPaginationScrollListener(layoutManager));

    return view;
  }

  private void loadMoreItems() {
    final Handler handler = new Handler(Looper.getMainLooper());
    handler.postDelayed(() -> {
      try {
        presenter.loadMoreItems(Cache.getInstance().getCurrUserAuthToken(), State.user);
      } catch (MalformedURLException e) {
        e.printStackTrace();
      }
    }, 0);
  }


  /**
   * The ViewHolder for the RecyclerView that displays the Followers data.
   */
  private class FollowersHolder extends RecyclerView.ViewHolder {

    private final ImageView userImage;
    private final TextView userAlias;
    private final TextView userName;

    /**
     * Creates an instance and sets an OnClickListener for the user's row.
     *
     * @param itemView the view on which the user will be displayed.
     */
    FollowersHolder(@NonNull View itemView) {
      super(itemView);

      userImage = itemView.findViewById(R.id.userImage);
      userAlias = itemView.findViewById(R.id.userAlias);
      userName = itemView.findViewById(R.id.userName);

      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          presenter.getUser(State.authToken, userAlias.getText().toString());
        }
      });
    }

    /**
     * Binds the user's data to the view.
     *
     * @param user the user.
     */
    void bindUser(User user) {
      userImage.setImageDrawable(ImageUtils.drawableFromByteArray(user.getImageBytes()));
      userAlias.setText(user.getAlias());
      userName.setText(user.getName());
    }


  }

  /**
   * The adapter for the RecyclerView that displays the Followers data.
   */
  private class FollowersRecyclerViewAdapter extends RecyclerView.Adapter<FollowersHolder> {

    private final List<User> users = new ArrayList<>();

    /**
     * Creates an instance and loads the first page of Followers data.
     */
    FollowersRecyclerViewAdapter() {
      loadMoreItems();
    }

    /**
     * Adds new users to the list from which the RecyclerView retrieves the users it displays
     * and notifies the RecyclerView that items have been added.
     *
     * @param newUsers the users to add.
     */
    void addItems(List<User> newUsers) {
      int startInsertPosition = users.size();
      users.addAll(newUsers);
      this.notifyItemRangeInserted(startInsertPosition, newUsers.size());
    }

    /**
     * Adds a single user to the list from which the RecyclerView retrieves the users it
     * displays and notifies the RecyclerView that an item has been added.
     *
     * @param user the user to add.
     */
    void addItem(User user) {
      users.add(user);
      this.notifyItemInserted(users.size() - 1);
    }

    /**
     * Removes a user from the list from which the RecyclerView retrieves the users it displays
     * and notifies the RecyclerView that an item has been removed.
     *
     * @param user the user to remove.
     */
    void removeItem(User user) {
      int position = users.indexOf(user);
      users.remove(position);
      this.notifyItemRemoved(position);
    }

    /**
     * Creates a view holder for a followee to be displayed in the RecyclerView or for a message
     * indicating that new rows are being loaded if we are waiting for rows to load.
     *
     * @param parent   the parent view.
     * @param viewType the type of the view (ignored in the current implementation).
     * @return the view holder.
     */
    @NonNull
    @Override
    public FollowersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      LayoutInflater layoutInflater = LayoutInflater.from(FollowersFragment.this.getContext());
      View view;

      if (viewType == LOADING_DATA_VIEW) {
        view = layoutInflater.inflate(R.layout.loading_row, parent, false);

      } else {
        view = layoutInflater.inflate(R.layout.user_row, parent, false);
      }

      return new FollowersHolder(view);
    }

    /**
     * Binds the followee at the specified position unless we are currently loading new data. If
     * we are loading new data, the display at that position will be the data loading footer.
     *
     * @param FollowersHolder the ViewHolder to which the followee should be bound.
     * @param position        the position (in the list of followees) that contains the followee to be
     *                        bound.
     */
    @Override
    public void onBindViewHolder(@NonNull FollowersHolder FollowersHolder, int position) {
      if (!isLoading) {
        FollowersHolder.bindUser(users.get(position));
      }
    }

    /**
     * Returns the current number of followees available for display.
     *
     * @return the number of followees available for display.
     */
    @Override
    public int getItemCount() {
      return users.size();
    }

    /**
     * Returns the type of the view that should be displayed for the item currently at the
     * specified position.
     *
     * @param position the position of the items whose view type is to be returned.
     * @return the view type.
     */
    @Override
    public int getItemViewType(int position) {
      return (position == users.size() - 1 && isLoading) ? LOADING_DATA_VIEW : ITEM_VIEW;
    }

    /**
     * Adds a dummy user to the list of users so the RecyclerView will display a view (the
     * loading footer view) at the bottom of the list.
     */
    private void addLoadingFooter() {
      addItem(new User("Dummy", "User", ""));
    }

    /**
     * Removes the dummy user from the list of users so the RecyclerView will stop displaying
     * the loading footer at the bottom of the list.
     */
    private void removeLoadingFooter() {
      removeItem(users.get(users.size() - 1));
    }


  }

  /**
   * A scroll listener that detects when the user has scrolled to the bottom of the currently
   * available data.
   */
  private class FollowRecyclerViewPaginationScrollListener extends RecyclerView.OnScrollListener {

    private final LinearLayoutManager layoutManager;

    /**
     * Creates a new instance.
     *
     * @param layoutManager the layout manager being used by the RecyclerView.
     */
    FollowRecyclerViewPaginationScrollListener(LinearLayoutManager layoutManager) {
      this.layoutManager = layoutManager;
    }

    /**
     * Determines whether the user has scrolled to the bottom of the currently available data
     * in the RecyclerView and asks the adapter to load more data if the last load request
     * indicated that there was more data to load.
     *
     * @param recyclerView the RecyclerView.
     * @param dx           the amount of horizontal scroll.
     * @param dy           the amount of vertical scroll.
     */
    @Override
    public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
      super.onScrolled(recyclerView, dx, dy);

      int visibleItemCount = layoutManager.getChildCount();
      int totalItemCount = layoutManager.getItemCount();
      int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

      if ((visibleItemCount + firstVisibleItemPosition) >=
              totalItemCount && firstVisibleItemPosition >= 0) {
        loadMoreItems();
      }
    }
  }
}

