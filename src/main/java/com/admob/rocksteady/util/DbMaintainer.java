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

import com.admob.rocksteady.domain.*;

import java.io.*;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DbMaintainer{
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
	 * 
	 */
	public void purgeRecords() {
		logger.info("Purging old records");
		Threshold t = new Threshold();
		logger.info("Deleted " + t.deleteOldRecord(purgeThreshold) + " thresholds.");
		Revision r = new Revision();
		logger.info("Deleted " + r.deleteOldRecord(purgeRevision) + " revisions.");
	}
}
