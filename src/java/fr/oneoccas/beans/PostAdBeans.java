package fr.oneoccas.beans;

import fr.oneoccas.mapping.Objects;
import fr.oneoccas.mapping.Types;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.servlet.http.Part;

@ManagedBean(name="post")
@SessionScoped
public class PostAdBeans {
    
    FacesContext context = null;
    ExternalContext externalContext = null;
    
    EntityManagerFactory entityManagerFactory = null;
    EntityManager entityManager = null;
    EntityTransaction trans = null;
    
    private String error_message;
    private Color error_color;
    
    private Part file;
    private String imageName;
    private String successUploadMessage;
    
    private String selectedType;
    private int userID;
    
    private String title;
    private String description;
    private String price;
    
    private boolean fileIsUpload = false;
    
    enum Color {
        RED,
        ORANGE,
        GREEN
    }

    public PostAdBeans() {
        context = FacesContext.getCurrentInstance();
        externalContext = context.getExternalContext();
        
        entityManagerFactory = Persistence.createEntityManagerFactory("OneOccasPU");
        entityManager = entityManagerFactory.createEntityManager();
        trans = entityManager.getTransaction();
    }
    
    public void onLoad() {  
        if (!externalContext.getSessionMap().containsKey("user-id")) {
            try {    
                externalContext.redirect("login.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(LoginBeans.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            userID = Integer.parseInt(externalContext.getSessionMap().get("user-id").toString());
        }
    }
    
    public void postNewObject() {
        if (fileIsUpload) {          
            Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(selectedType);
            int selectedID = -1;

            while(matcher.find()) {
                selectedID = Integer.parseInt(matcher.group());
            }
            
            trans.begin();
            Objects newObjects = new Objects(userID, selectedID, imageName, title, description, Double.parseDouble(price), new Date());
            entityManager.persist(newObjects);
            trans.commit();
            
            try {
                externalContext.redirect("validate.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(PostAdBeans.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            error_color = Color.RED;
            error_message = "Une erreur est survenue !";
        }
    }
    
    public void uploadImage() {
        String fileName = Paths.get(file.getSubmittedFileName()).getFileName().toString();
        
        fileIsUpload = false;
        
        if (fileName.contains(".png")) {
            imageName = "" + System.currentTimeMillis() + ".png";
        } else if (fileName.contains(".jpg")) {
            imageName = "" + System.currentTimeMillis() + ".jpg";
        } else {
            fileIsUpload = false;
            error_color = Color.RED;
            error_message = "L'image envoyé n'est pas dans le bon format !";
            return;
        }
            
        try (InputStream input = file.getInputStream()) {
            Files.copy(input, new File("C:\\Users\\Tanguy\\Documents\\upload\\", imageName).toPath());
        }
        catch (IOException e) {
            fileIsUpload = false;
            error_color = Color.RED;
            error_message = "Un problème est survenue lors de l'envoie de votre image !";
        } finally {
            fileIsUpload = true;
            successUploadMessage = "Votre image " + fileName + " à bien été enregistré !";
        }
    }
    
    public void validate(FacesContext context, UIComponent component, Object value) {
        Part file = (Part) value;
        /*if (file.getSize() > 11) {
            throw new ValidatorException(new FacesMessage("File is too large"));
        }*/
        /*if (!file.getContentType().equals("text/plain")) 
            throw new ValidatorException(new FacesMessage("File is not a text file"));*/
    }
    
    public List<Types> getTypes() {
        return entityManager.createQuery("from Types", Types.class).getResultList();
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
    
    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }

    public String getSuccessUploadMessage() {
        return successUploadMessage;
    }

    public void setSuccessUploadMessage(String successUploadMessage) {
        this.successUploadMessage = successUploadMessage;
    }

    public String getSelectedType() {
        return selectedType;
    }

    public void setSelectedType(String selectedType) {
        this.selectedType = selectedType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    
}
