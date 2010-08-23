package com.admob.rocksteady.domain;

import java.lang.String;

privileged aspect Alert_Roo_ToString {
    
    public String Alert.toString() {    
        StringBuilder sb = new StringBuilder();        
        sb.append("Id: ").append(getId()).append(", ");        
        sb.append("Version: ").append(getVersion()).append(", ");        
        sb.append("Message: ").append(getMessage()).append(", ");        
        sb.append("Type: ").append(getType()).append(", ");        
        sb.append("Status: ").append(getStatus());        
        return sb.toString();        
    }    
    
}
