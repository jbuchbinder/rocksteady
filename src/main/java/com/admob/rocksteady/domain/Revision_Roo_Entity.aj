package com.admob.rocksteady.domain;

import com.admob.rocksteady.domain.Revision;
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

privileged aspect Revision_Roo_Entity {
    
    @PersistenceContext    
    transient EntityManager Revision.entityManager;    
    
    @Id    
    @GeneratedValue(strategy = GenerationType.AUTO)    
    @Column(name = "id")    
    private Long Revision.id;    
    
    @Version    
    @Column(name = "version")    
    private Integer Revision.version;    
    
    public Long Revision.getId() {    
        return this.id;        
    }    
    
    public void Revision.setId(Long id) {    
        this.id = id;        
    }    
    
    public Integer Revision.getVersion() {    
        return this.version;        
    }    
    
    public void Revision.setVersion(Integer version) {    
        this.version = version;        
    }    
    
    @Transactional    
    public void Revision.persist() {    
        if (this.entityManager == null) this.entityManager = entityManager();        
        this.entityManager.persist(this);        
    }    
    
    @Transactional    
    public void Revision.remove() {    
        if (this.entityManager == null) this.entityManager = entityManager();        
        if (this.entityManager.contains(this)) {        
            this.entityManager.remove(this);            
        } else {        
            Revision attached = this.entityManager.find(Revision.class, this.id);            
            this.entityManager.remove(attached);            
        }        
    }    
    
    @Transactional    
    public void Revision.flush() {    
        if (this.entityManager == null) this.entityManager = entityManager();        
        this.entityManager.flush();        
    }    
    
    @Transactional    
    public void Revision.merge() {    
        if (this.entityManager == null) this.entityManager = entityManager();        
        Revision merged = this.entityManager.merge(this);        
        this.entityManager.flush();        
        this.id = merged.getId();        
    }    
    
    public static final EntityManager Revision.entityManager() {    
        EntityManager em = new Revision().entityManager;        
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");        
        return em;        
    }    
    
    public static long Revision.countRevisions() {    
        return (Long) entityManager().createQuery("select count(o) from Revision o").getSingleResult();        
    }    
    
    public static List<Revision> Revision.findAllRevisions() {    
        return entityManager().createQuery("select o from Revision o").getResultList();        
    }    
    
    public static Revision Revision.findRevision(Long id) {    
        if (id == null) throw new IllegalArgumentException("An identifier is required to retrieve an instance of Revision");        
        return entityManager().find(Revision.class, id);        
    }    
    
    public static List<Revision> Revision.findRevisionEntries(int firstResult, int maxResults) {    
        return entityManager().createQuery("select o from Revision o").setFirstResult(firstResult).setMaxResults(maxResults).getResultList();        
    }    
    
}
