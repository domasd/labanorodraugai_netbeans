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
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dziaudom
 */
@Entity
@Table(name = "config")
@NamedQueries({@NamedQuery(name = "Config.getConfig", query = "SELECT c FROM Config c")})
public class Config implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "FirstDateAbleToReserve")
    @Temporal(TemporalType.DATE)
    private Date firstDateAbleToReserve;
    @Basic(optional = false)
    @NotNull
    @Column(name = "LastDateAbleToReserve")
    @Temporal(TemporalType.DATE)
    private Date lastDateAbleToReserve;

    @Basic(optional = false)
    @NotNull
    @Column(name = "ReservationProcessBeginDate")
    @Temporal(TemporalType.DATE)
    private Date reservationProcessBeginDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MaxNumberOfAccountsInOneGroup")
    private int maxNumberOfAccountsInOneGroup;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MaxReservationDaysLength")
    private int maxReservationDaysLength;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "Id")
    private Integer id;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "MinApprovalsRequired")
    private int minApprovalsRequired;
    
    @Lob
    @Size(max = 65535)
    @Column(name = "CustomRegistrationFields")    
    @NotNull
    private String customRegistrationFields;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "SeasonDays")
    private int seasonDays;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "MembershipPrice")
    private int membershipPrice;

    public Config() {
    }

    public Config(Integer id) {
        this.id = id;
    }

    public Config(Integer id, int minApprovalsRequired) {
        this.id = id;
        this.minApprovalsRequired = minApprovalsRequired;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getMinApprovalsRequired() {
        return minApprovalsRequired;
    }

    public void setMinApprovalsRequired(int minApprovalsRequired) {
        this.minApprovalsRequired = minApprovalsRequired;
    }

    public String getCustomRegistrationFields() {
        return customRegistrationFields;
    }

    public void setCustomRegistrationFields(String customRegistrationFields) {
        this.customRegistrationFields = customRegistrationFields;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Config)) {
            return false;
        }
        Config other = (Config) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "io.mif.labanorodraugai.entities.Config[ id=" + id + " ]";
    }

    public Date getReservationProcessBeginDate() {
        return reservationProcessBeginDate;
    }

    public void setReservationProcessBeginDate(Date reservationProcessBeginDate) {
        this.reservationProcessBeginDate = reservationProcessBeginDate;
    }

    public int getMaxNumberOfAccountsInOneGroup() {
        return maxNumberOfAccountsInOneGroup;
    }

    public void setMaxNumberOfAccountsInOneGroup(int maxNumberOfAccountsInOneGroup) {
        this.maxNumberOfAccountsInOneGroup = maxNumberOfAccountsInOneGroup;
    }

    public int getMaxReservationDaysLength() {
        return maxReservationDaysLength;
    }

    public void setMaxReservationDaysLength(int maxReservationDaysLength) {
        this.maxReservationDaysLength = maxReservationDaysLength;
    }

    public Date getFirstDateAbleToReserve() {
        return firstDateAbleToReserve;
    }

    public void setFirstDateAbleToReserve(Date firstDateAbleToReserve) {
        this.firstDateAbleToReserve = firstDateAbleToReserve;
    }

    public Date getLastDateAbleToReserve() {
        return lastDateAbleToReserve;
    }

    public void setLastDateAbleToReserve(Date lastDateAbleToReserve) {
        this.lastDateAbleToReserve = lastDateAbleToReserve;
    }
    
    public int getSeasonDays() {
        return seasonDays;
    }

    public void setSeasonDays(int SeasonDays) {
        this.seasonDays = SeasonDays;
    }
    
    public int getMembershipPrice() {
        return membershipPrice;
    }

    public void setMembershipPrice(int MembershipPrice) {
        this.membershipPrice = MembershipPrice;
    }
    
}
