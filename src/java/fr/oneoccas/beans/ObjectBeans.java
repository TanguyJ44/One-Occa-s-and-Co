package fr.oneoccas.beans;

import fr.oneoccas.mapping.Objects;
import fr.oneoccas.mapping.Types;
import fr.oneoccas.mapping.Users;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ManagedBean(name="object")
@RequestScoped
public class ObjectBeans {
    
    FacesContext context = null;
    ExternalContext externalContext = null;
    
    EntityManagerFactory entityManagerFactory = null;
    EntityManager entityManager = null;
    
    static Objects dataObject;

    public ObjectBeans() {
        context = FacesContext.getCurrentInstance();
        externalContext = context.getExternalContext();
        
        entityManagerFactory = Persistence.createEntityManagerFactory("OneOccasPU");
        entityManager = entityManagerFactory.createEntityManager();
    }
    
    public void onLoad() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        
        if (params.get("id") == null) {
            try {
                externalContext.redirect("index.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(ObjectBeans.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            List<Objects> findObject = entityManager.createQuery( "FROM Objects WHERE id = '" + params.get("id") + "'", Objects.class ).getResultList();
            if (findObject.size() == 1) {
                dataObject = findObject.get(0);
            } else {
                try {
                    externalContext.redirect("index.xhtml");
                } catch (IOException ex) {
                    Logger.getLogger(ObjectBeans.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public String getTypeName() {
        List<Types> findTypeName = entityManager.createQuery( "FROM Types WHERE id = '" + dataObject.getType() + "'", Types.class ).getResultList();
        return findTypeName.get(0).getName();
    }
    
    public Users getUserInfo() {
        List<Users> findUserName = entityManager.createQuery( "FROM Users WHERE id = '" + dataObject.getIdClient() + "'", Users.class ).getResultList();
        return findUserName.get(0);
    }

    public Objects getDataObject() {
        return dataObject;
    }

    public void setDataObject(Objects dataObject) {
        ObjectBeans.dataObject = dataObject;
    }

    public String getImage() {
        return "url_vers_image/" + dataObject.getImage();
    }

}
