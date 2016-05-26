/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.mif.labanorodraugai.beans;

import io.mif.labanorodraugai.beans.util.JsfUtil;
import io.mif.labanorodraugai.entities.Account;
import io.mif.labanorodraugai.entities.Config;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.primefaces.event.CloseEvent;

/**
 *
 * @author Mindaugas
 */
@Named
@ManagedBean
public class DialogView {

    @PersistenceContext
    EntityManager em;

    @Inject
    private AuthenticationBean authenticationBean;

    @Inject
    private AccountController accountController;

    public void extendMembership() {
        //addMessage("System Error", "Please try again later.");
        int accId = authenticationBean.getLoggedAccount().getId();
        Account currentAccount = em.createNamedQuery("Account.findById", Account.class)
                .setParameter("id", accId)
                .getSingleResult();
        //Account currentAccount = authenticationBean.getLoggedAccount();

        //EntityManager em = ejbFacade.getEntityManager();
        Config config = em.createNamedQuery("Config.getConfig", Config.class).getSingleResult();
        int price = config.getMembershipPrice();
        int seasonDays = config.getSeasonDays();

        // if account has not enough points return error
        if (currentAccount.pointsQuantity.intValue() < price) {
            JsfUtil.addErrorMessage("Klaida: Neužtenka taškų narystei pratęsti.");
            return;
        } //todo error message

        // if account membership valid return error
        if (currentAccount.isValid()) {
            JsfUtil.addErrorMessage("Klaida: Narystė dar galioja.");
            return;
        } //todo error message

        //if new member
        if (currentAccount.membershipValidUntil == null) {
            currentAccount.membershipValidUntil = new Date(new java.util.Date().getTime());
        };

        GregorianCalendar newDate = new GregorianCalendar();
        //currentAccount.membershipValidUntil.getYear(), currentAccount.membershipValidUntil.getMonth(), currentAccount.membershipValidUntil.getDay()
        newDate.setTime(new Date(new java.util.Date().getTime()));
        // Add days
        currentAccount.membershipValidUntil = new java.sql.Date(newDate.getTimeInMillis());
        newDate.add((GregorianCalendar.DATE), seasonDays);
        currentAccount.membershipValidUntil = new java.sql.Date(newDate.getTimeInMillis());

        //subtract points
        currentAccount.pointsQuantity = currentAccount.pointsQuantity.subtract(new BigDecimal(price));
        accountController.setSelected(currentAccount);
        accountController.update();

        return;
    }

    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public String getFormMessage() {
        Account currentAccount = authenticationBean.getLoggedAccount();
        Config config = em.createNamedQuery("Config.getConfig", Config.class).getSingleResult();
        String msg = config.getSeasonDays() + " dienų pratęsimas kainuoja " + config.getMembershipPrice() + " taškų (turimi taškai: " + currentAccount.getPointsQuantity() + "). Ar norite tęsti?";
        return msg;
    }

    public boolean isValid() {
        Account currentAccount = authenticationBean.getLoggedAccount();
        return currentAccount.isValid();
    }
}
