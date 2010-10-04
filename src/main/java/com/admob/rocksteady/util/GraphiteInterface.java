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

import java.io.*;
import java.net.*;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sends statistics to a <a href="http://graphite.wikidot.com/">Graphite</a>
 * instance. Graphite records and graphs these statistics so we can see, for
 * example, our requests per second over time.
 * <p>
 * Graphite is a replacement for our old Cacti system. Cacti had many
 * shortcomings like a large administration overhead that made creating new
 * stats difficult.
 * <p>
 * The Graphite API is pretty simple. It is a socket connection that sends one
 * line of data for each stat when we want to record it. For example the
 * following would save the value 480.72 at UNIX timestamp 1234567890: <code> targeting.api.requests.sc9.tgt101 480.72 1234567890\n * </code>
 * <p>
 * The convension we use for keys is a lower-case string with:
 * <ol>
 * <li>System name (e.g. targeting, adworker, etc.)
 * <li>Subsystem 1
 * <li>...
 * <li>Subsystem N
 * <li>Stat name
 * <li>Colo
 * <li>Machine name
 * </ol>
 */
public class GraphiteInterface {
  /**
   * The maximum number of bytes the backup file will be for failed attempts to
   * sent to Graphite. If you assume each update sends 2 kb worth of stats and
   * you know how often the update interval is, you can figure out roughly how
   * long Graphite can be down before stats are lost. For example if the backup
   * is 512 kb, every 10 seconds we send 2kb, then our backup stores the last
   * 2,560 seconds (~42 minutes).
   */
  private static final int MAX_BACKUP_SIZE = 512 * 1024; // 512 kb

  /**
   * The log instance.
   */


  private static final Logger LOG = LoggerFactory.getLogger(GraphiteInterface.class);

  private Boolean enableSend = true;
  /**
   * The name of this server.
   */
  private static final String SERVER_NAME;

  /**
   * The name of this system like "targeting.". This is the top-level folder all
   * stats will be recorded under.
   */
  private final String systemName;

  /**
   * The server name hosting Graphite.
   */
  private final String graphiteServer;

  /**
   * The port Graphite is listening on.
   */
  private final short graphitePort;

  /**
   * The connection to the Graphite server.
   */
  private Socket socket;

  /**
   * For writing output to <code>socket</code>.
   */
  private OutputStream out;

  /**
   * The number of milliseconds between each stats update.
   */
  private int updateInterval = 5 * 60 * 1000; // 5 minute default

  /**
   * The timer that runs jobs at the <code>updateInterval</code>.
   */
  private Timer jobTimer;

  /**
   * Discovers the host name of this server.
   */
  static {
    String serverName = "";

    try {
      // Get the name like prf101.sc9.admob.int
      InetAddress localMachine = InetAddress.getLocalHost();
      String fullyQualifiedName = localMachine.getHostName();

      // We only want the first two parts.
      if (fullyQualifiedName != null) {
        String[] parts = fullyQualifiedName.split("\\.");
        if (parts.length >= 2) {
          serverName = "." + parts[1] + "." + parts[0];
        }
      }
    } catch (UnknownHostException e) {
      // Don't know the name so just continue on.
    }

    SERVER_NAME = serverName;
  }

  /**
   * Constructs an interface to Graphite to record stats.
   *
   * @param graphiteServer is the server name hosting Graphite such as
   *        "graphite.sc9.admob.int".
   * @param graphitePort is the port number the server is listening on (default
   *        is 2003).
   * @param system is the name of this service such as "targeting".
   */
  public GraphiteInterface(String graphiteServer, short graphitePort, String system) {
    this.graphiteServer = graphiteServer;
    this.graphitePort = graphitePort;
    this.systemName = system + ".";
  }

  /**
   * Constructs an interface to Graphite to record stats.
   *
   * @param graphiteServer is the server name hosting Graphite such as
   *        "graphite.sc9.admob.int".
   * @param system is the name of this service such as "targeting". It is turned
   *        into lower case so our top-level folders have the same naming
   *        convension.
   */
  public GraphiteInterface(String graphiteServer, String system) {
    this(graphiteServer, (short) 2003, system);
  }

  /**
   * Sets how frequently stats should be collected.
   *
   * @param seconds is the number of seconds between each stats update.
   */
  public void setInterval(int seconds) {
    this.updateInterval = seconds * 1000; // Convert to ms
  }


  /**
   * Forms the input string to Graphite to record all the current data.
   *
   * @return A string that can be sent to the Graphite socket.
   */
  public String graphiteString(String name) {
    StringBuilder sb = new StringBuilder(8196);

    // Current UNIX timestamp.
    long timestamp = new Date().getTime() / 1000; // seconds since midnight Jan
                                                  // 1, 1970
    String suffix = " " + timestamp + "\n";

    // Add each statistic.
    if (name != null) {
      sb.append(name);
      sb.append(" ");
      sb.append(suffix);
    }


    String result = sb.toString();
    return result;

  }

