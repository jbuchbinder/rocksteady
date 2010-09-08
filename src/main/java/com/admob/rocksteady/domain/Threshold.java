/**
 * Copyright 2010 Google Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 **/
package com.admob.rocksteady.domain;

import javax.persistence.Entity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.admob.rocksteady.util.DbMaintainer;

@Entity
@RooJavaBean
@RooToString
@RooEntity
public class Threshold {
  private static final Logger logger = LoggerFactory.getLogger(Threshold.class);

  private String name;

  private String hostname;

  private String colo;

  private String graphiteValue;

  private String graphiteTs;

  @Temporal(TemporalType.TIMESTAMP)
  @DateTimeFormat(style = "S-")
  private Date createOn;

  private String app;

  @Transactional
  public Integer deleteOldRecord(Long cutOffTime) {

    long currentTime = new Date().getTime() / 1000; // seconds since midnight
                                                    // Jan 1, 1970
    String query =
        "delete from Threshold o where (" + currentTime + " - UNIX_TIMESTAMP(create_on))" + " > "
            + cutOffTime;
    logger.debug(query);
    return entityManager().createQuery(query).executeUpdate();

  }
}
