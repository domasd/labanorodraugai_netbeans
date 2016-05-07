/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.mif.labanorodraugai.entities;

import io.mif.labanorodraugai.entities.Account;
import io.mif.labanorodraugai.entities.Summerhouse;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author SFILON
 */
@Entity
@Table(name = "summerhouse_reservation")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SummerhouseReservation.findAll", query = "SELECT s FROM SummerhouseReservation s"),
    @NamedQuery(name = "SummerhouseReservation.findByAccountID", query = "SELECT s FROM SummerhouseReservation s WHERE s.summerhouseReservationPK.accountID = :accountID"),
    @NamedQuery(name = "SummerhouseReservation.findBySummerhouseID", query = "SELECT s FROM SummerhouseReservation s WHERE s.summerhouseReservationPK.summerhouseID = :summerhouseID"),
    @NamedQuery(name = "SummerhouseReservation.findByBeginDate", query = "SELECT s FROM SummerhouseReservation s WHERE s.summerhouseReservationPK.beginDate = :beginDate"),
    @NamedQuery(name = "SummerhouseReservation.findByEndDate", query = "SELECT s FROM SummerhouseReservation s WHERE s.summerhouseReservationPK.endDate = :endDate")})
public class SummerhouseReservation implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SummerhouseReservationPK summerhouseReservationPK;
    @JoinColumn(name = "AccountID", referencedColumnName = "Id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Account account;
    @JoinColumn(name = "SummerhouseID", referencedColumnName = "Id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Summerhouse summerhouse;

    public SummerhouseReservation() {
    }

    public SummerhouseReservation(SummerhouseReservationPK summerhouseReservationPK) {
        this.summerhouseReservationPK = summerhouseReservationPK;
    }

    public SummerhouseReservation(int accountID, int summerhouseID, Date beginDate, Date endDate) {
        this.summerhouseReservationPK = new SummerhouseReservationPK(accountID, summerhouseID, beginDate, endDate);
    }

    public SummerhouseReservationPK getSummerhouseReservationPK() {
        return summerhouseReservationPK;
    }

    public void setSummerhouseReservationPK(SummerhouseReservationPK summerhouseReservationPK) {
        this.summerhouseReservationPK = summerhouseReservationPK;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Summerhouse getSummerhouse() {
        return summerhouse;
    }

    public void setSummerhouse(Summerhouse summerhouse) {
        this.summerhouse = summerhouse;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (summerhouseReservationPK != null ? summerhouseReservationPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SummerhouseReservation)) {
            return false;
        }
        SummerhouseReservation other = (SummerhouseReservation) object;
        if ((this.summerhouseReservationPK == null && other.summerhouseReservationPK != null) || (this.summerhouseReservationPK != null && !this.summerhouseReservationPK.equals(other.summerhouseReservationPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "io.mif.labanorodraugai.beans.SummerhouseReservation[ summerhouseReservationPK=" + summerhouseReservationPK + " ]";
    }
    
}
