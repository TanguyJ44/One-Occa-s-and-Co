package fr.oneoccas.beans;

import fr.oneoccas.mapping.Objects;
import fr.oneoccas.mapping.Users;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

@ManagedBean(name="dashboard")
@RequestScoped
public class DashboardBeans {
    
    FacesContext context = null;
    ExternalContext externalContext = null;
    
    EntityManagerFactory entityManagerFactory = null;
    EntityManager entityManager = null;
    EntityTransaction entityTransaction = null;
    
    private String error_message;
    private Color error_color;
    
    private String selectedObject;
    private int userID;
    
    private String user_username;
    private String user_email;
    private String user_lastname;
    private String user_firstname;
    private String user_zipcode;
    private String user_oldpassword;
    private String user_newpassword;
    private String user_secondnewpassword;
    
    enum Color {
        RED,
        ORANGE,
        GREEN
    }

    public DashboardBeans()
    {
        context = FacesContext.getCurrentInstance();
        externalContext = context.getExternalContext();
        
        entityManagerFactory = Persistence.createEntityManagerFactory("OneOccasPU");
        entityManager = entityManagerFactory.createEntityManager();
        entityTransaction = entityManager.getTransaction();
    }
    
    public void onLoad()
    {  
        if (!externalContext.getSessionMap().containsKey("user-id")) {
            try {    
                externalContext.redirect("login.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(LoginBeans.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            userID = Integer.parseInt(externalContext.getSessionMap().get("user-id").toString());
            
            user_username = getUsers().get(0).getUsername();
            user_email = getUsers().get(0).getEmail();
            user_lastname = getUsers().get(0).getLastname();
            user_firstname = getUsers().get(0).getFirstname();
            user_zipcode = getUsers().get(0).getZipcode();
        }
    }
    
    public void onDisconnect()
    {
        context = FacesContext.getCurrentInstance();
        externalContext = context.getExternalContext();
               
        try {    
            externalContext.getSessionMap().remove("user-id");
            externalContext.redirect("login.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(LoginBeans.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void onDeleteObject()
    {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(selectedObject);
        int selectedID = -1;
    
        while(matcher.find()) {
            selectedID = Integer.parseInt(matcher.group());
        }
        
        Objects deleteObject = null;
        for (Objects object : getObjects()) {
            if (object.getId() == selectedID)
                deleteObject = object;
        }
                
        entityTransaction.begin();
        entityManager.remove(deleteObject);
        entityTransaction.commit();
        
        error_color = Color.GREEN;
        error_message = "L'objet a bien été supprimé !";
    }
    
    public void onUpdateUserInfo()
    {
        if (user_newpassword.isEmpty() || !user_newpassword.isEmpty() && 
                user_newpassword.equalsIgnoreCase(user_secondnewpassword)) {
            if (getUsers().get(0).getPassword().equalsIgnoreCase(""+user_oldpassword.hashCode())) {
                Users updateUser = getUsers().get(0);
                updateUser.setFirstname(user_firstname);
                updateUser.setLastname(user_lastname);
                updateUser.setEmail(user_email);
                updateUser.setZipcode(user_zipcode);
                if (!user_newpassword.isEmpty()) {
                    updateUser.setPassword(""+user_newpassword.hashCode());
                }
                entityTransaction.begin();
                entityManager.persist(updateUser);
                entityTransaction.commit();
                
                error_color = Color.GREEN;
                error_message = "Les modifications ont bien été prise en compte !";
            } else {
                error_color = Color.RED;
                error_message = "Le mot de passe actuel est incorrect !";
            }
        } else {
            error_color = Color.RED;
            error_message = "Les deux nouveaux mot de passe ne correspondent pas !";
        }
        
    }
    
    public List<Objects> getObjects()
    {
        return entityManager.createQuery("from Objects WHERE idClient = " +
                getUsers().get(0).getId(), Objects.class).getResultList();
    }
    
    public List<Users> getUsers() {
        return entityManager.createQuery("from Users WHERE id = '" + 
                externalContext.getSessionMap().get("user-id") + "'", Users.class).getResultList();
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

    public Color getError_color() {
        return error_color;
    }

    public void setError_color(Color error_color) {
        this.error_color = error_color;
    }

    public String getSelectedObjects()
    {
        return selectedObject;
    }

    public void setSelectedObjects(String selectedObject)
    {
        this.selectedObject = selectedObject;
    }

    public String getUser_username() {
        return user_username;
    }

    public void setUser_username(String user_username) {
        this.user_username = user_username;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_lastname() {
        return user_lastname;
    }

    public void setUser_lastname(String user_lastname) {
        this.user_lastname = user_lastname;
    }

    public String getUser_firstname() {
        return user_firstname;
    }

    public void setUser_firstname(String user_firstname) {
        this.user_firstname = user_firstname;
    }

    public String getUser_zipcode() {
        return user_zipcode;
    }

    public void setUser_zipcode(String user_zipcode) {
        this.user_zipcode = user_zipcode;
    }

    public String getUser_oldpassword() {
        return user_oldpassword;
    }

    public void setUser_oldpassword(String user_oldpassword) {
        this.user_oldpassword = user_oldpassword;
    }

    public String getUser_newpassword() {
        return user_newpassword;
    }

    public void setUser_newpassword(String user_newpassword) {
        this.user_newpassword = user_newpassword;
    }

    public String getUser_secondnewpassword() {
        return user_secondnewpassword;
    }

    public void setUser_secondnewpassword(String user_secondnewpassword) {
        this.user_secondnewpassword = user_secondnewpassword;
    }
 
}
