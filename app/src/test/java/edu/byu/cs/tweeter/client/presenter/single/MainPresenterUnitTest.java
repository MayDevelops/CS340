package edu.byu.cs.tweeter.client.presenter.single;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenterUnitTest {

  private MainPresenter.MainView mockMainView;
  private UserService mockUserService;
  private Cache mockCache;

  private MainPresenter mainPresenterSpy;

  @BeforeEach
  public void setup() {
    // Create mock MainPresenter dependencies
    mockMainView = Mockito.mock(MainPresenter.MainView.class);
    mockUserService = Mockito.mock(UserService.class);
    mockCache = Mockito.mock(Cache.class);

    // Create objects required by MainPresenter constructor
    User user = new User("bob", "bobby", "billybob", "url");

    // Create a main presenter spy so we can mock it with the userServiceMock
    mainPresenterSpy = Mockito.spy(new MainPresenter(mockMainView));
    Mockito.when(mainPresenterSpy.getUserService()).thenReturn(mockUserService);

    // Inject a mock cache into the Cache class
    Cache.setInstance(mockCache);
  }

  @Test
  public void testLogout_logoutSucceeds() {
    // Create an answer object to make the mock UserService call handleSucceeded on it's
    // observer when it's logout method is called
    Answer<Void> callHandleSucceededAnswer = new Answer<Void>() {
      @Override
      public Void answer(InvocationOnMock invocation) throws Throwable {
        UserService.LogoutObserver observer = invocation.getArgumentAt(1, UserService.LogoutObserver.class);
        observer.logoutSucceeded();
        return null;
      }
    };

    Mockito.doAnswer(callHandleSucceededAnswer).when(mockUserService).logout(Mockito.any());

    // Invoke the logout method
    mainPresenterSpy.logout();

    // Verify that the expected methods were called on the main view
    Mockito.verify(mockMainView).displayToast("Logging Out...");

    // Verify that the cache was cleared
    Mockito.verify(mockCache).clearCache();
  }

  @Test
  public void testLogout_logoutFailsWithErrorMessage() {
    // Create an answer object to make the mock UserService call handleFailed on it's
    // observer when it's logout method is called
    Answer<Void> callHandleFailedAnswer = new Answer<Void>() {
      @Override
      public Void answer(InvocationOnMock invocation) throws Throwable {
        UserService.LogoutObserver observer = invocation.getArgumentAt(1, UserService.LogoutObserver.class);
        observer.handleFailure("failure message");
        return null;
      }
    };

    Mockito.doAnswer(callHandleFailedAnswer).when(mockUserService).logout(Mockito.any());

    mainPresenterSpy.logout();

    verifyFailureMethodCalls("Failed to logout: failure message");
  }

  @Test
  public void testLogout_logoutFailsWithExceptionMessage() {
    // Create an answer object to make the mock UserService call handleException on it's
    // observer when it's logout method is called
    Answer<Void> callHandleExceptionAnswer = new Answer<Void>() {
      @Override
      public Void answer(InvocationOnMock invocation) throws Throwable {
        UserService.LogoutObserver observer = invocation.getArgumentAt(1, UserService.LogoutObserver.class);
        observer.handleFailure("The exception message");
        return null;
      }
    };

    Mockito.doAnswer(callHandleExceptionAnswer).when(mockUserService).logout(Mockito.any());

    mainPresenterSpy.logout();

    verifyFailureMethodCalls("Failed to logout because of exception: The exception message");
  }

  private void verifyFailureMethodCalls(String errorMessage) {
    // Verify that the expected methods were called on the main view
    Mockito.verify(mockMainView).displayToast("Logging Out...");
    Mockito.verify(mockMainView).displayToast(errorMessage);

    // Verify that the cache was NOT cleared
    Mockito.verify(mockCache, Mockito.times(0)).clearCache();
  }
}
