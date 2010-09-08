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
package com.admob.rocksteady.common;

/**
 * This class defines all the default constants which will be used when they can
 * not be found in the r2d2.properties file.
 */
public class DefaultConfig {

  /**
   * The default server type
   */
  public static final String ROCKSTEADY = "ROCKSTEADY";

  /**
   * The default properties file name
   */
  public static final String DEFAULT_PROPERTIES_FILE = "META-INF/rocksteady.properties";

  /**
   * The default number of IO threads to use. This will be set to number of
   * cores + one.
   */
  public static final int IO_THREADS = Runtime.getRuntime().availableProcessors() + 1;

  /**
   * The default number of Event threads to use.
   */
  public static final int EVENT_THREADS = 16;

  /**
   * The default number of statistics threads to use by the CollectionManager.
   */
  public static final int STATISTICS_THREADS = 20;

  /**
   * Default Spring Context configuration file name
   */
  public static final String SPRING_CONTEXT_FILE = "META-INF/spring/applicationContext.xml";

  /**
   * The Minimum number of Concurrent Consumers for async endpoints
   */
  public static final int MIN_ASYNC_CONCURRENT_CONSUMERS = 1;

  /**
   * The default Log4J Port
   */
  public static final int LOG4J_PORT = 9091;

  /**
   * The default admin username
   */
  public static final String ADMIN_USERNAME = "admin";

  /**
   * The default flag to enable or disable CEP Threading
   *
   *  NOTE: By default esper threading is disabled. Care should be taken if it
   * needs to enabled since the send and callback events occur in different
   * threads.
   */
  public static final boolean CEP_ENABLE_THREADING = false;

  /**
   * The default size of inBound Thread pool used by the ComplexEventManager.
   */
  public static final int CEP_INBOUND_THREAD_POOL_SIZE = 5;

  /**
   * The default size of Timmer Thread pool used by the ComplexEventManager
   */
  public static final int CEP_TIMMER_THREAD_POOL_SIZE = 5;
}
