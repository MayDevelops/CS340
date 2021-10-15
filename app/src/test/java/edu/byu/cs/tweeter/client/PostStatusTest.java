package edu.byu.cs.tweeter.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.stream.Stream;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FeedService;
import edu.byu.cs.tweeter.client.presenter.single.MainPresenter;
import edu.byu.cs.tweeter.model.domain.User;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PostStatusTest {
  private MainPresenter presenterSpy;
  private MainPresenter.MainView mockMainView;
  private FeedService mockFeedService;
  private Cache mockCache;

  User user;
  String postString = "Wsup";


  @BeforeAll
  void setUp() {
    mockMainView = Mockito.mock(MainPresenter.MainView.class);
    mockFeedService = Mockito.mock(FeedService.class);
    mockCache = Mockito.mock(Cache.class);

    presenterSpy = Mockito.spy(new MainPresenter(mockMainView));
    Mockito.when(presenterSpy.getFeedService()).thenReturn(mockFeedService);

    Cache.setInstance(mockCache);

  }

  @Test
  void PostSuccessful() {
    Answer<Void> callHandleSucceeded = new Answer<Void>() {
      @Override
      public Void answer(InvocationOnMock invocation) throws Throwable {
        FeedService.StatusObserver observer = invocation.getArgumentAt(2, FeedService.StatusObserver.class);
        Assertions.assertEquals( postString,invocation.getArgumentAt(0, FeedService.class));
        Assertions.assertEquals( user,invocation.getArgumentAt(1, FeedService.class));
        observer.statusSucceeded();
        return null;
      }
    };

    Mockito.doAnswer(callHandleSucceeded).when(mockFeedService).postStatus(Mockito.any(), Mockito.any(), Mockito.any());
    presenterSpy.postStatus(postString, user);
    Mockito.verify(mockMainView).displayToast("Successfully Posted!");

  }


  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  @Nested
  @Tag("handle-fails")
  class FailTests {
    @ParameterizedTest(name = "Should display message {0} as toast")
    @MethodSource("failMessages")
    void failMessages(String msg) {
      Answer<Void> callHandleException = new Answer<Void>() {
        @Override
        public Void answer(InvocationOnMock invocation) throws Throwable {
          FeedService.StatusObserver observer = invocation.getArgumentAt(2, FeedService.StatusObserver.class);
            Assertions.assertEquals( postString,invocation.getArgumentAt(0, FeedService.class));
          Assertions.assertEquals( user,invocation.getArgumentAt(1, FeedService.class));
          observer.handleFailure(msg);
          return null;
        }
      };

      Mockito.doAnswer(callHandleException).when(mockFeedService).postStatus(Mockito.any(), Mockito.any(), Mockito.any());
      presenterSpy.postStatus(postString, user);
      Mockito.verify(mockMainView).displayToast(msg);
    }

    Stream<Arguments> failMessages() {
      return Stream.of(
              Arguments.arguments("exceptionMessage"),
              Arguments.arguments("failMessage")
      );
    }
  }

}
