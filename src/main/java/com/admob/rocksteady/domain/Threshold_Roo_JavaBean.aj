package com.admob.rocksteady.domain;

import java.lang.String;
import java.util.Date;

privileged aspect Threshold_Roo_JavaBean {
    
    public String Threshold.getName() {    
        return this.name;        
    }    
    
    public void Threshold.setName(String name) {    
        this.name = name;        
    }    
    
    public String Threshold.getHostname() {    
        return this.hostname;        
    }    
    
    public void Threshold.setHostname(String hostname) {    
        this.hostname = hostname;        
    }    
    
    public String Threshold.getColo() {    
        return this.colo;        
    }    
    
    public void Threshold.setColo(String colo) {    
        this.colo = colo;        
    }    
    
    public String Threshold.getGraphiteValue() {    
        return this.graphiteValue;        
    }    
    
    public void Threshold.setGraphiteValue(String graphiteValue) {    
        this.graphiteValue = graphiteValue;        
    }    
    
    public String Threshold.getGraphiteTs() {    
        return this.graphiteTs;        
    }    
    
    public void Threshold.setGraphiteTs(String graphiteTs) {    
        this.graphiteTs = graphiteTs;        
    }    
    
    public Date Threshold.getCreateOn() {    
        return this.createOn;        
    }    
    
    public void Threshold.setCreateOn(Date createOn) {    
        this.createOn = createOn;        
    }    
    
    public String Threshold.getApp() {    
        return this.app;        
    }    
    
    public void Threshold.setApp(String app) {    
        this.app = app;        
    }    
    
}
