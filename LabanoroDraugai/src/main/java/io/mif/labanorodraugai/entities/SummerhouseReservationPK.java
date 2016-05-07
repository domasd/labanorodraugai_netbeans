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
public class SummerhouseReservationPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "AccountID")
    private int accountID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SummerhouseID")
    private int summerhouseID;
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

    public SummerhouseReservationPK() {
    }

    public SummerhouseReservationPK(int accountID, int summerhouseID, Date beginDate, Date endDate) {
        this.accountID = accountID;
        this.summerhouseID = summerhouseID;
        this.beginDate = beginDate;
        this.endDate = endDate;
    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public int getSummerhouseID() {
        return summerhouseID;
    }

    public void setSummerhouseID(int summerhouseID) {
        this.summerhouseID = summerhouseID;
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
        hash += (int) accountID;
        hash += (int) summerhouseID;
        hash += (beginDate != null ? beginDate.hashCode() : 0);
        hash += (endDate != null ? endDate.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SummerhouseReservationPK)) {
            return false;
        }
        SummerhouseReservationPK other = (SummerhouseReservationPK) object;
        if (this.accountID != other.accountID) {
            return false;
        }
        if (this.summerhouseID != other.summerhouseID) {
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
        return "io.mif.labanorodraugai.beans.SummerhouseReservationPK[ accountID=" + accountID + ", summerhouseID=" + summerhouseID + ", beginDate=" + beginDate + ", endDate=" + endDate + " ]";
    }
    
}
