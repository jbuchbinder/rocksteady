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
import java.util.List;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@RooJavaBean
@RooToString
@RooEntity
public class Revision {

  private String colo;

  private String hostname;

  private String app;

  @Temporal(TemporalType.TIMESTAMP)
  @DateTimeFormat(style = "S-")
  private Date createOn;

  private String oldRevision;

  private String newRevision;

  private String revision;

  @Transactional
  public Integer deleteOldRecord(Long cutOffTime) {

    long currentTime = new Date().getTime() / 1000; // seconds since midnight
                                                    // Jan 1, 1970
    String query =
        "delete from Revision o where (" + currentTime + " - UNIX_TIMESTAMP(create_on))" + " > "
            + cutOffTime;
    return entityManager().createQuery(query).executeUpdate();

  }

  public static List<Revision> findRecentRevisions() {
    String query = "select o from Revision o group by colo,hostname,app";
    return entityManager().createQuery(query).getResultList();
  }

  public Double getTimestamp() {
    return new Double(createOn.getTime() / 1000);
  }
}
