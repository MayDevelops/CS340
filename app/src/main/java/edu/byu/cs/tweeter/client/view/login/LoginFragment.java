package edu.byu.cs.tweeter.client.view.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import edu.byu.cs.client.R;
import edu.byu.cs.tweeter.client.presenter.single.LoginPresenter;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Implements the login screen.
 */
public class LoginFragment extends Fragment implements LoginPresenter.LoginView {
  private static final String LOG_TAG = "LoginFragment";

//  private Toast loginInToast;
  private EditText alias;
  private EditText password;
  private TextView errorView;
  private LoginPresenter presenter = new LoginPresenter(this);

  /**
   * Creates an instance of the fragment and places the user and auth token in an arguments
   * bundle assigned to the fragment.
   *
   * @return the fragment.
   */
  public static LoginFragment newInstance() {
    LoginFragment fragment = new LoginFragment();
    return fragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_login, container, false);

    alias = view.findViewById(R.id.loginUsername);
    password = view.findViewById(R.id.loginPassword);
    errorView = view.findViewById(R.id.loginError);
    Button loginButton = view.findViewById(R.id.loginButton);
    loginButton.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View view) {
        presenter.login(alias.getText().toString(), password.getText().toString());
      }
    });

    return view;
  }

  @Override
  public void displayToast(String message) {
    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
  }

  @Override
  public void launchIntent(User user) {
    Intent intent = new Intent(getContext(), MainActivity.class);
    intent.putExtra(MainActivity.CURRENT_USER_KEY, user);
    startActivity(intent);
  }
}
