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
