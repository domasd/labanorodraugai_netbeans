/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.mif.labanorodraugai.beans;

import io.mif.labanorodraugai.beans.util.AccountUtil;
import io.mif.labanorodraugai.entities.Account;
import java.io.IOException;
import java.io.Serializable;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.primefaces.context.RequestContext;

/**
 *
 * @author SFILON
 */
@Named
@SessionScoped
@Stateful
public class AuthenticationBean implements Serializable {

    @PersistenceContext
    private EntityManager em;

    private String loginParameter;
    private String password;
    private Account loggedAccount;

    public AuthenticationBean() {
    }

    public void userIsAuthorized() throws IOException {

        if (loggedAccount == null) {
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.getExternalContext().redirect(fc.getExternalContext().getApplicationContextPath() + "/login/login.html");
        }

    }

    public void userIsNotAuthorized() throws IOException {

        if (loggedAccount != null) {
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.getExternalContext().redirect(fc.getExternalContext().getApplicationContextPath() + "/login/login.html");
        }

    }

    public String login() {
        // validation

        Query findAccount = null;
        String hashedPassword = null;

        try {
            AccountUtil au = new AccountUtil();
            hashedPassword = au.HashPassword(password);
        } catch (Exception e) {

        }
        
        if (getLoginParameter().matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {

            findAccount = em.createNamedQuery("Account.findByEmailAndPassword").setParameter("email", getLoginParameter()).setParameter("password", hashedPassword);
        } else {

            findAccount = em.createNamedQuery("Account.findByNameAndPassword").setParameter("name", getLoginParameter()).setParameter("password", hashedPassword);

        }

        if (findAccount.getResultList().size() > 0) {

            loggedAccount = (Account) findAccount.getSingleResult();
            return "../index.html?faces-redirect=true";
        } else {

            RequestContext.getCurrentInstance().update("growl");
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    ResourceBundle.getBundle("/Bundle").getString("LoginErrorTitle"),
                    ResourceBundle.getBundle("/Bundle").getString("LoginErrorMessage")));
        }

        return null;

    }

    public String logout() {
        loggedAccount = null;

        return "../login/login.html?faces-redirect=true";
    }

    public void redirectToIndex() {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.getExternalContext().redirect(fc.getExternalContext().getApplicationContextPath() + "/index.hmtl");

        } catch (Exception e) {

        }

    }

    public void redirectToLogout() {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.getExternalContext().redirect(fc.getExternalContext().getApplicationContextPath() + "/login/logout.hmtl");

        } catch (Exception e) {

        }

    }

    public void redirect(String page) {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.getExternalContext().redirect(fc.getExternalContext().getApplicationContextPath() + page);

        } catch (Exception e) {

        }

    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the loginParameter
     */
    public String getLoginParameter() {
        return loginParameter;
    }

    /**
     * @param loginParameter the loginParameter to set
     */
    public void setLoginParameter(String loginParameter) {
        this.loginParameter = loginParameter;
    }

    /**
     * @return the loggedAccount
     */
    public Account getLoggedAccount() {
        return loggedAccount;
    }

    /**
     * @param loggedAccount the loggedAccount to set
     */
    public void setLoggedAccount(Account loggedAccount) {
        this.loggedAccount = loggedAccount;
    }

}
