/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.mif.labanorodraugai.beans;

import io.mif.labanorodraugai.beans.util.JsfUtil;
import io.mif.labanorodraugai.entities.Account;
import io.mif.labanorodraugai.entities.enums.AccountStatus;
import io.mif.labanorodraugai.services.PasswordHashService;
import java.math.BigDecimal;
import java.util.ResourceBundle;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Vies
 */
@Named("facebook")
@RequestScoped
@Stateful
public class FacebookBean {
    
    private Account account;

    @PersistenceContext
    private EntityManager em;

    @Inject
    private PasswordHashService paswordHashService;
    
    @PostConstruct
    public void init(){
        this.account = new Account();
        this.account.setPointsQuantity(BigDecimal.ZERO);
        this.account.setStatus(AccountStatus.Candidate);
    }
    
    public void listen(){
        String email = FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("email");
        String firstName = FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("first_name");
        String lastname = FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("last_name");
        System.out.println(email + " " + firstName + " " + lastname);
        
        this.account.setEmail(email);
        this.account.setName(firstName);
        this.account.setLastname(lastname);
        this.account.setPassword("asasas");
        
        
        Query existing = em
                .createNamedQuery("Account.findByEmail")
                .setParameter("email",this.account.getEmail());

        if (existing.getResultList().size() > 0) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/AccountBundle").getString("EmailExistsError"));
//            FacesContext.getCurrentInstance().addMessage(
//                    null, new FacesMessage(ResourceBundle.getBundle("/AccountBundle").getString("EmailExistsError"))
//            );
//            
            return;
        }
        
        String hashedPassword = paswordHashService.HashPassword(this.account.getPassword());
        this.account.setPassword(hashedPassword);
        
        em.persist(account);
        
       // return "../index.html?faces-redirect=true";
        
        
    }
    
    /**
     * @return the account
     */
    public Account getAccount() {
        return account;
    }

    /**
     * @param account the account to set
     */
    public void setAccount(Account account) {
        this.account = account;
    }
}
