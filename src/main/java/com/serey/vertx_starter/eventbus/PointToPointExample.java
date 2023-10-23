package com.serey.vertx_starter.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;


public class PointToPointExample {
  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new Sender());
    vertx.deployVerticle(new Receiver());
  }
  static class Sender extends AbstractVerticle{
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.setPeriodic(1000, id ->{ // send the message every second
        vertx.eventBus().send(Sender.class.getName(),"Sending a message....");
      });


    }
  }

  static class Receiver extends AbstractVerticle{
    public static final Logger LOG = LoggerFactory.getLogger(Receiver.class.getName());
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().consumer(Sender.class.getName(),message->{
        LOG.debug("Received: {}",message.body());
      });
    }
  }
}
