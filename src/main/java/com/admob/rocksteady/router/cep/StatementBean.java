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