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

import com.admob.rocksteady.domain.*;

import java.io.*;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DbMaintainer {
  private static final Logger logger = LoggerFactory.getLogger(DbMaintainer.class);

  private Long purgeThreshold;
  private Long purgeRevision;


  public Long getPurgeThreshold() {
    return purgeThreshold;
  }

  public void setPurgeThreshold(Long purgeThreshold) {
    this.purgeThreshold = purgeThreshold;
  }

  public Long getPurgeRevision() {
    return purgeRevision;
  }

  public void setPurgeRevision(Long purgeRevision) {
    this.purgeRevision = purgeRevision;
  }

  /*
   * Null constructor
   */
  public DbMaintainer() {

  }

  public void initialize() {
    logger.info("Initialized DB Maintainer");
  }

  public void shutdown() {

  }

  /*
   * Purging old records
   */
  public void purgeRecords() {
    logger.info("Purging old records");
    Threshold t = new Threshold();
    logger.info("Deleted " + t.deleteOldRecord(purgeThreshold) + " thresholds.");
    Revision r = new Revision();
    logger.info("Deleted " + r.deleteOldRecord(purgeRevision) + " revisions.");
  }
}
