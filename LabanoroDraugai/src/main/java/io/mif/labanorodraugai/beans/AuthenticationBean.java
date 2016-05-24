/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.mif.labanorodraugai.beans;

import io.mif.labanorodraugai.entities.Account;
import io.mif.labanorodraugai.entities.enums.AccountStatus;
import io.mif.labanorodraugai.services.PasswordHashService;
import io.mif.labanorodraugai.utils.ConstantsBean;
import java.io.IOException;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author SFILON
 */
@Named("authenticationBean")
@SessionScoped
@Stateful
public class AuthenticationBean implements Serializable {

    @Inject
    PasswordHashService passwordHashService;

    @PersistenceContext
    private EntityManager em;

    private String loginParameter;
    private String password;
    private Account loggedAccount;
    private String stringToRedirect;

    public AuthenticationBean() {
    }

    public void userIsAuthorized() throws IOException {

        if (loggedAccount == null) {

            HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            stringToRedirect = ".." + req.getRequestURI().substring(req.getContextPath().length()) + "?faces-redirect=true";

            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect(FacesContext.getCurrentInstance()
                            .getExternalContext()
                            .getApplicationContextPath() + "/login/login.html");
        }
    }

    public void userIsNotAuthorized() throws IOException {

        if (loggedAccount != null) {
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.getExternalContext().redirect(fc.getExternalContext().getApplicationContextPath() + "/index.html");
        }

    }
    
    public void userIsAuthorizedAndCandidate() throws IOException{
        FacesContext fc = FacesContext.getCurrentInstance();
        
        if (loggedAccount==null){
            fc.getExternalContext().redirect(fc.getExternalContext().getApplicationContextPath()+"/login/login.html");
        } else if (loggedAccount.getStatus() != AccountStatus.Candidate){
            fc.getExternalContext().redirect(fc.getExternalContext().getApplicationContextPath()+"/index.html");
        }
    }
    
    public void userIsAuthorizedAndNotCandidate() throws IOException{
        FacesContext fc = FacesContext.getCurrentInstance();
        
        if (loggedAccount==null){
            fc.getExternalContext().redirect(fc.getExternalContext().getApplicationContextPath()+"/login/login.html");
        } else if (loggedAccount.getStatus() == AccountStatus.Candidate){
            fc.getExternalContext().redirect(fc.getExternalContext().getApplicationContextPath()+"/index.html");
        }
    }
    
    public boolean isAuthorizedAndCandidate() {
        return loggedAccount != null && loggedAccount.getStatus() == AccountStatus.Candidate;
    }
    
    public void userIsAuthorizedAndAdmin() throws IOException{
         FacesContext fc = FacesContext.getCurrentInstance();
        
        if (loggedAccount==null){
            fc.getExternalContext().redirect(fc.getExternalContext().getApplicationContextPath()+"/login/login.html");
        } else if (loggedAccount.getStatus() != AccountStatus.Admin){
            fc.getExternalContext().redirect(fc.getExternalContext().getApplicationContextPath()+"/index.html");
        }
    }
    
     public boolean isAuthorizedAndAdmin() {
        return loggedAccount != null && loggedAccount.getStatus() == AccountStatus.Admin;
    }
    
    public boolean isAuthorizedAndNotCandidate() {
        return loggedAccount != null && loggedAccount.getStatus() != AccountStatus.Candidate;
    }
    
    public String login() throws NoSuchAlgorithmException {

        String hashedPassword = passwordHashService.HashPassword(password);

        if (getLoginParameter().matches(ConstantsBean.REGEX_EMAIL)) {

            Query findAccount = em.createNamedQuery("Account.findByEmailAndPassword").setParameter("email", getLoginParameter()).setParameter("password", hashedPassword);

            if (findAccount.getResultList().size() > 0) {
                loggedAccount = (Account) findAccount.getSingleResult();

                if (stringToRedirect != null && !stringToRedirect.equals("..?faces-redirect=true")
                        && !stringToRedirect.equals("../?faces-redirect=true")) {
                    return stringToRedirect;
                } else {
                    return "../index.html?faces-redirect=true";
                }
            } else {
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/LoginBundle").getString("LoginErrorTitle"),
                        ResourceBundle.getBundle("/LoginBundle").getString("LoginErrorMessage")));

            }
        } else {

            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/LoginBundle").getString("LoginErrorTitle"),
                    ResourceBundle.getBundle("/LoginBundle").getString("LoginParameterMessage")));
        }

        return null;
    }
    
    public boolean canReserve(){
        
        Date currentDate = new Date();
        Date startDate = loggedAccount.getReservationGroup().getStartOfReservationDate();
        
        return currentDate.compareTo(startDate)>0;        		
    }

    public String logout() {
        loggedAccount = null;
        return "/login/login.html?faces-redirect=true";
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
