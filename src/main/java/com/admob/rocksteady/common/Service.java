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
 * This interface defines a managed service. The service life cycle is managed
 * during startup and shutdown.
 */
public interface Service {

  /**
   * Return the description of the service
   *
   * @return the description
   */
  public String getDescription();

  /**
   * Initialize the Service. This will get called to bootstrap the service.
   */
  public void initialize();

  /**
   * Shutdown the Service. This is called during the shutdown sequence.
   */
  public void shutdown();

}
