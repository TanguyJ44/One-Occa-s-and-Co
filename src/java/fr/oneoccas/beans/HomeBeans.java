package fr.oneoccas.beans;

import fr.oneoccas.mapping.Objects;
import fr.oneoccas.mapping.Types;
import fr.oneoccas.mapping.Users;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

@ManagedBean(name="home")
@RequestScoped
public class HomeBeans {
    
    FacesContext context = null;
    ExternalContext externalContext = null;
    
    EntityManagerFactory entityManagerFactory = null;
    EntityManager entityManager = null;
    EntityTransaction trans = null;
    
    private boolean userIsConnected = false;
    
    private List<Objects> searchObject;
    private boolean submit = false;
    private boolean viewMoreObject = false;
    
    private String selectedObject;
    private String search;
    private String price;
    private String zipcode;

    public HomeBeans() {
        context = FacesContext.getCurrentInstance();
        externalContext = context.getExternalContext();
        
        entityManagerFactory = Persistence.createEntityManagerFactory("OneOccasPU");
        entityManager = entityManagerFactory.createEntityManager();
        trans = entityManager.getTransaction();
        
        searchObject = new ArrayList<Objects>();
        
        if (externalContext.getSessionMap().containsKey("user-id")) {
            userIsConnected = true;
        }
    }
    
    public void onLoad() {
        if(!submit) {
            for (int i = 1; i < 6; i++) {
                searchObject.add(getObjects().get(getObjects().size()-i));
            }
        }
    }
    
    public void onSearch() {
        submit = true;
        
        searchObject.clear();
        
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(selectedObject);
        int selectedID = -1;
    
        while(matcher.find()) {
            selectedID = Integer.parseInt(matcher.group());
        }
        
        int pageIndex = 1;
        
        if (externalContext.getSessionMap().containsKey("page-index")) {
            pageIndex = (int) externalContext.getSessionMap().get("page-index");
        }
        
        String request = "";
        
        if (!search.isEmpty()) {
            request += " AND titre LIKE '%" + search + "%'";
        }
        if (!price.isEmpty()) {
            request += " AND prix LIKE '%" + price + "%'";
        }
        
        searchObject = entityManager.createQuery("from Objects WHERE type = " + selectedID + request + " ORDER BY id DESC", 
                Objects.class).setMaxResults(10 * pageIndex).getResultList();
                
        Users user = null;
        if (!zipcode.isEmpty()) {
            for (Objects object : searchObject) {
                user = entityManager.createQuery("from Users WHERE id = " + object.getIdClient(), 
                        Users.class).getResultList().get(0);
                if (!user.getZipcode().equalsIgnoreCase(zipcode))
                    searchObject.remove(object);
            }
        }
        
        if (searchObject.size() == 10) {
            viewMoreObject = true;
        }

    }
    
    public void onViewMoreObject() {
        if (!externalContext.getSessionMap().containsKey("page-index")) {
            externalContext.getSessionMap().put("page-index", 2);
        } else {
            int currentPageIndex = (int) externalContext.getSessionMap().get("page-index");
            externalContext.getSessionMap().put("page-index", currentPageIndex + 1);
        }
        
        onSearch();
    }
    
    public List<Users> getUsers() {
        return entityManager.createQuery("from Users", Users.class).getResultList();
    }
    
    public List<Objects> getObjects() {
        return entityManager.createQuery("from Objects", Objects.class).getResultList();
    }

    public List<Types> getTypes() {
        return entityManager.createQuery("from Types", Types.class).getResultList();
    }

    public boolean isUserIsConnected() {
        return userIsConnected;
    }

    public void setUserIsConnected(boolean userIsConnected) {
        this.userIsConnected = userIsConnected;
    }

    public List<Objects> getSearchObject() {
        return searchObject;
    }

    public boolean isViewMoreObject() {
        return viewMoreObject;
    }

    public void setViewMoreObject(boolean viewMoreObject) {
        this.viewMoreObject = viewMoreObject;
    }

    public void setSearchObject(List<Objects> searchObject) {
        this.searchObject = searchObject;
    }

    public String getSelectedObject() {
        return selectedObject;
    }

    public void setSelectedObject(String selectedObject) {
        this.selectedObject = selectedObject;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    
    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
    
}
