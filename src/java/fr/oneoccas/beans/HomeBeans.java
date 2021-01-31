package fr.oneoccas.beans;

import fr.oneoccas.mapping.Objects;
import fr.oneoccas.mapping.Types;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.persistence.AttributeConverter;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

@ManagedBean(name="home")
@RequestScoped
public class HomeBeans {
    
    private String search;
    private String zipcode;
    
    EntityManagerFactory entityManagerFactory = null;
    EntityManager entityManager = null;
    EntityTransaction trans = null;

    public HomeBeans() {
        entityManagerFactory = Persistence.createEntityManagerFactory("OneOccasPU");
        entityManager = entityManagerFactory.createEntityManager();
        trans = entityManager.getTransaction();
        
        /*trans.begin();
        Objects newObject = new Objects(0, 1, "image", "Oneplus 7T Pro", "Un super smartphone", 520, new Date());
        entityManager.persist(newObject);
        trans.commit();*/
    }
    
    public List<Objects> getObjects() {
        return entityManager.createQuery("from Objects", Objects.class).getResultList();
    }

    public List<Types> getTypes() {
        return entityManager.createQuery("from Types", Types.class).getResultList();
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
    
    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
    
}
