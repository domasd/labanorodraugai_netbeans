package io.mif.labanorodraugai.beans.registration;

import io.mif.labanorodraugai.beans.util.JsfUtil;
import io.mif.labanorodraugai.entities.Account;
import io.mif.labanorodraugai.entities.enums.AccountStatus;
import io.mif.labanorodraugai.services.PasswordHashService;
import java.math.BigDecimal;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
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
@Stateful
@Named
public class RegistrationController {
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
    
    public String registerAccount() {
        Query existing = em
                .createNamedQuery("Account.findByEmail")
                .setParameter("email",this.account.getEmail());

        if (existing.getResultList().size() > 0) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/AccountBundle").getString("EmailExistsError"));         
            return null;
        }
        
        String hashedPassword = paswordHashService.HashPassword(this.account.getPassword());
        this.account.setPassword(hashedPassword);
        
        em.persist(account);
                
        return "/askingForApproval.html?faces-redirect=true";
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
