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
