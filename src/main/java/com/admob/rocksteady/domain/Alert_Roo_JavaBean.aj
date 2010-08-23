package com.admob.rocksteady.domain;

import java.lang.Integer;
import java.lang.String;

privileged aspect Alert_Roo_JavaBean {
    
    public String Alert.getMessage() {    
        return this.message;        
    }    
    
    public void Alert.setMessage(String message) {    
        this.message = message;        
    }    
    
    public String Alert.getType() {    
        return this.type;        
    }    
    
    public void Alert.setType(String type) {    
        this.type = type;        
    }    
    
    public Integer Alert.getStatus() {    
        return this.status;        
    }    
    
    public void Alert.setStatus(Integer status) {    
        this.status = status;        
    }    
    
}
