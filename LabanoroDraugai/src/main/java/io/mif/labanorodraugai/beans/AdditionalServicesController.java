/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.mif.labanorodraugai.beans;

import io.mif.labanorodraugai.beans.util.JsfUtil;
import io.mif.labanorodraugai.entities.AdditionalServices;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author SFILON
 */
@Named
@ViewScoped
public class AdditionalServicesController implements Serializable  {
    
    private List<AdditionalServices> items;
    
    private List<AdditionalServices> selectedItems;   

    private AdditionalServices selected;
    
    @EJB
    private io.mif.labanorodraugai.beans.AdditionalServicesFacade ejbFacade;
    /**
     * @return the items
     */
    
    public AdditionalServices prepareCreate() {
        selected = new AdditionalServices();
        initializeEmbeddableKey();
        return selected;
       
    }
    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }
    
    public void destroy() {
        
        persist(JsfUtil.PersistAction.DELETE, ResourceBundle.getBundle("/AdditionalServicesBundle").getString("AdditionalServiceDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }
    
    public void create() {
        persist(JsfUtil.PersistAction.CREATE, ResourceBundle.getBundle("/AdditionalServicesBundle").getString("AdditionalServiceCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }
    
    public void update() {        
        persist(JsfUtil.PersistAction.UPDATE, ResourceBundle.getBundle("/AdditionalServicesBundle").getString("AdditionalServiceUpdated"));
    }
        
    private void persist(JsfUtil.PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != JsfUtil.PersistAction.DELETE) {
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
    
    public List<AdditionalServices> getItems() {
               
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    
    }
    /**
     * @param items the items to set
     */
    public void setItems(List<AdditionalServices> items) {
        this.items = items;
    }
    private AdditionalServicesFacade getFacade() {
        return ejbFacade;
    }

    /**
     * @return the selectedItems
     */
    public List<AdditionalServices> getSelectedItems() {
        return selectedItems;
    }

    /**
     * @param selectedItems the selectedItems to set
     */
    public void setSelectedItems(List<AdditionalServices> selectedItems) {
        this.selectedItems = selectedItems;
    }

    /**
     * @return the selected
     */
    public AdditionalServices getSelected() {
        return selected;
    }

    /**
     * @param selected the selected to set
     */
    public void setSelected(AdditionalServices selected) {
        this.selected = selected;
    }
    
    public AdditionalServices getAdditionalService(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<AdditionalServices> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<AdditionalServices> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }
    
    @FacesConverter(forClass = AdditionalServices.class)
    public static class AdditionalServicesControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AdditionalServicesController controller = (AdditionalServicesController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "additionalServicesController");
            return controller.getAdditionalService(getKey(value));
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
            if (object instanceof AdditionalServices) {
                AdditionalServices o = (AdditionalServices) object;
                return getStringKey(o.getServiceID());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), AdditionalServices.class.getName()});
                return null;
            }
        }

    }
}
