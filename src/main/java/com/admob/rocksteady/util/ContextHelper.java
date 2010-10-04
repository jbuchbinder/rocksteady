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

import java.util.HashMap;
import java.util.Properties;

import com.admob.rocksteady.common.DefaultConfig;
import com.admob.rocksteady.common.Config;


public class ContextHelper {
  public static final String SERVER_CONTEXT_KEY = "rocksteady.server.context.nodename";
  public static final String HOST_ADDRESS_KEY = "rocksteady.server.host";
  private static final boolean isServerContext;
  private static final HashMap<String, Object> WSHomes = new HashMap<String, Object>();

  static {
    // First set the runtime context
    isServerContext = System.getProperty(SERVER_CONTEXT_KEY) != null;
  }



}