  /**
   * Connects to the Graphite server.
   *
   * @throws IOException if the socket fails to connect.
   */
  private void connect() throws IOException {
    // Don't bother if the socket is already open.
    if ((socket == null) || (socket.isConnected() == false)) {
      // Connect the socket.
      socket = new Socket(graphiteServer, graphitePort);

      // Open an output stream to write to the socket.
      out = socket.getOutputStream();

      LOG.trace("Opened socket to Graphite instance.");
    }
  }

  /**
   * Sends a graphite string to Graphite.
   *
   * @param input is all the statistics to record in Graphite's format. See
   *        <code>graphiteString</code>.
   */
  public void send(String input) {
    LOG.trace("Going to record statistics with Graphite.");
    String data;

    data = input;

    if (enableSend) {
      // Write the data to the socket.
      try {
        // Make sure we're connected to Graphite.
        connect();

        // Write the data as sequence of 1-byte characters. Can't just do
        // our.writeUTF because
        // it prefixes UTF encoding characters that we don't want.
        byte[] bytes = data.getBytes();
        out.write(bytes);
        out.flush();

        LOG.trace("Recorded statistics with Graphite.");
      } catch (UnknownHostException e) {
        LOG.warn("Cannot resolve the Graphite server hostname:  " + graphiteServer
            + ".  Are you sure it is configured correctly?  Will save the data and try again.");
        saveStats(data);

        socket = null;
        out = null;
      } catch (IOException e) {
        LOG.warn("Problem sending statistics to Graphite.  Will save the data and try again.");
        LOG.debug("Problem sending to Graphite", e);
        try {
          if (out != null) {
            out.close();
          }

          if (socket != null) {
            socket.close();
          }
        } catch (IOException ex) {
          // Ignore.
        } finally {
          out = null;
          socket = null;
        }
      }
    }
  }

  /**
   * When data can't be sent to Graphite use this method to save it locally to
   * disk. It can later be sent to Graphite through <code>loadSavedStats</code>.
   */
  private void saveStats(String input) {
    try {
      // Make sure we don't save so much that the file runith over.
      int len = input.length();
      if (len > MAX_BACKUP_SIZE) {
        int cutoff = input.indexOf("\n", len - MAX_BACKUP_SIZE) + 1;
        input = input.substring(cutoff, len - 1);
      }

      // Create a temporary file for saving the data.
      File temp = temporaryFile();
      temp.delete();
      temp.createNewFile();

      // Save all the input to the file.
      if (temp.canWrite()) {
        FileOutputStream os = new FileOutputStream(temp);
        DataOutputStream out = new DataOutputStream(os);

        try {
          out.writeUTF(input);
        } finally {
          out.flush();
          out.close();
        }
      }
    } catch (Exception e) {
      LOG.info("Failed to save Graphite data to disk.  This data is lost:\n" + input, e);
    }
  }

  /**
   * If the previous attempt to send data to Graphite failed this will return a
   * non-<code>null<code>string which should be sent.
   */
  private String loadSavedStats() {
    String saved = null;

    try {
      File temp = temporaryFile();

      // Is there saved data to send to Graphite?
      if (temp.exists()) {
        // Read the contents of the file.
        // Extract the contents of the file.
        FileInputStream is = null;

        try {
          is = new FileInputStream(temp);
          DataInputStream in = new DataInputStream(is);
          saved = in.readUTF();
        } catch (Exception e) {
          LOG
              .debug(
                  "Could not read saved Graphite data from the temporary file.  Its statistics are lost.", e);
        } finally {
          try {
            is.close();
          } catch (IOException e) {
            // Ignore.
          }
        }

        // Delete the temporary file.
        temp.delete();
      }
    } catch (Exception e) {
      LOG.debug("Problem with the Graphite data temporary file.  Its statistics are lost.", e);
    }

    return saved;
  }

  /**
   * Returns the object for storing and retrieving failed attempts to save data
   * on Graphite.
   */
  private File temporaryFile() {
    // Get the system's temporary directory.
    String tempDir = System.getProperty("java.io.tmpdir");
    if (tempDir.endsWith(File.separator) == false) {
      // Some operating systems don't do this. Whatever happen
      tempDir += File.separator;
    }

    File dir = new File(tempDir);
    dir.mkdirs();

    // Create the temporary file object.
    File file = new File(dir, "graphite.data");
    return file;
  }

  public void setEnableSend(Boolean enableSend) {
    this.enableSend = enableSend;
  }

  public Boolean getEnableSend() {
    return enableSend;
  }

}
