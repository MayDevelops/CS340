package edu.byu.cs.tweeter.client;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Date;

import edu.byu.cs.tweeter.client.model.service.FeedService;
import edu.byu.cs.tweeter.client.presenter.single.MainPresenter;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenterTest {
//  MainPresenter presenter;
//  MainPresenter.MainView view;


  private MainPresenter mockPresenter;

  User user;
  Status status1;


  @BeforeEach
  void setUp() {
//    mockPresenter = Mockito.mock(MainPresenter.class);
//    user = new User("Optimus", "Prime", "MegatronStanks", null);
//    status1 = new Status("He Do Be", user, new Date().toString(), null, null);
  }

  @AfterEach
  void tearDown() {

  }


  @Test
  void PostSuccessful() {
//    mockPresenter.postStatus("He Do Be Stanky", user);
//    assertNotNull(mockPresenter);

  }

  @Test
  void PostThrewException() {

  }

  @Test
  void PostFailed() {

  }


}
