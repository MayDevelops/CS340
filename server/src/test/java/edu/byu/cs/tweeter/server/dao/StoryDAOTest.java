package edu.byu.cs.tweeter.server.dao;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;

import android.app.DownloadManager;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.verification.VerificationMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.config.DAOConfig;

class StoryDAOTest extends DAOConfig {
  private static StoryDAO mockDAO;
  private static User[] users;

  @BeforeAll
  static void setUp() {
    String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
    String FEMALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png";

    User user1 = new User("Pikachu", "Pika", "@Pikachu", MALE_IMAGE_URL);
    User user2 = new User("Charmander", "Char", "@Charmander", FEMALE_IMAGE_URL);
    User user3 = new User("Bulbasaur", "Bulba", "@Bulbasaur", MALE_IMAGE_URL);
    User user4 = new User("Squirtle", "Squirt", "@Squirtle", FEMALE_IMAGE_URL);
    User user5 = new User("MewTwo", "Mew", "@MewTwo", MALE_IMAGE_URL);
    User user6 = new User("Mew", "Mewewewe", "@Mew", FEMALE_IMAGE_URL);
    User user7 = new User("Ivysaur", "Ivy", "@Ivysaur", MALE_IMAGE_URL);
    User user8 = new User("Venusaur", "Vena", "@Venusaur", FEMALE_IMAGE_URL);
    User user9 = new User("Charmeleon", "CharChar", "@Charmeleon", MALE_IMAGE_URL);
    User user10 = new User("Charizard", "CharCharChar", "@Charizard", FEMALE_IMAGE_URL);
    User user11 = new User("Wartortle", "War", "@Wartortle", MALE_IMAGE_URL);
    User user12 = new User("Blastoise", "Squirt", "@Blastoise", FEMALE_IMAGE_URL);
    User user13 = new User("Caterpie", "Cater", "@Caterpie", MALE_IMAGE_URL);
    User user14 = new User("Metapod", "Pod", "@Metapod", FEMALE_IMAGE_URL);
    User user15 = new User("Butterfree", "Butter", "@Butterfree", MALE_IMAGE_URL);
    User user16 = new User("Weedle", "Weed", "@Weedle", FEMALE_IMAGE_URL);
    User user17 = new User("Kakuna", "Kaku", "@Kakuna", MALE_IMAGE_URL);
    User user18 = new User("Beedrill", "DrillyDrill", "@Beedrill", MALE_IMAGE_URL);
    User user19 = new User("Pidgey", "Pidg", "@Pidgey", MALE_IMAGE_URL);
    User user20 = new User("Pidgeotto", "Pika", "@Pidgeotto", MALE_IMAGE_URL);

    users = new User[]{user1, user2, user3, user4, user5, user6, user7, user8, user9, user10, user11
            , user12, user13, user14, user15, user16, user17, user18, user19, user20};


    Calendar calendar = new GregorianCalendar();
    List<User> fakeUsers = Arrays.asList(users);
    List<Status> allStatuses = new ArrayList<>();

    for (int i = 0; i < 2; ++i) {
      for (int j = 0; j < fakeUsers.size(); ++j) {
        User sender = fakeUsers.get(j);
        User mention = ((j < fakeUsers.size() - 1) ? fakeUsers.get(j + 1) : fakeUsers.get(0));
        List<String> mentions = Arrays.asList(mention.getAlias());
        String url = "https://www.pokemon.com";
        List<String> urls = Arrays.asList(url);
        String post = "Post " + i + " " + j +
                "\nMy friend " + mention.getAlias() + " likes this website " +
                "\n" + url;
        calendar.add(Calendar.MINUTE, 1);
        String datetime = calendar.getTime().toString();
        Status status = new Status(post, sender, datetime, urls, mentions);
        allStatuses.add(status);
      }
    }

    mockDAO = Mockito.mock(StoryDAO.class);

  }

  @Test
  void testPostStatus() {

    Iterable<Item> mockItemCollection = Mockito.mock(Iterable.class);
    Iterator<Item> mockIterator = Mockito.mock(Iterator.class);

    Item mockItem = new Item().with("attributeName", "Hello World");

    Mockito.when(mockItemCollection.iterator()).thenReturn(mockIterator);
    Mockito.when(mockIterator.hasNext()).thenReturn(true).thenReturn(false);
    Mockito.when(mockIterator.next()).thenReturn(mockItem);

    Mockito.reset(mockItemCollection, mockIterator);

    for(Item i : mockItemCollection) {
      assertSame(i, mockItem);
    }

    verify(mockItemCollection, (VerificationMode) mockIterator);


  }


}


/*
  @Override
  public ItemCollection<QueryOutcome> getStories(StoryRequest request) {
    QuerySpec storySpec = new QuerySpec()
            .withKeyConditionExpression("user_handle = :v_id")
            .withValueMap(new ValueMap()
                    .withString(":v_id", request.getUser().getAlias()));

    return storyTable.query(storySpec);
  }

  @Override
  public PutItemOutcome postStatus(PostStatusRequest request) {
    Item item = new Item().withPrimaryKey("user_handle", request.getStatus().getUser().getAlias())
            .with("post", request.getStatus().getPost())
            .with("datetime", request.getStatus().getDate())
            .with("urls", request.getStatus().getUrls())
            .with("mentions", request.getStatus().getMentions());

    return storyTable.putItem(item);
  }
 */