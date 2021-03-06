/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.mif.labanorodraugai.beans.registration;

import io.mif.labanorodraugai.beans.AuthenticationBean;
import io.mif.labanorodraugai.beans.util.JsfUtil;
import io.mif.labanorodraugai.services.EmailService;
import java.util.ResourceBundle;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Vytautas
 */
@RequestScoped
@Named
public class InvitationController {

    private String email;
//    private String message;

    @PersistenceContext
    private EntityManager em;

    @Inject
    AuthenticationBean authenticationBean;
    
    @Inject
    EmailService emailService;
    
    public void invite() {
        Query existing = em
                .createNamedQuery("Account.findByEmail")
                .setParameter("email", email);

        if (existing.getResultList().size() > 0) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/AccountBundle").getString("EmailExistsError"));
            return;
        }
        
        try {
            emailService.SendInvitationEmail(authenticationBean.getLoggedAccount(), email);
            JsfUtil.addSuccessMessage("Pakvietimas " + email + " išsiųstas sėkmingai");
        } catch (Exception ex) {
            JsfUtil.addErrorMessage("Nusiųsti pakvietimo nepavyko. Bandykite vėliau");
        }
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

//    /**
//     * @return the message
//     */
//    public String getMessage() {
//        return message;
//    }
//
//    /**
//     * @param message the message to set
//     */
//    public void setMessage(String message) {
//        this.message = message;
//    }

}
