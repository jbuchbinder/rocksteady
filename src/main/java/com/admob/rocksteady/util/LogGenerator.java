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
package com.admob.rocksteady.util;

import com.admob.rocksteady.router.MessageManager;
import com.admob.rocksteady.event.Log;

import java.io.*;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LogGenerator {
  private static final Logger logger = LoggerFactory.getLogger(LogGenerator.class);

  private String rabbitExchange;

  /*
   * Null constructor
   */
  public LogGenerator() {

  }

  public void initialize() {
    logger.info("Initialized Log Generator");
  }

  public void shutdown() {

  }

  public void setRabbitExchange(String rabbitExchange) {
    this.rabbitExchange = rabbitExchange;
  }

  /*
   * Call this method to pump logs
   */
  public void createRandomLogs() {
    /**
     * We are going to create bunch of serialized Log object and send them to
     * rabbit.
     */
    // logger.info("Generating random log lines....");

    Random generator = new Random();


    try {
      Integer i;
      Integer end;
      end = generator.nextInt(10);
      for (i = 0; i <= end; i++) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        Log log = new Log();

        // logger.info("Loop 1 " + String.valueOf(i));

        log.setHostname("had" + i + ".sc9.admob.int");
        log.setTimestamp("332423");
        log.setName("dmJob100");
        log.setValue("Log line as following: random string " + generator.nextInt());
        log.setError("Line explaining error 1 ");
        log.setStatus(2);
        out.writeObject(log);
        out.flush();
        // Send message to rabbit
        MessageManager.getInstance().publishMessage(baos.toByteArray(), rabbitExchange);

      }
      end = generator.nextInt(10);
      for (i = 0; i <= end; i++) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        Log log = new Log();
        // logger.info("Loop 2 " + String.valueOf(baos.size()));

        log.setHostname("had" + i + ".sc9.admob.int");
        log.setTimestamp("332423");
        log.setName("dmJob300");
        log.setValue("Log line as following: random string " + generator.nextInt());
        log.setError("line explaining error 2");
        log.setStatus(2);
        out.writeObject(log);
        out.flush();
        // Send message to rabbit
        MessageManager.getInstance().publishMessage(baos.toByteArray(), rabbitExchange);


      }

    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }
}
