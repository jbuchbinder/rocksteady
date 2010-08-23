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

public class ServerInfo {

  /**
   * Retrieve a string globally uniquely identifying this server (across colos)
   * @return A string globally uniquely identifying this server (across colos)
   */
  public static String getServerName() {
    String hostname;
    try {
      hostname = java.net.InetAddress.getLocalHost().getHostName();
    } catch(java.net.UnknownHostException uhe) {
      hostname = "unknown";
    }

    return hostname;
  }

}