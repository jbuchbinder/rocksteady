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
package com.admob.rocksteady.util;

/**
 * Application code that detects a condition outside of the bounds of its normal and
 * alternative use cases throw an instance of RocksteadySystemException.
 * <p/>
 * A system exception indicates an unplanned or unexpected failure in the software,
 * network, or hardware system.
 */
public class ROCKSTEADYSystemException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * construct an RocksteadySystemException with a given message.
     *
     * @param msg String containing a message describing the specific circumstances of the failure.
     * @param e Exception to wrap
     */
    public ROCKSTEADYSystemException(String msg, Exception e) {
        super(msg, e);        
    }

    public ROCKSTEADYSystemException(String msg) {
        super(msg, new Exception());
    }

    /**
     * Create the exception.
     */
    public ROCKSTEADYSystemException(Throwable rootCause) {
        super(String.valueOf(rootCause));
    }
}
