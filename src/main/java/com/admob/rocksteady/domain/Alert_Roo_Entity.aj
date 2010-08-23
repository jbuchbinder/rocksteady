package com.admob.rocksteady.domain;

import com.admob.rocksteady.domain.Alert;
import java.lang.Integer;
import java.lang.Long;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Version;
import org.springframework.transaction.annotation.Transactional;

privileged aspect Alert_Roo_Entity {
    
    @PersistenceContext    
    transient EntityManager Alert.entityManager;    
    
    @Id    
    @GeneratedValue(strategy = GenerationType.AUTO)    
    @Column(name = "id")    
    private Long Alert.id;    
    
    @Version    
    @Column(name = "version")    
    private Integer Alert.version;    
    
    public Long Alert.getId() {    
        return this.id;        
    }    
    
    public void Alert.setId(Long id) {    
        this.id = id;        
    }    
    
    public Integer Alert.getVersion() {    
        return this.version;        
    }    
    
    public void Alert.setVersion(Integer version) {    
        this.version = version;        
    }    
    
    @Transactional    
    public void Alert.persist() {    
        if (this.entityManager == null) this.entityManager = entityManager();        
        this.entityManager.persist(this);        
    }    
    
    @Transactional    
    public void Alert.remove() {    
        if (this.entityManager == null) this.entityManager = entityManager();        
        if (this.entityManager.contains(this)) {        
            this.entityManager.remove(this);            
        } else {        
            Alert attached = this.entityManager.find(Alert.class, this.id);            
            this.entityManager.remove(attached);            
        }        
    }    
    
    @Transactional    
    public void Alert.flush() {    
        if (this.entityManager == null) this.entityManager = entityManager();        
        this.entityManager.flush();        
    }    
    
    @Transactional    
    public void Alert.merge() {    
        if (this.entityManager == null) this.entityManager = entityManager();        
        Alert merged = this.entityManager.merge(this);        
        this.entityManager.flush();        
        this.id = merged.getId();        
    }    
    
    public static final EntityManager Alert.entityManager() {    
        EntityManager em = new Alert().entityManager;        
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");        
        return em;        
    }    
    
    public static long Alert.countAlerts() {    
        return (Long) entityManager().createQuery("select count(o) from Alert o").getSingleResult();        
    }    
    
    public static List<Alert> Alert.findAllAlerts() {    
        return entityManager().createQuery("select o from Alert o").getResultList();        
    }    
    
    public static Alert Alert.findAlert(Long id) {    
        if (id == null) throw new IllegalArgumentException("An identifier is required to retrieve an instance of Alert");        
        return entityManager().find(Alert.class, id);        
    }    
    
    public static List<Alert> Alert.findAlertEntries(int firstResult, int maxResults) {    
        return entityManager().createQuery("select o from Alert o").setFirstResult(firstResult).setMaxResults(maxResults).getResultList();        
    }    
    
}
