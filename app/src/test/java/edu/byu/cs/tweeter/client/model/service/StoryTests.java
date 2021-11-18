package edu.byu.cs.tweeter.client.model.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryTests {
  private final AuthToken testUserAuthToken = new AuthToken();
  private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";

  private static final User user1 = new User("Allen", "Anderson", "@allen", MALE_IMAGE_URL);


  StoryService mockService = Mockito.spy(new StoryService());
  StoryService.StoryObserver mockObserver = Mockito.spy(new StoryService.StoryObserver() {
    @Override
    public void storySucceeded(List<Status> statuses, boolean pages) {
    }

    @Override
    public void handleFailure(String message) {
    }
  });


  @Test
  void test() {
    mockService.getStoryTask(testUserAuthToken, user1, null, mockObserver);
    List<String> list = new ArrayList<>();
    list.add("string");
    Status status1 = new Status("post", user1, "date", list, list);
    Status status2 = new Status();
    Status status3 = new Status();

    List<Status> statuses = new ArrayList<>();
    statuses.add(status1);
    statuses.add(status2);
    statuses.add(status3);

    Mockito.verify(mockObserver, Mockito.times(1)).storySucceeded(statuses, true);
  }
}
