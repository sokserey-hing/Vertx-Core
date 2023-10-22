package com.serey.vertx_starter.worker;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkerExample extends AbstractVerticle {
  public static final Logger LOG = LoggerFactory.getLogger(WorkerExample.class);
  public static void main(String[] args) {
    var Vertx = io.vertx.core.Vertx.vertx();
    Vertx.deployVerticle(new WorkerExample());
  }

  @Override
  public void start(final Promise<Void> startPromise) throws Exception {
    vertx.deployVerticle(new WorkerVerticle(),
      new DeploymentOptions()
        .setWorker(true)
        .setWorkerPoolSize(1)
        .setWorkerPoolName("my-worker-verticle")
    );
    startPromise.complete();
    executeBlockingCode();
  }

  private void executeBlockingCode() {
    vertx.executeBlocking(event -> {
      LOG.debug("Execute blocking code");
      event.complete();
      try{
        Thread.sleep(5000);
        event.fail("Force Fail!!"); // we can even force event loop to fail
      }catch (InterruptedException e){
        LOG.debug("Failed: {}",e);
        event.fail(e);
      }
    },result->{ //this is async handler part
        if(result.succeeded()){
          LOG.debug("Blocking Call Done");
        }else{
          LOG.debug("Blocking call failed", result.cause());
        }
    });
  }
}
