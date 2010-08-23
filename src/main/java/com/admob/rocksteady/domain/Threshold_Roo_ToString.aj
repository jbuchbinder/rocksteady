package com.admob.rocksteady.domain;

import java.lang.String;

privileged aspect Threshold_Roo_ToString {
    
    public String Threshold.toString() {    
        StringBuilder sb = new StringBuilder();        
        sb.append("Id: ").append(getId()).append(", ");        
        sb.append("Version: ").append(getVersion()).append(", ");        
        sb.append("Name: ").append(getName()).append(", ");        
        sb.append("Hostname: ").append(getHostname()).append(", ");        
        sb.append("Colo: ").append(getColo()).append(", ");        
        sb.append("GraphiteValue: ").append(getGraphiteValue()).append(", ");        
        sb.append("GraphiteTs: ").append(getGraphiteTs()).append(", ");        
        sb.append("CreateOn: ").append(getCreateOn()).append(", ");        
        sb.append("App: ").append(getApp());        
        return sb.toString();        
    }    
    
}
