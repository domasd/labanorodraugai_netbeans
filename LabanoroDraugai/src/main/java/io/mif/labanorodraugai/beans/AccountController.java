package io.mif.labanorodraugai.beans;

import com.google.gson.Gson;
import static com.sun.javafx.logging.PulseLogger.addMessage;
import io.mif.labanorodraugai.entities.Account;
import io.mif.labanorodraugai.beans.util.JsfUtil;
import io.mif.labanorodraugai.beans.util.JsfUtil.PersistAction;
import io.mif.labanorodraugai.entities.SummerhouseReservation;
import io.mif.labanorodraugai.entities.enums.AccountStatus;
import io.mif.labanorodraugai.utils.CustomRegistrationField;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.event.PhaseId;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.apache.commons.io.IOUtils;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

@Named
@ViewScoped
public class AccountController implements Serializable {

    @EJB
    private io.mif.labanorodraugai.beans.AccountFacade ejbFacade;

    @Inject
    private ReservationController reservationController;
    
    private List<Account> items = null;
    private Account selected;
    private UploadedFile uploadedFile; // move to seperate service
    private Gson gson;

    public AccountController() {
        gson = new Gson();
    }

    public List<CustomRegistrationField> getCustomRegistrationFields() {
        if (selected != null) {
            return gson.fromJson(selected.getCustomRegistrationFields(), List.class);
        }
        return null;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    // TODO add error handling
    public void upload() throws IOException {
        InputStream inputstream = uploadedFile.getInputstream();
        byte[] contents = IOUtils.toByteArray(inputstream);
        selected.setImage(contents);
        this.update();
    }

    public List<SummerhouseReservation> getAllAccountReservations(){
        if (selected!=null) {
           return reservationController.getAccountAllReservations(selected);
        }
        else{
            return new ArrayList<>();
        }

    }
    public Account getSelected() {
        return selected;
    }

    public void setSelected(Account selected) {
        this.selected = selected;
    }

    public void onRowSelect(SelectEvent event) {
        addMessage(String.format("Selected account %s", (Account) event.getObject()));
    }

    private AccountFacade getFacade() {
        return ejbFacade;
    }

    public Account prepareCreate() {
        selected = new Account();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/CommonBundle").getString("AccountCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/AccountBundle").getString("AccountUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/CommonBundle").getString("SuccessfulyDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Account> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
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

    public Account getAccount(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Account> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Account> getItemsAvailableSelectOne() {
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
            String accountId = context.getExternalContext().getRequestParameterMap().get("accountId");
            Account account = this.getAccount(Integer.valueOf(accountId));
            byte[] imageBytes = account.getImage();
            return new DefaultStreamedContent(new ByteArrayInputStream(imageBytes));
        }
    }

    public List<SelectItem> getStatuses() {
        ArrayList<SelectItem> selectItems;
        selectItems = new ArrayList<>();
        for (AccountStatus status : AccountStatus.values()) {
            selectItems.add(new SelectItem(status,
                    ResourceBundle.getBundle("/AccountBundle")
                    .getString("Enum.AccountStatus." + status)));
        }

        return selectItems;
    }

    @FacesConverter(forClass = Account.class)
    public static class AccountControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AccountController controller = (AccountController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "accountController");
            return controller.getAccount(getKey(value));
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
            if (object instanceof Account) {
                Account o = (Account) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Account.class.getName()});
                return null;
            }
        }

    }

}
