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

/**
 * This interface defines a managed service.  The service life cycle is managed
 * during startup and shutdown.
 */
public interface Service {

    /**
     * Return the description of the service
     * @return the description
     */
    public String getDescription();

    /**
     * Initialize the Service.  This will get called to bootstrap the service.
     */
    public void initialize();

    /**
     * Shutdown the Service.  This is called during the shutdown sequence.
     */
    public void shutdown();

}
