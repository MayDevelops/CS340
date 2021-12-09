package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.google.gson.Gson;

import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.server.service.FeedService;

public class PostUpdateFeedMessagesHandler implements RequestHandler<SQSEvent, Void> {
  @Override
  public Void handleRequest(SQSEvent event, Context context) {
    if (event.getRecords() == null) {
      return null;
    }
    FeedService service = new FeedService();

    // Get and parse message
    for (SQSEvent.SQSMessage msg : event.getRecords()) {
      PostStatusRequest request = new Gson().fromJson(msg.getBody(), PostStatusRequest.class);

      service.sendFeed(request);

    }
    return null;
  }
}
