package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import service.UserService;

public class LoginPresenter  implements UserService.LoginObserver {

    public interface View {

        void navigateToUser(User user);

        void displayErrorMessage(String message);

        void clearErrorMessage();

        void displayInfoMessage(String message);

        void clearInfoMessage();

    }

    private final View view;

    public LoginPresenter(View v) {
        view = v;
    }


    public void login(String alias, String pw) {
        new UserService().login(alias, pw, this);
    }

    @Override
    public void loginSucceeded(AuthToken authToken, User user) {

    }

    @Override
    public void loginFailed(String message) {

    }

    @Override
    public void loginThrewException(Exception e) {

    }

}
