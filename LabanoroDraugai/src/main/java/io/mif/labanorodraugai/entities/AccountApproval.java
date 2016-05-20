/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.mif.labanorodraugai.entities;

import io.mif.labanorodraugai.entities.Account;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Vytautas
 */
@Entity
@Table(name = "account_approval")
@NamedQueries({
    @NamedQuery(name = "AccountApproval.findAll", query = "SELECT a FROM AccountApproval a"),
    @NamedQuery(name = "AccountApproval.findByCandidateId", query = "SELECT a FROM AccountApproval a WHERE a.accountApprovalPK.candidateId = :candidateId"),
    @NamedQuery(name = "AccountApproval.findByApproverId", query = "SELECT a FROM AccountApproval a WHERE a.accountApprovalPK.approverId = :approverId"),
    @NamedQuery(name = "AccountApproval.findByApprovalDate", query = "SELECT a FROM AccountApproval a WHERE a.approvalDate = :approvalDate"),
    @NamedQuery(name = "AccountApproval.findByGeneratedId", query = "SELECT a FROM AccountApproval a WHERE a.generatedId = :generatedId"),
    @NamedQuery(name = "AccountApproval.findAllApprovedByCandidateId", query = "SELECT a FROM AccountApproval a WHERE a.accountApprovalPK.candidateId = :candidateId AND a.approvalDate IS NOT NULL")})
public class AccountApproval implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AccountApprovalPK accountApprovalPK;
    @Column(name = "ApprovalDate")
    @Temporal(TemporalType.DATE)
    private Date approvalDate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 36)
    @Column(name = "GeneratedId")
    private String generatedId;
    @JoinColumn(name = "ApproverId", referencedColumnName = "Id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Account approver;
    @JoinColumn(name = "CandidateId", referencedColumnName = "Id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Account candidate;

    public AccountApproval() {
    }

    public AccountApproval(AccountApprovalPK accountApprovalPK) {
        this.accountApprovalPK = accountApprovalPK;
    }

    public AccountApproval(AccountApprovalPK accountApprovalPK, String generatedId) {
        this.accountApprovalPK = accountApprovalPK;
        this.generatedId = generatedId;
    }

    public AccountApproval(int candidateId, int approverId) {
        this.accountApprovalPK = new AccountApprovalPK(candidateId, approverId);
    }

    public AccountApprovalPK getAccountApprovalPK() {
        return accountApprovalPK;
    }

    public void setAccountApprovalPK(AccountApprovalPK accountApprovalPK) {
        this.accountApprovalPK = accountApprovalPK;
    }

    public Date getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(Date approvalDate) {
        this.approvalDate = approvalDate;
    }

    public String getGeneratedId() {
        return generatedId;
    }

    public void setGeneratedId(String generatedId) {
        this.generatedId = generatedId;
    }

    public Account getApprover() {
        return approver;
    }

    public void setApprover(Account approver) {
        this.approver = approver;
    }

    public Account getCandidate() {
        return candidate;
    }

    public void setCandidate(Account candidate) {
        this.candidate = candidate;
    }

    @Override
    public int hashCode() {
        return approver.hashCode() + candidate.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AccountApproval)) {
            return false;
        }
        AccountApproval other = (AccountApproval) object;
        
        return this.approver.equals(other.approver) && this.candidate.equals(other.candidate);
    }

    @Override
    public String toString() {
        return "io.mif.labanorodraugai.beans.AccountApproval[ accountApprovalPK=" + accountApprovalPK + " ]";
    }
    
}
