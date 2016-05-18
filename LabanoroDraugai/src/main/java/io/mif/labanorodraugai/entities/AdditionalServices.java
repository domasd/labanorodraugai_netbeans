/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.mif.labanorodraugai.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author SFILON
 */
@Entity
@Table(name = "additional_services")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AdditionalServices.findAll", query = "SELECT a FROM AdditionalServices a"),
    @NamedQuery(name = "AdditionalServices.findByServiceID", query = "SELECT a FROM AdditionalServices a WHERE a.serviceID = :serviceID"),
    @NamedQuery(name = "AdditionalServices.findByName", query = "SELECT a FROM AdditionalServices a WHERE a.name = :name"),
    @NamedQuery(name = "AdditionalServices.findByDescription", query = "SELECT a FROM AdditionalServices a WHERE a.description = :description"),
    @NamedQuery(name = "AdditionalServices.findByPoints", query = "SELECT a FROM AdditionalServices a WHERE a.points = :points")})
public class AdditionalServices implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "additionalServices")
    private List<AdditionalServicesReservation> additionalServicesReservationList;

    @Basic(optional = false)
    @NotNull
    @Column(name = "Points")
    private int points;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ServiceID")
    private Integer serviceID;
    @Size(max = 45)
    @Column(name = "Name")
    private String name;
    @Size(max = 256)
    @Column(name = "Description")
    private String description;

    public AdditionalServices() {
    }

    public AdditionalServices(Integer serviceID) {
        this.serviceID = serviceID;
    }

    public Integer getServiceID() {
        return serviceID;
    }

    public void setServiceID(Integer serviceID) {
        this.serviceID = serviceID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (serviceID != null ? serviceID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AdditionalServices)) {
            return false;
        }
        AdditionalServices other = (AdditionalServices) object;
        if ((this.serviceID == null && other.serviceID != null) || (this.serviceID != null && !this.serviceID.equals(other.serviceID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @XmlTransient
    public List<AdditionalServicesReservation> getAdditionalServicesReservationList() {
        return additionalServicesReservationList;
    }

    public void setAdditionalServicesReservationList(List<AdditionalServicesReservation> additionalServicesReservationList) {
        this.additionalServicesReservationList = additionalServicesReservationList;
    }
    
}
