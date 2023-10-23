package com.serey.vertx_starter.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestResponseExample {
  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new RequestVerticle());
    vertx.deployVerticle(new ResponseVerticle());
  }
  static class RequestVerticle extends AbstractVerticle{
    public static final Logger LOG = LoggerFactory.getLogger(RequestVerticle.class.getName());
    public static final String ADDRESS = "my.request.address";

    @Override
    public void start(final Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      var eventBus = vertx.eventBus(); // there is only one eventbus available per vertx instance
      final String message = "Hello World!";
      LOG.debug("Sending: {}",message);
      eventBus.<String>request(ADDRESS, message, reply->{
        LOG.debug("Response: {}",reply.result().body());
      });

    }
  }
  static class ResponseVerticle extends AbstractVerticle{
    public static final Logger LOG = LoggerFactory.getLogger(ResponseVerticle.class.getName());
    @Override
    public void start(final Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().<String>consumer(RequestVerticle.ADDRESS, message ->{
        LOG.debug("Received Message: {}",message.body());
        message.reply("Received your message. Thanks!");
      });

    }
  }

}
