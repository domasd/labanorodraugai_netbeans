/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.mif.labanorodraugai.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Vytautas
 */
@Embeddable
public class AccountApprovalPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "CandidateId")
    private int candidateId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ApproverId")
    private int approverId;

    public AccountApprovalPK() {
    }

    public AccountApprovalPK(int candidateId, int approverId) {
        this.candidateId = candidateId;
        this.approverId = approverId;
    }

    public int getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(int candidateId) {
        this.candidateId = candidateId;
    }

    public int getApproverId() {
        return approverId;
    }

    public void setApproverId(int approverId) {
        this.approverId = approverId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) candidateId;
        hash += (int) approverId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AccountApprovalPK)) {
            return false;
        }
        AccountApprovalPK other = (AccountApprovalPK) object;
        if (this.candidateId != other.candidateId) {
            return false;
        }
        if (this.approverId != other.approverId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "io.mif.labanorodraugai.beans.AccountApprovalPK[ candidateId=" + candidateId + ", approverId=" + approverId + " ]";
    }
    
}
