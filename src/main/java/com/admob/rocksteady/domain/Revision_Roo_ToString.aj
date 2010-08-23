package com.admob.rocksteady.domain;

import java.lang.String;

privileged aspect Revision_Roo_ToString {
    
    public String Revision.toString() {    
        StringBuilder sb = new StringBuilder();        
        sb.append("Id: ").append(getId()).append(", ");        
        sb.append("Version: ").append(getVersion()).append(", ");        
        sb.append("Colo: ").append(getColo()).append(", ");        
        sb.append("Hostname: ").append(getHostname()).append(", ");        
        sb.append("App: ").append(getApp()).append(", ");        
        sb.append("CreateOn: ").append(getCreateOn()).append(", ");        
        sb.append("OldRevision: ").append(getOldRevision()).append(", ");        
        sb.append("NewRevision: ").append(getNewRevision()).append(", ");        
        sb.append("Revision: ").append(getRevision());        
        return sb.toString();        
    }    
    
}
