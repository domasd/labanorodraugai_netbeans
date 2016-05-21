/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.mif.labanorodraugai.entities;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author SFILON
 */
@Entity
@Table(name = "additional_services_reservation")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AdditionalServicesReservation.findAll", query = "SELECT a FROM AdditionalServicesReservation a"),
    @NamedQuery(name = "AdditionalServicesReservation.findByServiceID", query = "SELECT a FROM AdditionalServicesReservation a WHERE a.additionalServicesReservationPK.serviceID = :serviceID"),
    @NamedQuery(name = "AdditionalServicesReservation.findByAccountID", query = "SELECT a FROM AdditionalServicesReservation a WHERE a.additionalServicesReservationPK.accountID = :accountID"),
    @NamedQuery(name = "AdditionalServicesReservation.findByBeginDate", query = "SELECT a FROM AdditionalServicesReservation a WHERE a.additionalServicesReservationPK.beginDate = :beginDate"),
    @NamedQuery(name = "AdditionalServicesReservation.findByEndDate", query = "SELECT a FROM AdditionalServicesReservation a WHERE a.additionalServicesReservationPK.endDate = :endDate"),
    @NamedQuery(name = "AdditionalServicesReservation.findByRecordCreated", query = "SELECT a FROM AdditionalServicesReservation a WHERE a.recordCreated = :recordCreated"),
    @NamedQuery(name = "AdditionalServicesReservation.findByPointsAmount", query = "SELECT a FROM AdditionalServicesReservation a WHERE a.pointsAmount = :pointsAmount")})
public class AdditionalServicesReservation implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AdditionalServicesReservationPK additionalServicesReservationPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "RecordCreated")
    @Temporal(TemporalType.DATE)
    private Date recordCreated;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "PointsAmount")
    private BigDecimal pointsAmount;
    @JoinColumn(name = "AccountID", referencedColumnName = "Id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Account account;
    @JoinColumn(name = "ServiceID", referencedColumnName = "ServiceID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private AdditionalServices additionalServices;

    public AdditionalServicesReservation() {
    }

    public AdditionalServicesReservation(AdditionalServicesReservationPK additionalServicesReservationPK) {
        this.additionalServicesReservationPK = additionalServicesReservationPK;
    }

    public AdditionalServicesReservation(AdditionalServicesReservationPK additionalServicesReservationPK, Date recordCreated, BigDecimal pointsAmount) {
        this.additionalServicesReservationPK = additionalServicesReservationPK;
        this.recordCreated = recordCreated;
        this.pointsAmount = pointsAmount;
    }

    public AdditionalServicesReservation(int serviceID, int accountID, Date beginDate, Date endDate) {
        this.additionalServicesReservationPK = new AdditionalServicesReservationPK(serviceID, accountID, beginDate, endDate);
    }

    public AdditionalServicesReservationPK getAdditionalServicesReservationPK() {
        return additionalServicesReservationPK;
    }

    public void setAdditionalServicesReservationPK(AdditionalServicesReservationPK additionalServicesReservationPK) {
        this.additionalServicesReservationPK = additionalServicesReservationPK;
    }

    public Date getRecordCreated() {
        return recordCreated;
    }

    public void setRecordCreated(Date recordCreated) {
        this.recordCreated = recordCreated;
    }

    public BigDecimal getPointsAmount() {
        return pointsAmount;
    }

    public void setPointsAmount(BigDecimal pointsAmount) {
        this.pointsAmount = pointsAmount;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public AdditionalServices getAdditionalServices() {
        return additionalServices;
    }

    public void setAdditionalServices(AdditionalServices additionalServices) {
        this.additionalServices = additionalServices;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (additionalServicesReservationPK != null ? additionalServicesReservationPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AdditionalServicesReservation)) {
            return false;
        }
        AdditionalServicesReservation other = (AdditionalServicesReservation) object;
        if ((this.additionalServicesReservationPK == null && other.additionalServicesReservationPK != null) || (this.additionalServicesReservationPK != null && !this.additionalServicesReservationPK.equals(other.additionalServicesReservationPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "io.mif.labanorodraugai.entities.AdditionalServicesReservation[ additionalServicesReservationPK=" + additionalServicesReservationPK + " ]";
    }
    
}
