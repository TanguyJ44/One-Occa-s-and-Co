package fr.oneoccas.mapping;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@Table(name = "Objects")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Objects.findAll", query = "SELECT o FROM Objects o")
    , @NamedQuery(name = "Objects.findById", query = "SELECT o FROM Objects o WHERE o.id = :id")
    , @NamedQuery(name = "Objects.findByIdClient", query = "SELECT o FROM Objects o WHERE o.idClient = :idClient")
    , @NamedQuery(name = "Objects.findByType", query = "SELECT o FROM Objects o WHERE o.type = :type")
    , @NamedQuery(name = "Objects.findByImage", query = "SELECT o FROM Objects o WHERE o.image = :image")
    , @NamedQuery(name = "Objects.findByTitre", query = "SELECT o FROM Objects o WHERE o.titre = :titre")
    , @NamedQuery(name = "Objects.findByPrix", query = "SELECT o FROM Objects o WHERE o.prix = :prix")
    , @NamedQuery(name = "Objects.findByDate", query = "SELECT o FROM Objects o WHERE o.date = :date")})
public class Objects implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "idClient")
    private int idClient;
    @Basic(optional = false)
    @Column(name = "type")
    private int type;
    @Basic(optional = false)
    @Column(name = "image")
    private String image;
    @Basic(optional = false)
    @Column(name = "titre")
    private String titre;
    @Basic(optional = false)
    @Lob
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Column(name = "prix")
    private double prix;
    @Basic(optional = false)
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    public Objects() {
    }

    public Objects(int idClient, int type, String image, String titre, String description, double prix, Date date) {
        this.idClient = idClient;
        this.type = type;
        this.image = image;
        this.titre = titre;
        this.description = description;
        this.prix = prix;
        this.date = date;
    }

    public Objects(Integer id) {
        this.id = id;
    }

    public Objects(Integer id, int idClient, int type, String image, String titre, String description, double prix, Date date) {
        this.id = id;
        this.idClient = idClient;
        this.type = type;
        this.image = image;
        this.titre = titre;
        this.description = description;
        this.prix = prix;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Objects)) {
            return false;
        }
        Objects other = (Objects) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "fr.oneoccas.mapping.Objects[ id=" + id + " ]";
    }
    
}
