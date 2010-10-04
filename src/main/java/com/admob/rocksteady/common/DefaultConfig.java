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
