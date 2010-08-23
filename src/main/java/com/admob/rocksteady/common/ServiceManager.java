/**
 * Copyright 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
**/
package com.admob.rocksteady.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ContextStartedEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.lang.reflect.InvocationTargetException;

import com.admob.rocksteady.util.ROCKSTEADYSystemException;

/**
 * The Service Manager manages the life cycle of all the services in the server.
 * Services are started and shutdown using this manager.
 */
public class ServiceManager implements ApplicationListener {

    private static final Logger logger = LoggerFactory.getLogger(ServiceManager.class);
    private ArrayList<String> services;

    public ArrayList<String> getServices() {
        return services;
    }

    public void setServices(ArrayList<String> services) {
        this.services = services;
    }

    public void initialize() {
        logger.info("ServiceManager initializing registered Services");
        for (String service : services) {
            initService(service);
        }
    }

    public void start() {
        if (services == null) {
            return;
        }
        for (String service : services) {
           startService(service);
        }
    }

    public void shutdown() {
        logger.info("ServiceManager shutting down registered Services");
        Collections.reverse(services);
        for (String service : services) {
            shutdownService(service);
        }
    }

    private void initService(String serviceClass) {
        try {
            if (serviceClass != null) {
            	// Don't to import service because it's in the same path.
                Service service = (Service) Class.forName(serviceClass).getMethod("getInstance").invoke(null);
                logger.info("Initializing " + service.getDescription());
                service.initialize();
                logger.info(service.getDescription() + " initialized successfully");
            }
        } catch (InvocationTargetException e) {
            throw new ROCKSTEADYSystemException("ServiceManager: Error initializing Service " + serviceClass, e);
        } catch (IllegalAccessException e) {
            throw new ROCKSTEADYSystemException("ServiceManager: Error initializing Service " + serviceClass, e);
        } catch (ClassNotFoundException e) {
            throw new ROCKSTEADYSystemException("ServiceManager: Error initializing Service " + serviceClass, e);
        } catch (NoSuchMethodException e) {
            throw new ROCKSTEADYSystemException("ServiceManager: Error initializing Service " + serviceClass, e);        	
        }
    }

    private void shutdownService(String serviceClass) {
        try {
            if (serviceClass != null) {
                Service service = (Service) Class.forName(serviceClass).getMethod("getInstance").invoke(null);
                logger.info("Shutting down " + service.getDescription());
                service.shutdown();
                logger.info(service.getDescription() + " was shutdown");
            }
        } catch (InvocationTargetException e) {
            throw new ROCKSTEADYSystemException("ServiceManager: Error shutting down Service " + serviceClass, e);
        } catch (IllegalAccessException e) {
            throw new ROCKSTEADYSystemException("ServiceManager: Error shutting down Service " + serviceClass, e);
        } catch (ClassNotFoundException e) {
            throw new ROCKSTEADYSystemException("ServiceManager: Error shutting down Service " + serviceClass, e);
        } catch (NoSuchMethodException e) {
            throw new ROCKSTEADYSystemException("ServiceManager: Error shutting down Service " + serviceClass, e);
        }
    }

    private void startService(String serviceClass) {
        try {
            if (serviceClass != null) {
                Service service = (Service) Class.forName(serviceClass).getMethod("getInstance").invoke(null);
                if (service instanceof Startable) {
                    logger.info("Starting " + service.getDescription());
                    ((Startable) service).start();
                    logger.info(service.getDescription() + " started successfully");
                }
            }
        } catch (InvocationTargetException e) {
            throw new ROCKSTEADYSystemException("ServiceManager: Error Starting Service " + serviceClass, e);
        } catch (IllegalAccessException e) {
            throw new ROCKSTEADYSystemException("ServiceManager: Error Starting Service " + serviceClass, e);
        } catch (ClassNotFoundException e) {
            throw new ROCKSTEADYSystemException("ServiceManager: Error Starting Service " + serviceClass, e);
        } catch (NoSuchMethodException e) {
            throw new ROCKSTEADYSystemException("ServiceManager: Error Starting Service " + serviceClass, e);
        } catch (Exception e) {
        	throw new ROCKSTEADYSystemException("ServiceManager: Error Starting Service " + serviceClass, e);
        }
    }

    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextStartedEvent) {
            start();    
        }
    }
}
