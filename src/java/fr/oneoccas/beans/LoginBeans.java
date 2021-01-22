package fr.oneoccas.beans;

import fr.oneoccas.mapping.Users;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

@ManagedBean(name="login")
@RequestScoped

public class LoginBeans {

    private String firstname;
    private String lastname;
    private String username;
    private int zipcode;
    private String email;
    private String password;
    private String secondpassword;
    private String error;
    
    FacesContext context = null;
    ExternalContext externalContext = null;
    
    EntityManagerFactory entityManagerFactory = null;
    EntityManager entityManager = null;
    EntityTransaction trans = null;
    
    public LoginBeans() {
        context = FacesContext.getCurrentInstance();
        externalContext = context.getExternalContext();
        
        entityManagerFactory = Persistence.createEntityManagerFactory("OneOccasPU");
        entityManager = entityManagerFactory.createEntityManager();
        trans = entityManager.getTransaction();
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getZipcode() {
        return zipcode;
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecondpassword() {
        return secondpassword;
    }

    public void setSecondpassword(String secondpassword) {
        this.secondpassword = secondpassword;
    }
    
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
    
    public void onLogin() {
        List<Users> user = entityManager.createQuery( "FROM Users WHERE email = '" + getEmail() + "'", Users.class ).getResultList();
        
        if (user.size() > 0) {
            if (user.get(0).getPassword().equalsIgnoreCase(""+getPassword().hashCode())) {
                try {    
                    externalContext.getSessionMap().put("user-connected", "true");
                    externalContext.getSessionMap().put("user-email", getEmail());
                    externalContext.getSessionMap().put("user-username", user.get(0).getUsername());
                    externalContext.redirect("dashboard.xhtml");
                } catch (IOException ex) {
                    Logger.getLogger(LoginBeans.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                // bad user password
                setError("Email ou Mot de passe invalide !");
            }
        } else {
            // user not found
            setError("Email ou Mot de passe invalide !");
        }
    }
    
    public void onRegister() {
        List<Users> user = entityManager.createQuery( "FROM Users WHERE email = '" + getEmail() + "'", Users.class ).getResultList();
        
        if (user.size() == 0) {
            user.clear();
            user = entityManager.createQuery( "FROM Users WHERE username = '" + getUsername() + "'", Users.class ).getResultList();
            
            if (user.size() == 0) {
                
                if (getPassword().equalsIgnoreCase(getSecondpassword())) {
                    trans.begin();
                    Users newUser = new Users(getFirstname(), getLastname(), getEmail(), getZipcode(), getUsername(), ""+getPassword().hashCode());
                    entityManager.persist(newUser);
                    trans.commit();
                    
                    try {    
                        externalContext.getSessionMap().put("user-connected", "true");
                        externalContext.getSessionMap().put("user-email", getEmail());
                        externalContext.getSessionMap().put("user-username", getUsername());
                        externalContext.redirect("dashboard.xhtml");
                    } catch (IOException ex) {
                        Logger.getLogger(LoginBeans.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    // password not match
                    setError("Les deux mots de passe renseignés ne sont pas identiques !");
                }
                
            } else {
                // username exist
                setError("Le pseudo renseignée est déjà associé à un compte existant !");
            }
        } else {
            // email exist
            setError("L'email renseignée est déjà associé à un compte existant !");
        }
    }
    
}
