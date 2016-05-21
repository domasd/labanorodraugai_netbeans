/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.mif.labanorodraugai.entities;

import io.mif.labanorodraugai.entities.enums.AccountStatus;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author SFILON
 */
@Entity
@Table(name = "account")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Account.doesSameEmailExist", query = "SELECT count(a.id) FROM Account a WHERE a.email = :email AND a.id <> :userId"),
    @NamedQuery(name="Account.findByEmailAndPassword", query="SELECT a FROM Account a Where a.email=:email and a.password=:password"),
    @NamedQuery(name = "Account.findAll", query = "SELECT a FROM Account a"),
    @NamedQuery(name = "Account.findById", query = "SELECT a FROM Account a WHERE a.id = :id"),
    @NamedQuery(name = "Account.findByPassword", query = "SELECT a FROM Account a WHERE a.password = :password"),
    @NamedQuery(name = "Account.findByName", query = "SELECT a FROM Account a WHERE a.name = :name"),
    @NamedQuery(name = "Account.findByLastname", query = "SELECT a FROM Account a WHERE a.lastname = :lastname"),
    @NamedQuery(name = "Account.findByDescription", query = "SELECT a FROM Account a WHERE a.description = :description"),
    @NamedQuery(name = "Account.findByStatus", query = "SELECT a FROM Account a WHERE a.status = :status"),
    @NamedQuery(name = "Account.findByEmail", query = "SELECT a FROM Account a WHERE a.email = :email"),
    @NamedQuery(name = "Account.findByFbUrl", query = "SELECT a FROM Account a WHERE a.fbUrl = :fbUrl"),
    @NamedQuery(name = "Account.findByPointsQuantity", query = "SELECT a FROM Account a WHERE a.pointsQuantity = :pointsQuantity"),
    @NamedQuery(name = "Account.findByOptLockVersion", query = "SELECT a FROM Account a WHERE a.optLockVersion = :optLockVersion")})
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Id")
    private Integer id;
    @Size(max = 32)
    @Column(name = "Password")
    private String password;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "Name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "Lastname")
    private String lastname;
    @Size(max = 500)
    @Column(name = "Description")
    private String description;
    @Lob
    @Column(name = "Image")
    private byte[] image;
    @Basic(optional = false)
    @NotNull
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "Status")
    private AccountStatus status;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "Email")
    private String email;
    @Size(max = 100)
    @Column(name = "FB_URL")
    private String fbUrl;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "PointsQuantity")
    private BigDecimal pointsQuantity;
    @Column(name = "OptLockVersion")
    private Integer optLockVersion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "account")
    private List<AdditionalServicesReservation> additionalServicesReservationList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "account")
    private List<SummerhouseReservation> summerhouseReservationList;
    @JoinColumn(name = "ReservationGroup", referencedColumnName = "GroupNumber")
    @ManyToOne
    private ReservationGroups reservationGroup;

    public Account() {
    }

    public Account(Integer id) {
        this.id = id;
    }

    public Account(Integer id, String name, String lastname, AccountStatus status, String email, BigDecimal pointsQuantity) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.status = status;
        this.email = email;
        this.pointsQuantity = pointsQuantity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFbUrl() {
        return fbUrl;
    }

    public void setFbUrl(String fbUrl) {
        this.fbUrl = fbUrl;
    }

    public BigDecimal getPointsQuantity() {
        return pointsQuantity;
    }

    public void setPointsQuantity(BigDecimal pointsQuantity) {
        this.pointsQuantity = pointsQuantity;
    }

    public Integer getOptLockVersion() {
        return optLockVersion;
    }

    public void setOptLockVersion(Integer optLockVersion) {
        this.optLockVersion = optLockVersion;
    }

    @XmlTransient
    public List<AdditionalServicesReservation> getAdditionalServicesReservationList() {
        return additionalServicesReservationList;
    }

    public void setAdditionalServicesReservationList(List<AdditionalServicesReservation> additionalServicesReservationList) {
        this.additionalServicesReservationList = additionalServicesReservationList;
    }

    @XmlTransient
    public List<SummerhouseReservation> getSummerhouseReservationList() {
        return summerhouseReservationList;
    }

    public void setSummerhouseReservationList(List<SummerhouseReservation> summerhouseReservationList) {
        this.summerhouseReservationList = summerhouseReservationList;
    }

    public ReservationGroups getReservationGroup() {
        return reservationGroup;
    }

    public void setReservationGroup(ReservationGroups reservationGroup) {
        this.reservationGroup = reservationGroup;
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
        if (!(object instanceof Account)) {
            return false;
        }
        Account other = (Account) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "io.mif.labanorodraugai.entities.Account[ id=" + id + " ]";
    }
    
}
