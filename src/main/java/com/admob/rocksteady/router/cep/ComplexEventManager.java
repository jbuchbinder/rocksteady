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
package com.admob.rocksteady.router.cep;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.espertech.esper.client.*;

import com.admob.rocksteady.common.Service;
import com.admob.rocksteady.common.Startable;

import java.util.Set;
import java.util.LinkedHashSet;
import java.util.ArrayList;

import org.apache.commons.dbcp.BasicDataSource;

/**
 * This is a wrapper bean arround the Esper Complex Event Processing engine. The
 * manager allows for Esper to be managed and initialized by the Spring
 * container.
 *
 *  NOTE: By default Esper threading is not enabled. This is to make sure that
 * all processing happen in the calling thread. For example if you send an event
 * and expect to read the results within the same thread.
 *
 *  NOTE: Esper relies on ThreadLocal variables to store transient data. There
 * is a potential of a memory leak if the engine is reset in different thread
 * than the one used for sending events. For example currently all events are
 * sent by the L1 consumer. We need to make sure that we cause the L1 Container
 * to refresh the underlying TaskExecutor (i.e the thread factory) so as older
 * threads are released and all associated ThreadLocal variables are GC-ed
 * properly.
 */
public class ComplexEventManager implements Service, Startable {
  private static final Logger logger = LoggerFactory.getLogger(ComplexEventManager.class);
  private static ComplexEventManager instance;
  private boolean initialized = false;
  private boolean enableThreading;
  private int inBoundThreadPoolSize;
  private int timmerThreadPoolSize;
  private int lruCache;
  private double maxAgeSeconds;
  private double purgeIntervalSeconds;
  private Configuration config;
  private ConfigurationDBRef configDB;
  private BasicDataSource dataSource;
  private EPServiceProvider epServiceProvider;
  private Set<StatementBean> statementBeans = new LinkedHashSet<StatementBean>();

  private ArrayList<String> paths;

  public static ComplexEventManager getInstance() {
    if (instance == null) {
      instance = new ComplexEventManager();
    }
    return instance;
  }

  /**
   * Get the description of the service
   *
   * @return description of the service
   */
  public String getDescription() {
    return "Complex Event Manager";
  }

  /**
   * Get the class paths where the CEP engine will look for event objects
   *
   * @return the class paths array list
   */
  public ArrayList<String> getPaths() {
    return paths;
  }

  /**
   * Set the class paths array where CEP engine will look for event objects
   *
   * @param paths the class path array list
   */
  public void setPaths(ArrayList<String> paths) {
    this.paths = paths;
  }

  /**
   * Get the Esper Configuration object
   *
   * @return the Esper Configuration object
   */
  public Configuration getConfig() {
    return config;
  }

  /**
   * Start the service
   */
  public void start() {
    if (!initialized || instance == null) {
      throw new IllegalStateException(
          "Please initialized the Complex Event Manager before starting it.");
    }
    config = new Configuration();
    // Add the location of the class paths to look for event objects
    for (String path : paths) {
      config.addEventTypeAutoName(path);
    }


    if (this.dataSource != null) {
      configDB = new ConfigurationDBRef();
      configDB.setDriverManagerConnection(dataSource.getClass().getName(), dataSource.getUrl(),
          dataSource.getUsername(), dataSource.getPassword());
      configDB.setLRUCache(lruCache);
      configDB.setExpiryTimeCache(maxAgeSeconds, purgeIntervalSeconds);
      config.addDatabaseReference("mysql_db", configDB);
    }

    // Enable Prioritized Execution
    config.getEngineDefaults().getExecution().setPrioritized(true);
    // Enable debug view
    config.getEngineDefaults().getLogging().setEnableExecutionDebug(false);
    config.getEngineDefaults().getLogging().setEnableTimerDebug(false);
    // Engine metric collection
    config.getEngineDefaults().getMetricsReporting().setEnableMetricsReporting(false);

    // Set the inBound and Timer Thread Pools
    if (enableThreading) {
      config.getEngineDefaults().getThreading().setThreadPoolInbound(true);
      config.getEngineDefaults().getThreading().setThreadPoolInboundNumThreads(
          inBoundThreadPoolSize);
      config.getEngineDefaults().getThreading().setThreadPoolTimerExec(true);
      config.getEngineDefaults().getThreading().setThreadPoolTimerExecNumThreads(
          timmerThreadPoolSize);
    }
    // Setup the Service Provider
    epServiceProvider = EPServiceProviderManager.getDefaultProvider(config);
    addEplStatements();

    logger.info("Loading historic data");
    LoadHistoricData.loadDeploy();
  }

