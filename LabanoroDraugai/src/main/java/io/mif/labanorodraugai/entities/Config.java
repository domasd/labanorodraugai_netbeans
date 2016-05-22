/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.mif.labanorodraugai.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
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
    
}
