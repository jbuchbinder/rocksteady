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
package com.admob.rocksteady.util;

/**
 * Application code that detects a condition outside of the bounds of its normal
 * and alternative use cases throw an instance of RocksteadySystemException.
 * <p/>
 * A system exception indicates an unplanned or unexpected failure in the
 * software, network, or hardware system.
 */
public class ROCKSTEADYSystemException extends RuntimeException {

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;

  /**
   * construct an RocksteadySystemException with a given message.
   *
   * @param msg String containing a message describing the specific
   *        circumstances of the failure.
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
