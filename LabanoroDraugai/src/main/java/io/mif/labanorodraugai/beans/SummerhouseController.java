package io.mif.labanorodraugai.beans;

import static com.sun.javafx.logging.PulseLogger.addMessage;
import io.mif.labanorodraugai.beans.util.JsfUtil;
import io.mif.labanorodraugai.beans.util.JsfUtil.PersistAction;
import io.mif.labanorodraugai.entities.Summerhouse;
import io.mif.labanorodraugai.entities.SummerhouseReservation;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.persistence.PersistenceException;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.event.PhaseId;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.apache.commons.io.IOUtils;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

@Named("summerhouseController")
@SessionScoped
public class SummerhouseController implements Serializable {

    @EJB
    private io.mif.labanorodraugai.beans.SummerhouseFacade ejbFacade;
    private List<Summerhouse> items = null;
    private Summerhouse selected;
    private UploadedFile uploadedFile; //move to seperate service
    
    private org.primefaces.component.calendar.Calendar calendar;
    
    private String calendarReservedDates;
    
    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    // TODO add error handling
    public void upload() throws IOException{
        System.out.println("update invoked");
        String fileName = uploadedFile.getFileName();
        String contentType = uploadedFile.getContentType();    
        InputStream inputstream = uploadedFile.getInputstream();
        byte[] contents = IOUtils.toByteArray(inputstream);
        selected.setImage(contents);
        this.update();
    }
    
    public SummerhouseController() {
        
    }
    
    public String generateDisabledDates(){
          
        if (selected==null || selected.getId()==null)
            return "[];";
                       
        List<SummerhouseReservation> records =  ejbFacade.summerhouseReservationList(selected.getId());
        
        List<String> disabledDates = new ArrayList<>();
                       
        SimpleDateFormat dateFormat = new SimpleDateFormat("M-d-yyyy");
        
        for(SummerhouseReservation record:records){
                        
            Date begin = record.getSummerhouseReservationPK().getBeginDate();
            Date end = record.getSummerhouseReservationPK().getEndDate();

            disabledDates.add(dateFormat.format(begin));
            
            Calendar cal = Calendar.getInstance();
            cal.setTime(begin);
            
            while (cal.getTime().before(end)) {
                 cal.add(Calendar.DATE, 1);
                 String formatted = dateFormat.format(cal.getTime());
                 disabledDates.add(formatted);
            } 
        }
        
        if (records.isEmpty()) 
            return "[];";
        
        String resultString="["; 
        
        for(String date:disabledDates){
            resultString+="\"";
            resultString+=date;
            resultString+="\"";
            resultString+=",";
        }
        
        return resultString.substring(0, resultString.lastIndexOf(","))+"];";
    }

    public Summerhouse getSelected() {
        return selected;
    }

    public void setSelected(Summerhouse selected) {
        this.selected = selected;
    }
    
    public void onRowSelect(SelectEvent event) {
        addMessage(String.format("Selected summerhouse %s", (Summerhouse) event.getObject()));
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private SummerhouseFacade getFacade() {
        return ejbFacade;
    }

    public Summerhouse prepareCreate() {
        selected = new Summerhouse();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/SummerhouseBundle").getString("SummerhouseCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
        selected=null;
    }

    public void update() {        
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/SummerhouseBundle").getString("SummerhouseUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/SummerhouseBundle").getString("SummerhouseDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Summerhouse> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected); 
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/CommonBundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/CommonBundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public Summerhouse getSummerhouse(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Summerhouse> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Summerhouse> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    // TODO move to another class - proabably another table and write service
    public StreamedContent getImage() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the HTML. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        } else {
            // So, browser is requesting the image. Return a real StreamedContent with the image bytes.
            String summerhouseId = context.getExternalContext().getRequestParameterMap().get("summerhouseId");
            Summerhouse summerhouse = this.getSummerhouse(Integer.valueOf(summerhouseId));
            byte[] imageBytes = summerhouse.getImage();
            return new DefaultStreamedContent(new ByteArrayInputStream(imageBytes));
        }
    }

    /**
     * @return the calendarReservedDates
     */
    public String getCalendarReservedDates() {
        if (calendarReservedDates==null) calendarReservedDates=generateDisabledDates();
        return calendarReservedDates;
    }

    /**
     * @param calendarReservedDates the calendarReservedDates to set
     */
    public void setCalendarReservedDates(String calendarReservedDates) {
        this.calendarReservedDates = calendarReservedDates;
    }

    /**
     * @return the calendar
     */
    public org.primefaces.component.calendar.Calendar getCalendar() {
        
        return new org.primefaces.component.calendar.Calendar();
    }

    /**
     * @param calendar the calendar to set
     */
    public void setCalendar(org.primefaces.component.calendar.Calendar calendar) {
        this.calendar = calendar;
        
    }

    @FacesConverter(forClass = Summerhouse.class)
    public static class SummerhouseControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            SummerhouseController controller = (SummerhouseController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "summerhouseController");
            return controller.getSummerhouse(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Summerhouse) {
                Summerhouse o = (Summerhouse) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Summerhouse.class.getName()});
                return null;
            }
        }

    }

}
