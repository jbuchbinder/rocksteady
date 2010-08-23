package com.admob.rocksteady.domain;

import java.lang.String;
import java.util.Date;

privileged aspect Revision_Roo_JavaBean {
    
    public String Revision.getColo() {    
        return this.colo;        
    }    
    
    public void Revision.setColo(String colo) {    
        this.colo = colo;        
    }    
    
    public String Revision.getHostname() {    
        return this.hostname;        
    }    
    
    public void Revision.setHostname(String hostname) {    
        this.hostname = hostname;        
    }    
    
    public String Revision.getApp() {    
        return this.app;        
    }    
    
    public void Revision.setApp(String app) {    
        this.app = app;        
    }    
    
    public Date Revision.getCreateOn() {    
        return this.createOn;        
    }    
    
    public void Revision.setCreateOn(Date createOn) {    
        this.createOn = createOn;        
    }    
    
    public String Revision.getOldRevision() {    
        return this.oldRevision;        
    }    
    
    public void Revision.setOldRevision(String oldRevision) {    
        this.oldRevision = oldRevision;        
    }    
    
    public String Revision.getNewRevision() {    
        return this.newRevision;        
    }    
    
    public void Revision.setNewRevision(String newRevision) {    
        this.newRevision = newRevision;        
    }    
    
    public String Revision.getRevision() {    
        return this.revision;        
    }    
    
    public void Revision.setRevision(String revision) {    
        this.revision = revision;        
    }    
    
}
