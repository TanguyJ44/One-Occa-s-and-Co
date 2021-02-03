package fr.oneoccas.beans;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@ManagedBean(name="validate")
@RequestScoped
public class ValidateBeans {
    
    FacesContext context = null;
    ExternalContext externalContext = null;

    public ValidateBeans() {
        context = FacesContext.getCurrentInstance();
        externalContext = context.getExternalContext();
    }
    
    public void onLoad()
    {  
        if (!externalContext.getSessionMap().containsKey("user-id")) {
            try {    
                externalContext.redirect("login.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(LoginBeans.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