  private void addEplStatements() {
    // Add all the configured EPL statements
    for (StatementBean statementBean : statementBeans) {
      EPStatement epStatement;

      if (statementBean.getValues() != null && statementBean.getValues().size() > 0) {
        EPPreparedStatement prepared =
            epServiceProvider.getEPAdministrator().prepareEPL(statementBean.getEPL());
        int index = 1;
        for (Object value : statementBean.getValues()) {
          prepared.setObject(index, value);
          index++;
        }
        epStatement =
            epServiceProvider.getEPAdministrator().create(prepared, statementBean.getName());
      } else {
        epStatement = epServiceProvider.getEPAdministrator().createEPL(
            statementBean.getEPL(), statementBean.getName());
      }
      // Set all the known UpdateListeners
      for (UpdateListener listener : statementBean.getAllListeners()) {
        epStatement.addListener(listener);
      }
    }
  }

  /**
   * Reset the Engine state.
   */
  public void reset() {
    if (!initialized || instance == null || epServiceProvider == null) {
      throw new IllegalStateException(
          "Please initialized the Complex Event Manager before starting it.");
    }
    epServiceProvider.initialize();
    addEplStatements();

    /*
     * Esper relies on ThreadLocal variables to store transient data. There is a
     * potential of a memory leak if the engine is reset in different thread
     * than the one used for sending events. For example currently all events
     * are sent by the L1 consumer. We need to make sure that we cause the L1
     * Container to refresh the underlying TaskExecutor (i.e the thread factory)
     * so as older threads are released and all associated ThreadLocal variables
     * are GC-ed properly.
     */
    // MessagingManager.getInstance().refreshListener("levelOneMessageContainer",
    // false);
    logger.info("Reset the Complex Event Manager");
  }

  /**
   * Initialize the service
   */
  public void initialize() {
    initialized = true;
    logger.info("Initialized the Complex Event Manager");
  }

  /**
   * Shutdown the service
   */
  public void shutdown() {
    logger.info("Shuting down Complex Event Manager");
    epServiceProvider.destroy();
  }

  /**
   * Set the EPL statements
   *
   * @param statementBeans the EPL statements
   */
  public void setStatements(StatementBean... statementBeans) {
    for (StatementBean statementBean : statementBeans) {
      addStatement(statementBean);
    }
  }

  /**
   * Add a new EPL statement to the ComplexEventManager
   *
   * @param statementBean EPL statement
   */
  public void addStatement(StatementBean statementBean) {
    statementBeans.add(statementBean);
  }

  /**
   * Send a new Event to the CEP Engine for processing
   *
   * @param event the event object
   */
  public void sendEvent(Object event) {
    epServiceProvider.getEPRuntime().sendEvent(event);
  }

  /**
   * Set to true to enable threading in the underlying esper engine.
   *
   * @param enableThreading true to enable esper threading
   */
  public void setEnableThreading(Boolean enableThreading) {
    this.enableThreading = enableThreading;
  }

  /**
   * Set the default size of the inbound thread pool size
   *
   * @param inBoundThreadPoolSize default size of the inbound thread pool size
   */
  public void setInBoundThreadPoolSize(int inBoundThreadPoolSize) {
    this.inBoundThreadPoolSize = inBoundThreadPoolSize;
  }

  /**
   * Set the default size of the timmer thread pool size
   *
   * @param timmerThreadPoolSize default size of the timmer thread pool size
   */
  public void setTimmerThreadPoolSize(int timmerThreadPoolSize) {
    this.timmerThreadPoolSize = timmerThreadPoolSize;
  }

  public void setDataSource(BasicDataSource dataSource) {
    this.dataSource = dataSource;
  }

  public BasicDataSource getDataSource() {
    return dataSource;
  }

  public int getLruCache() {
    return lruCache;
  }

  public void setLruCache(int lruCache) {
    this.lruCache = lruCache;
  }

  public double getMaxAgeSeconds() {
    return maxAgeSeconds;
  }

  public void setMaxAgeSeconds(double maxAgeSeconds) {
    this.maxAgeSeconds = maxAgeSeconds;
  }

  public double getPurgeIntervalSeconds() {
    return purgeIntervalSeconds;
  }

  public void setPurgeIntervalSeconds(double purgeIntervalSeconds) {
    this.purgeIntervalSeconds = purgeIntervalSeconds;
  }
}
