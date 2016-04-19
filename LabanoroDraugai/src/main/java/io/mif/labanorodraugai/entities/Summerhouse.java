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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Vies
 */
@Entity
@Table(name = "summerhouse")
@NamedQueries({
    @NamedQuery(name = "Summerhouse.findAll", query = "SELECT s FROM Summerhouse s"),
    @NamedQuery(name = "Summerhouse.findById", query = "SELECT s FROM Summerhouse s WHERE s.id = :id"),
    @NamedQuery(name = "Summerhouse.findByName", query = "SELECT s FROM Summerhouse s WHERE s.name = :name"),
    @NamedQuery(name = "Summerhouse.findByDescription", query = "SELECT s FROM Summerhouse s WHERE s.description = :description"),
    @NamedQuery(name = "Summerhouse.findByCapacity", query = "SELECT s FROM Summerhouse s WHERE s.capacity = :capacity"),
    @NamedQuery(name = "Summerhouse.findByAvailiability", query = "SELECT s FROM Summerhouse s WHERE s.availiability = :availiability")})
public class Summerhouse implements Serializable {

    @Size(max = 200)
    @Column(name = "url")
    private String url;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "name")
    private String name;
    @Size(max = 50)
    @Column(name = "description")
    private String description;
    @Column(name = "capacity")
    private Integer capacity;
    @Column(name = "availiability")
    @Temporal(TemporalType.TIMESTAMP)
    private Date availiability;

    public Summerhouse() {
    }

    public Summerhouse(Integer id) {
        this.id = id;
    }

    public Summerhouse(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Date getAvailiability() {
        return availiability;
    }

    public void setAvailiability(Date availiability) {
        this.availiability = availiability;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (name != null ? name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Summerhouse)) {
            return false;
        }
        Summerhouse other = (Summerhouse) object;
        if ((this.name == null && other.name != null) || (this.name != null && !this.name.equals(other.name))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "io.mif.labanorodraugai.entities.Summerhouse[ id=" + id + " ]";
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
}
