/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.mif.labanorodraugai.beans;

import io.mif.labanorodraugai.beans.util.AccountUtil;
import java.io.Serializable;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.ejb.Stateful;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.primefaces.context.RequestContext;

/**
 *
 * @author SFILON
 */
@ManagedBean(name="authenticationBean")
@SessionScoped
@Stateful
public class SessionBean implements Serializable{
    
    @PersistenceContext
    private EntityManager em;
    
    private String loginParameter;
    private String password;
    public boolean isLogged=false;
    
    public SessionBean(){
        
    }
    
    public String action(){
        // validation
        
        Query findAccount=null;
        String hashedPassword=null;
        
        try{
            AccountUtil au = new AccountUtil();
            hashedPassword = au.HashPassword(password);
        }
        catch (Exception e){
            
        }
        
        if (getLoginParameter().matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
		+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")){
                         
            findAccount = em.createNamedQuery("Account.findByEmailAndPassword").setParameter("email", getLoginParameter()).setParameter("password",hashedPassword);         
        }
        else{    
            
            findAccount = em.createNamedQuery("Account.findByNameAndPassword").setParameter("name", getLoginParameter()).setParameter("password",hashedPassword);
            
        }
        
        if (findAccount.getResultList().size()>0)
        {
            isLogged=true;
            return "../index.html?faces-redirect=true";
        }
        
        else {
            
            /*FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Second Message", "Additional Message Detail"));
            */
            RequestContext.getCurrentInstance().update("growl");
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    ResourceBundle.getBundle("/Bundle").getString("LoginErrorTitle"),
                    ResourceBundle.getBundle("/Bundle").getString("LoginErrorMessage")));
        }
        
        return null;

    }
    
    public void redirectToIndex(){
        try{
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.getExternalContext().redirect(fc.getExternalContext().getApplicationContextPath()+"/index.hmtl");
            
        }
        catch(Exception e){
            
        }
        
    }
    
    public void redirectToLogout(){
        try{
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.getExternalContext().redirect(fc.getExternalContext().getApplicationContextPath()+"/login/logout.hmtl");
            
        }
        catch(Exception e){
            
        }
        
    }
    
    public void redirect(String page){
        try{
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.getExternalContext().redirect(fc.getExternalContext().getApplicationContextPath()+page);
            
        }
        catch(Exception e){
            
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
     * @return the isLogged
     */
    public boolean isIsLogged() {
        return isLogged;
    }

    /**
     * @param isLogged the isLogged to set
     */
    public void setIsLogged(boolean isLogged) {
        this.isLogged = isLogged;
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
    
    
}
