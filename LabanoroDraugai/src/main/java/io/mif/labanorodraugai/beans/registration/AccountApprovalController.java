/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.mif.labanorodraugai.beans.registration;

import io.mif.labanorodraugai.beans.AdministrationController;
import io.mif.labanorodraugai.beans.AdministrationFacade;
import io.mif.labanorodraugai.beans.AuthenticationBean;
import io.mif.labanorodraugai.entities.AccountApproval;
import io.mif.labanorodraugai.entities.Account;
import io.mif.labanorodraugai.entities.Config;
import io.mif.labanorodraugai.entities.enums.AccountStatus;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Vytautas
 */
@Named
@Stateful
@ViewScoped
public class AccountApprovalController implements Serializable {

    private Account currentUser;

    private AccountApproval approval;

    @PersistenceContext
    private EntityManager em;

    @Inject
    private AuthenticationBean authenticationBean;

    Config config;

    @PostConstruct
    public void init() {
        this.currentUser = authenticationBean.getLoggedAccount();

        String generatedId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("gen");

        if (generatedId == null) {
            this.approval = null;
            return;
        }

        List<AccountApproval> approvals = em.createNamedQuery("AccountApproval.findByGeneratedId")
                .setParameter("generatedId", generatedId)
                .getResultList();

        if (approvals.isEmpty() || approvals.get(0).getAccountApprovalPK().getApproverId() != this.currentUser.getId()) {
            this.approval = null;
            return;
        }

        this.approval = approvals.get(0);
        
        config = em.createNamedQuery("Config.getConfig", Config.class)
                .getSingleResult();
    }

    public void approve() {
        this.approval.setApprovalDate(new Date());
        this.approval = em.merge(this.approval);

        List<AccountApproval> approvals = em.createNamedQuery("AccountApproval.findAllApprovedByCandidateId")
                .setParameter("candidateId", approval.getAccountApprovalPK().getCandidateId())
                .getResultList();

        config = em.createNamedQuery("Config.getConfig", Config.class)
                .getSingleResult();
        if (approvals.size() >= config.getMinApprovalsRequired()) {
            this.approval.getCandidate().setStatus(AccountStatus.Member);
            this.approval = em.merge(this.approval);
            //     em.persist(this.approval);
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
     * @return the approval
     */
    public AccountApproval getApproval() {
        return approval;
    }

    /**
     * @param approval the approval to set
     */
    public void setApproval(AccountApproval approval) {
        this.approval = approval;
    }
}
