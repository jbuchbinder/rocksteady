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
