package com.admob.rocksteady.domain;

import com.admob.rocksteady.domain.Threshold;
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

privileged aspect Threshold_Roo_Entity {
    
    @PersistenceContext    
    transient EntityManager Threshold.entityManager;    
    
    @Id    
    @GeneratedValue(strategy = GenerationType.AUTO)    
    @Column(name = "id")    
    private Long Threshold.id;    
    
    @Version    
    @Column(name = "version")    
    private Integer Threshold.version;    
    
    public Long Threshold.getId() {    
        return this.id;        
    }    
    
    public void Threshold.setId(Long id) {    
        this.id = id;        
    }    
    
    public Integer Threshold.getVersion() {    
        return this.version;        
    }    
    
    public void Threshold.setVersion(Integer version) {    
        this.version = version;        
    }    
    
    @Transactional    
    public void Threshold.persist() {    
        if (this.entityManager == null) this.entityManager = entityManager();        
        this.entityManager.persist(this);        
    }    
    
    @Transactional    
    public void Threshold.remove() {    
        if (this.entityManager == null) this.entityManager = entityManager();        
        if (this.entityManager.contains(this)) {        
            this.entityManager.remove(this);            
        } else {        
            Threshold attached = this.entityManager.find(Threshold.class, this.id);            
            this.entityManager.remove(attached);            
        }        
    }    
    
    @Transactional    
    public void Threshold.flush() {    
        if (this.entityManager == null) this.entityManager = entityManager();        
        this.entityManager.flush();        
    }    
    
    @Transactional    
    public void Threshold.merge() {    
        if (this.entityManager == null) this.entityManager = entityManager();        
        Threshold merged = this.entityManager.merge(this);        
        this.entityManager.flush();        
        this.id = merged.getId();        
    }    
    
    public static final EntityManager Threshold.entityManager() {    
        EntityManager em = new Threshold().entityManager;        
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");        
        return em;        
    }    
    
    public static long Threshold.countThresholds() {    
        return (Long) entityManager().createQuery("select count(o) from Threshold o").getSingleResult();        
    }    
    
    public static List<Threshold> Threshold.findAllThresholds() {    
        return entityManager().createQuery("select o from Threshold o").getResultList();        
    }    
    
    public static Threshold Threshold.findThreshold(Long id) {    
        if (id == null) throw new IllegalArgumentException("An identifier is required to retrieve an instance of Threshold");        
        return entityManager().find(Threshold.class, id);        
    }    
    
    public static List<Threshold> Threshold.findThresholdEntries(int firstResult, int maxResults) {    
        return entityManager().createQuery("select o from Threshold o").setFirstResult(firstResult).setMaxResults(maxResults).getResultList();        
    }    
    
}
