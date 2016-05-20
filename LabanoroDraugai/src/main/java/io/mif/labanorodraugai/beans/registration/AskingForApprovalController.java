/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.mif.labanorodraugai.beans.registration;

import io.mif.labanorodraugai.beans.AuthenticationBean;
import io.mif.labanorodraugai.entities.AccountApproval;
import io.mif.labanorodraugai.entities.Account;
import io.mif.labanorodraugai.entities.enums.AccountStatus;
import io.mif.labanorodraugai.services.EmailService;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Vytautas
 */

@RequestScoped 
@Stateful
@Named
public class AskingForApprovalController implements Serializable {
    
    private Account currentUser;
    
    private List<Account> approvedMembers;
    
    private List<AccountApproval> approvals;
    
    @PersistenceContext
    private EntityManager em;

    @Inject
    private AuthenticationBean authenticationBean;
    
    @Inject
    private EmailService emailService;
    
    @PostConstruct
    public void init(){
        this.currentUser = authenticationBean.getLoggedAccount();
        this.approvedMembers = em.createNamedQuery("Account.findByStatus")
                                .setParameter("status", AccountStatus.Member)
                                .getResultList();
        
        this.approvals = em.createNamedQuery("AccountApproval.findByCandidateId")
                            .setParameter("candidateId", currentUser.getId())
                            .getResultList();

    }

    public AccountApproval getApprovalFromMember(Account member) {
        AccountApproval searchApproval = new AccountApproval();
        searchApproval.setApprover(member);
        searchApproval.setCandidate(currentUser);
        
        int foundIndex = this.approvals.indexOf(searchApproval);
                
        if (foundIndex == -1) {
            return null;
        }
        
        return approvals.get(foundIndex);
    }
    
    public int getSentApprovalCount() {
        return approvals.size();
    }
    
    public void sendApprovalRequest(Account member) {
        
        AccountApproval newApproval = new AccountApproval(currentUser.getId(), member.getId());
        newApproval.setApprover(member);
        newApproval.setCandidate(currentUser);
        newApproval.setGeneratedId(UUID.randomUUID().toString());
        
        em.persist(newApproval);
        
        try {
            emailService.sendApprovalRequestEmail(newApproval);
            init();
        } catch (Exception ex) {
            Logger.getLogger(AskingForApprovalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * @return the currentUser
     */
    public Account getCurrentUser() {
        return currentUser;
    }

    /**
     * @param currentUser the currentUser to set
     */
    public void setCurrentUser(Account currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * @return the approvedMembers
     */
    public List<Account> getApprovedMembers() {
        return approvedMembers;
    }

    /**
     * @param approvedMembers the approvedMembers to set
     */
    public void setApprovedMembers(List<Account> approvedMembers) {
        this.approvedMembers = approvedMembers;
    }

}
