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
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "paypal_points_payment")
@NamedQueries({
    @NamedQuery(name = "PaypalPointsPayment.findAll", query = "SELECT p FROM PaypalPointsPayment p"),
    @NamedQuery(name = "PaypalPointsPayment.findByGeneratedId", query = "SELECT p FROM PaypalPointsPayment p WHERE p.generatedId = :generatedId"),
    @NamedQuery(name = "PaypalPointsPayment.findByPointsAmount", query = "SELECT p FROM PaypalPointsPayment p WHERE p.pointsAmount = :pointsAmount"),
    @NamedQuery(name = "PaypalPointsPayment.findByTotalCost", query = "SELECT p FROM PaypalPointsPayment p WHERE p.totalCost = :totalCost"),
    @NamedQuery(name = "PaypalPointsPayment.findByTransferDateTime", query = "SELECT p FROM PaypalPointsPayment p WHERE p.transferDateTime = :transferDateTime")})
public class PaypalPointsPayment implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 36)
    @Column(name = "GeneratedId")
    private String generatedId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PointsAmount")
    private int pointsAmount;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "TotalCost")
    private BigDecimal totalCost;
    @Column(name = "TransferDateTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date transferDateTime;
    @JoinColumn(name = "AccountId", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private Account account;

    public PaypalPointsPayment() {
    }

    public PaypalPointsPayment(String generatedId) {
        this.generatedId = generatedId;
    }

    public PaypalPointsPayment(String generatedId, int pointsAmount, BigDecimal totalCost) {
        this.generatedId = generatedId;
        this.pointsAmount = pointsAmount;
        this.totalCost = totalCost;
    }

    public String getGeneratedId() {
        return generatedId;
    }

    public void setGeneratedId(String generatedId) {
        this.generatedId = generatedId;
    }

    public int getPointsAmount() {
        return pointsAmount;
    }

    public void setPointsAmount(int pointsAmount) {
        this.pointsAmount = pointsAmount;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public Date getTransferDateTime() {
        return transferDateTime;
    }

    public void setTransferDateTime(Date transferDateTime) {
        this.transferDateTime = transferDateTime;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (generatedId != null ? generatedId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PaypalPointsPayment)) {
            return false;
        }
        PaypalPointsPayment other = (PaypalPointsPayment) object;
        if ((this.generatedId == null && other.generatedId != null) || (this.generatedId != null && !this.generatedId.equals(other.generatedId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "io.mif.labanorodraugai.entities.PaypalPointsPayment[ generatedId=" + generatedId + " ]";
    }
    
}
