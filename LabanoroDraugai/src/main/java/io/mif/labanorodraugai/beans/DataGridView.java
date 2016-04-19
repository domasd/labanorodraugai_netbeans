
package io.mif.labanorodraugai.beans;

/**
 *
 * @author SFILON
 */
import io.mif.labanorodraugai.entities.Summerhouse;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.primefaces.component.graphicimage.GraphicImage;

 
@ManagedBean
@ViewScoped
public class DataGridView implements Serializable {
     
    @PersistenceContext
    private EntityManager em;
    
    private List<Summerhouse> houses;
     
    private Summerhouse selectedHouse;
    
    private GraphicImage image;
    
    @PostConstruct
    public void init() {
        Query query = em.createNamedQuery("Summerhouse.findAll");
        houses = query.getResultList();
        
        image = new GraphicImage();
        image.setUrl("http://www.summerhouses.me.uk/summerhouses14.jpg");
        image.setWidth("300");
        image.setHeight("200");
    }
 
    public List<Summerhouse> getHouses() {
        return houses;
    }
 
    public Summerhouse getSelectedHouse() {
        return selectedHouse;
    }
 
    public void setSelectedHouse(Summerhouse selectedHouse) {
        this.selectedHouse = selectedHouse;
    }

    /**
     * @return the image
     */
    public GraphicImage getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(GraphicImage image) {
        this.image = image;
    }
}
