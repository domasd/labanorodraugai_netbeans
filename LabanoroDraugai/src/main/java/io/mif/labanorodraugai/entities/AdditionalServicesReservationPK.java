/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.mif.labanorodraugai.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author SFILON
 */
@Embeddable
public class AdditionalServicesReservationPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "ServiceID")
    private int serviceID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "AccountID")
    private int accountID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "BeginDate")
    @Temporal(TemporalType.DATE)
    private Date beginDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "EndDate")
    @Temporal(TemporalType.DATE)
    private Date endDate;

    public AdditionalServicesReservationPK() {
    }

    public AdditionalServicesReservationPK(int serviceID, int accountID, Date beginDate, Date endDate) {
        this.serviceID = serviceID;
        this.accountID = accountID;
        this.beginDate = beginDate;
        this.endDate = endDate;
    }

    public int getServiceID() {
        return serviceID;
    }

    public void setServiceID(int serviceID) {
        this.serviceID = serviceID;
    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) serviceID;
        hash += (int) accountID;
        hash += (beginDate != null ? beginDate.hashCode() : 0);
        hash += (endDate != null ? endDate.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AdditionalServicesReservationPK)) {
            return false;
        }
        AdditionalServicesReservationPK other = (AdditionalServicesReservationPK) object;
        if (this.serviceID != other.serviceID) {
            return false;
        }
        if (this.accountID != other.accountID) {
            return false;
        }
        if ((this.beginDate == null && other.beginDate != null) || (this.beginDate != null && !this.beginDate.equals(other.beginDate))) {
            return false;
        }
        if ((this.endDate == null && other.endDate != null) || (this.endDate != null && !this.endDate.equals(other.endDate))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "io.mif.labanorodraugai.entities.AdditionalServicesReservationPK[ serviceID=" + serviceID + ", accountID=" + accountID + ", beginDate=" + beginDate + ", endDate=" + endDate + " ]";
    }
    
}
