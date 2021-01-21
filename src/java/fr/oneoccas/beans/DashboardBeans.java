package fr.oneoccas.beans;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@ManagedBean(name="dashboard")
@RequestScoped
public class DashboardBeans {

    public DashboardBeans() {

    }
    
    public void onLoad() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        
        if (externalContext.getSessionMap().get("user-connected") == "false") {
            try {    
                externalContext.redirect("login.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(LoginBeans.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void onDisconnect() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
               
        try {    
            externalContext.getSessionMap().replace("user-connected", "false");
            externalContext.getSessionMap().remove("user-email");
            externalContext.getSessionMap().remove("user-username");
            externalContext.redirect("login.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(LoginBeans.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
