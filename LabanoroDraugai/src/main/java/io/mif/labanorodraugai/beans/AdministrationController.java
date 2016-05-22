/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.mif.labanorodraugai.beans;

import com.google.gson.Gson;
import io.mif.labanorodraugai.beans.util.JsfUtil;
import io.mif.labanorodraugai.beans.util.JsfUtil.PersistAction;
import io.mif.labanorodraugai.entities.Config;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author dziaudom
 */
@Named
@Stateful
@RequestScoped
public class AdministrationController {

    @PersistenceContext
    EntityManager em;

    @EJB
    AdministrationFacade administrationFacade;

    Config config;
    String newCustomRegistrationField;

    private Gson gson;

    @PostConstruct
    public void init() {
        config = em.createNamedQuery("Config.getConfig", Config.class)
                .getSingleResult();
    }

    public AdministrationController() {
        gson = new Gson();
    }

    public Config getConfig() {
        return config;
    }

    public List<String> getCustomRegistrationFields() {
        return gson.fromJson(getConfig().getCustomRegistrationFields(), List.class);
    }

    public void deleteCustomRegistrationField() {
        FacesContext context = FacesContext.getCurrentInstance();
        String field = context.getExternalContext().getRequestParameterMap().get("field");

        List<String> registrationFields = this.getCustomRegistrationFields();
        registrationFields.remove(field);
        String registrationFieldsJson = gson.toJson(registrationFields);

        config = getConfig();
        config.setCustomRegistrationFields(registrationFieldsJson);
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/AdministrationBundle").getString("ConfigurationUpdated"));
    }

    public void addCustomRegistrationField() {
        if (newCustomRegistrationField == null || newCustomRegistrationField.isEmpty()) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/CommonBundle").getString("RequiredField"));
            return;
        }

        List<String> registrationFields = this.getCustomRegistrationFields();
        registrationFields.add(newCustomRegistrationField);
        String registrationFieldsJson = gson.toJson(registrationFields);

        config = getConfig();
        config.setCustomRegistrationFields(registrationFieldsJson);
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/AdministrationBundle").getString("ConfigurationUpdated"));
        newCustomRegistrationField = null;
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/AdministrationBundle").getString("ConfigurationUpdated"));
    }

    private void persist(JsfUtil.PersistAction persistAction, String successMessage) {
        if (config != null) {
            try {
                if (persistAction != JsfUtil.PersistAction.DELETE) {
                    administrationFacade.edit(config);
                } else {
                    administrationFacade.remove(config);
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

    public String getNewCustomRegistrationField() {
        return newCustomRegistrationField;
    }

    public void setNewCustomRegistrationField(String newCustomRegistrationField) {
        this.newCustomRegistrationField = newCustomRegistrationField;
    }
}
