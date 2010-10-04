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
package com.admob.rocksteady.router.cep;



import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.espertech.esper.client.UpdateListener;
import com.espertech.esper.client.EPStatement;
import com.admob.rocksteady.domain.*;
import com.admob.rocksteady.event.*;


public class LoadHistoricData {

  private static final Logger logger = LoggerFactory.getLogger(LoadHistoricData.class);


  // Deploy deploy data into cep
  public static void loadDeploy() {

    // Invoke the method with JPA
    List<Revision> revisions = Revision.findRecentRevisions();

    // Go through it and generate deploy events to be send into CEP
    for (Revision revision : revisions) {
      Deploy d = new Deploy();
      d.setApp(revision.getApp());
      d.setColo(revision.getColo());
      d.setHostname(revision.getHostname());
      d.setRevision(new Double(revision.getRevision()));
      d.setTimestamp(new Double(revision.getTimestamp()));
      ComplexEventManager.getInstance().sendEvent(d);

    }
    // ComplexEventManager.getInstance().sendEvent(o);


  }

}
