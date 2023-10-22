package com.serey.vertx_starter.eventloops;

import io.vertx.core.*;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.concurrent.TimeUnit;


public class EventLoopExample extends AbstractVerticle {

  public static final Logger LOG = LoggerFactory.getLogger(EventLoopExample.class);
  public static void main(String[] args) {
    var vertx = Vertx.vertx(
      new VertxOptions()
        .setMaxEventLoopExecuteTime(500)
        .setMaxEventLoopExecuteTimeUnit(TimeUnit.MILLISECONDS)
        .setBlockedThreadCheckInterval(1)
        .setBlockedThreadCheckIntervalUnit(TimeUnit.SECONDS)
        .setEventLoopPoolSize(4)
    );
//    vertx.deployVerticle(new EventLoopExample()); //this is for single instance
    vertx.deployVerticle(EventLoopExample.class.getName(),
      new DeploymentOptions().setInstances(5)); // this is for multiple instance of deployment
  }

  @Override
  public void start(final Promise<Void> voidPromise) throws Exception {
    LOG.debug("Start {}",getClass().getName());
    //Thread.sleep(5000);// Thread will sleep for 5 seconds and block the eventloop so to never block the event loop

  }
}
