/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.mif.labanorodraugai.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author SFILON
 */
@Entity
@Table(name = "reservation_groups")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReservationGroups.findAll", query = "SELECT r FROM ReservationGroups r"),
    @NamedQuery(name = "ReservationGroups.findByGroupNumber", query = "SELECT r FROM ReservationGroups r WHERE r.groupNumber = :groupNumber"),
    @NamedQuery(name = "ReservationGroups.findByStartOfReservationDate", query = "SELECT r FROM ReservationGroups r WHERE r.startOfReservationDate = :startOfReservationDate")})
public class ReservationGroups implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "GroupNumber")
    private Integer groupNumber;
    @Basic(optional = false)
    @NotNull
    @Column(name = "StartOfReservationDate")
    @Temporal(TemporalType.DATE)
    private Date startOfReservationDate;
    @OneToMany(mappedBy = "reservationGroup")
    private List<Account> accountList;

    public ReservationGroups() {
    }

    public ReservationGroups(Integer groupNumber) {
        this.groupNumber = groupNumber;
    }

    public ReservationGroups(Integer groupNumber, Date startOfReservationDate) {
        this.groupNumber = groupNumber;
        this.startOfReservationDate = startOfReservationDate;
    }

    public Integer getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(Integer groupNumber) {
        this.groupNumber = groupNumber;
    }

    public Date getStartOfReservationDate() {
        return startOfReservationDate;
    }

    public void setStartOfReservationDate(Date startOfReservationDate) {
        this.startOfReservationDate = startOfReservationDate;
    }

    @XmlTransient
    public List<Account> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<Account> accountList) {
        this.accountList = accountList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (groupNumber != null ? groupNumber.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReservationGroups)) {
            return false;
        }
        ReservationGroups other = (ReservationGroups) object;
        if ((this.groupNumber == null && other.groupNumber != null) || (this.groupNumber != null && !this.groupNumber.equals(other.groupNumber))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "io.mif.labanorodraugai.entities.ReservationGroups[ groupNumber=" + groupNumber + " ]";
    }
    
}
