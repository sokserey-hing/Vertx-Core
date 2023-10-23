package com.serey.vertx_starter.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.time.Duration;

public class PublishSubscribeExample {
  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new Publish());
    vertx.deployVerticle(new Subscriber1());
    vertx.deployVerticle(new Subscriber2());
    vertx.deployVerticle(Subscriber2.LOG.getName(), new DeploymentOptions().setInstances(2));
  }

  public static class Publish extends AbstractVerticle{
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      super.start(startPromise);
      vertx.setPeriodic(Duration.ofSeconds(10).toMillis(), id->{
        vertx.eventBus().publish(Publish.class.getName(), "A message for everyone....");
      });
    }
  }

  public static class Subscriber1 extends AbstractVerticle{
    public static final Logger LOG = LoggerFactory.getLogger(Subscriber1.class.getName());
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      super.start(startPromise);
      vertx.eventBus().consumer(Publish.class.getName(), message->{
        LOG.debug("Received: {}", message.body());
      });
    }
  }

  public static class Subscriber2 extends AbstractVerticle{
    public static final Logger LOG = LoggerFactory.getLogger(Subscriber2.class.getName());
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      super.start(startPromise);
      vertx.eventBus().consumer(Publish.class.getName(),message->{
        LOG.debug("Received: {}", message.body());
      });
    }
  }

}
