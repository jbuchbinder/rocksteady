/**
 * Copyright 2010 Google Inc.

 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
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
          String body = new String(delivery.getBody());
          String[] metrics = body.split("\n");
          for (String m: metrics) {
            try {
              ComplexEventManager.getInstance().sendEvent(new Metric(m));
            } catch (Exception e) {
              logger.error("Trouble handling metric: " + m);
            }
          }
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
      //mqChannel.exchangeDelete(rabbitExchange);
      mqChannel.exchangeDeclare(rabbitExchange, rabbitExchangeType, false, rabbitDurable, rabbitAutoDelete, null);

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
