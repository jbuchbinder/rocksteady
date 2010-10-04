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

import com.espertech.esper.client.UpdateListener;
import com.espertech.esper.client.EPStatement;

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * A wrapper class arround the Esper EPStatement which is managed by Spring.
 */
public class StatementBean {
  private static Pattern TOKEN_PATTERN = Pattern.compile("\\{([^}]*)\\}");

  private String epl;
  private String name;
  private List values = new ArrayList();
  private Set<UpdateListener> listeners = new LinkedHashSet<UpdateListener>();

  public StatementBean(String epl) {
    this.epl = epl;
  }

  public StatementBean(String epl, List values) {
    this.epl = epl;
    this.values = values;
  }

  @SuppressWarnings("unchecked")
  public StatementBean(String epl, int value) {
    this.epl = epl;
    this.values.add(value);
  }

  @SuppressWarnings("unchecked")
  public StatementBean(String epl, Map values, boolean replaceToken) {
    if (!replaceToken) {
      this.epl = epl;
      this.values.add(values);
      return;
    }
    StringBuffer sb = new StringBuffer();
    Matcher myMatcher = TOKEN_PATTERN.matcher(epl);
    while (myMatcher.find()) {
      String field = myMatcher.group(1);
      myMatcher.appendReplacement(sb, "");
      if (values.get(field) == null) {
        throw new IllegalStateException("Unable to find field value using key " + field);
      }
      sb.append(values.get(field));
    }
    myMatcher.appendTail(sb);
    this.epl = sb.toString();
  }

  public String getEPL() {
    return epl;
  }

  public void setListeners(UpdateListener... listeners) {
    for (UpdateListener listener : listeners) {
      addListener(listener);
    }
  }

  public void addListener(UpdateListener listener) {
    listeners.add(listener);
  }

  void setEPStatement(EPStatement epStatement) {
    for (UpdateListener listener : listeners) {
      epStatement.addListener(listener);
    }
  }


  public Set<UpdateListener> getAllListeners() {
    return listeners;
  }

  public List getValues() {
    return values;
  }

  public String getName() {
    return name == null ? toString() : name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
