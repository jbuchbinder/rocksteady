/**
 * Copyright 2010 Google Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 **/
package com.admob.rocksteady.router;

import java.io.*;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.admob.rocksteady.common.Service;
import com.admob.rocksteady.common.Startable; // import
                                              // com.admob.rocksteady.event.Message;
import com.admob.rocksteady.event.*;
import com.admob.rocksteady.router.cep.ComplexEventManager;

import com.rabbitmq.client.*;


/*
 * This manager establish connection to mq server
 */
public class MessageManager implements Service, Startable {

  private static final Logger logger = LoggerFactory.getLogger(MessageManager.class);
  private static MessageManager instance;
  @SuppressWarnings("unused")
  private boolean initialized = false;
  private String rabbitHostname;
  private String rabbitExchange;
  private String rabbitExchangeLog;
  private String rabbitQueue;
  private boolean rabbitDurable;
  private String rabbitExchangeType;
  private String rabbitRoutingKey;
  private boolean rabbitAutoDelete;

  private Connection mqConnection;
  private ConnectionFactory mqConnectionFactory;
  private Channel mqChannel;
  private QueueingConsumer mqConsumer;
  private boolean QueuingNoAck = false;

  private Integer retryInterval;

  private boolean retryFlag = true;


  // We are doing this here so these managers are started by spring manager
  // instead of just being kicked off by spring randomly.
  public static MessageManager getInstance() {
    if (instance == null) {
      instance = new MessageManager();
    }
    return instance;
  }

  @Override
  public String getDescription() {
    // TODO Auto-generated method stub
    return "MessageManager";
  }

  @Override
  public void initialize() {
    initialized = true;
    logger.info("Initialized the Message Manager");
  }

  @Override
  public void shutdown() {
    setRetryFlag(false);
    // Make sure to close the mqConnection
    try {
      mqChannel.close();
      mqConnection.close(100);
    } catch (com.rabbitmq.client.ShutdownSignalException e) {
      // TODO Auto-generated catch block
      logger.error(e.getMessage());
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (Exception e) {

    }
  }

  @Override
  public void start() {

    connectRabbit();

    QueueingConsumer.Delivery delivery;

    // Here's we are going to loop
    while (true) {
      try {
        // Get the next delivery
        delivery = mqConsumer.nextDelivery();

        if (delivery.getEnvelope().getExchange().equals(rabbitExchange)) {
          // WE HAVE A DELIVERY!!!
          // We going to pass the message to another object and let it handle
          // the parsing.
          ComplexEventManager.getInstance().sendEvent(new Metric(new String(delivery.getBody())));
          // Message m = new Message(new String(delivery.getBody()));

        }


        // Acknowledge the delivery
        mqChannel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

      } catch (ShutdownSignalException e) {
        // TODO Auto-generated catch block\
        if (isRetryFlag()) {
          logger.error("Connection to MQ server lost.");
          connectRabbit();
        }
        // e.printStackTrace();
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        continue;
      } catch (IOException e) {
        // TODO Auto-generated catch block
        logger.error(e.getMessage());
      }

    }
  }

  /*
   * Publish message to a given exchange point
   */
  public void publishMessage(byte[] message, String exchange) {
    try {
      mqChannel.basicPublish(exchange, rabbitRoutingKey, null, message);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      logger.error(e.getMessage());
    }

  }

  private void connectRabbit() {
    // Time to start connection to rabbitmq
    try {
      mqConnection = mqConnectionFactory.newConnection(rabbitHostname);
      mqChannel = mqConnection.createChannel();
      // Now we have connection
      mqChannel.exchangeDeclare(rabbitExchange, rabbitExchangeType);
      // mqChannel.exchangeDeclare(rabbitExchangeLog, rabbitExchangeType);
      // mqChannel.queueDelete(rabbitQueue);

      // Add a random string to queue name so each instance of rocksteady will
      // have its own queue
      Random r = new Random();
      String token = Long.toString(Math.abs(r.nextLong()), 36);

      this.setRabbitQueue("rocksteady" + "-" + token);

      logger.info("Connected to MQ with queue: " + this.getRabbitQueue());


      // Create queue, passive = true, Durable setup externally, exclusive =
      // false, AutoDelete externally.
      mqChannel.queueDeclare(rabbitQueue, false, rabbitDurable, false, rabbitAutoDelete, null);
      mqChannel.queueBind(rabbitQueue, rabbitExchange, rabbitRoutingKey);
      // mqChannel.queueBind(rabbitQueue, rabbitExchangeLog, rabbitRoutingKey);

      mqConsumer = new QueueingConsumer(mqChannel);
      mqChannel.basicConsume(rabbitQueue, QueuingNoAck, mqConsumer);

    } catch (IOException e) {
      // Something's wrong
      logger.error("MQ Connnection failed with reason: " + e.getMessage());
      logger.error("Retrying connection...");
      try {
        Thread.sleep(retryInterval);
      } catch (InterruptedException ex) {

      }
      connectRabbit();
    }
  }

  public void setMqConnectionFactory(ConnectionFactory mqConnectionFactory) {
    this.mqConnectionFactory = mqConnectionFactory;
  }

  public ConnectionFactory getMqConnectionFactory() {
    return mqConnectionFactory;
  }

  public void setRabbitHostname(String rabbitHostname) {
    this.rabbitHostname = rabbitHostname;
  }

  public String getRabbitExchange() {
    return rabbitExchange;
  }

  public void setRabbitExchange(String rabbitExchange) {
    this.rabbitExchange = rabbitExchange;
  }

  public void setRabbitQueue(String rabbitQueue) {
    this.rabbitQueue = rabbitQueue;
  }

  public String getRabbitQueue() {
    return rabbitQueue;
  }

  public boolean isRabbitDurable() {
    return rabbitDurable;
  }

  public void setRabbitDurable(boolean rabbitDurable) {
    this.rabbitDurable = rabbitDurable;
  }

  public void setRabbitRoutingKey(String rabbitRoutingKey) {
    this.rabbitRoutingKey = rabbitRoutingKey;
  }

  public String getRabbitRoutingKey() {
    return rabbitRoutingKey;
  }

  public void setRabbitExchangeType(String rabbitExchangeType) {
    this.rabbitExchangeType = rabbitExchangeType;
  }

  public String getRabbitExchangeType() {
    return rabbitExchangeType;
  }

  public void setRabbitAutoDelete(boolean rabbitAutoDelete) {
    this.rabbitAutoDelete = rabbitAutoDelete;
  }

  public boolean getRabbitAutoDelete() {
    return rabbitAutoDelete;
  }

  public void setRabbitExchangeLog(String rabbitExchangeLog) {
    this.rabbitExchangeLog = rabbitExchangeLog;
  }

  public String getRabbitExchangeLog() {
    return rabbitExchangeLog;
  }

  public void setRetryInterval(Integer retryInterval) {
    this.retryInterval = retryInterval;
  }

  public Integer getRetryInterval() {
    return retryInterval;
  }

  public boolean isRetryFlag() {
    return retryFlag;
  }

  public void setRetryFlag(boolean retryFlag) {
    this.retryFlag = retryFlag;
  }

}
