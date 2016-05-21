/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.mif.labanorodraugai.services;

import io.mif.labanorodraugai.beans.AdditionalServicesController;
import io.mif.labanorodraugai.beans.AuthenticationBean;
import io.mif.labanorodraugai.beans.SummerhouseController;
import io.mif.labanorodraugai.entities.Account;
import io.mif.labanorodraugai.entities.AdditionalServices;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author SFILON
 */
@Stateful
@Named
@ViewScoped
@TransactionManagement(TransactionManagementType.CONTAINER)

public class PointsService {
        
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private AuthenticationBean authenticationBean;
    
    @Inject
    private SummerhouseController summerhouseController;
    
    @Inject
    private AdditionalServicesController additionalServicesController;
    
    private BigDecimal Points;
    
    private BigDecimal summerhousePoints;
    
    private BigDecimal additionalServicesPoints;
    
    public void calculateAllPoints(int numberOfDays){
        summerhousePoints = calculateSummerhousePoints(numberOfDays);
        additionalServicesPoints = calculateAdditionalServicesPoints(numberOfDays);
        Points=summerhousePoints.add(additionalServicesPoints);
    }
    
    public BigDecimal calculateSummerhousePoints(int numberOfDays){
        return summerhouseController.getSelected().getPointsPerDay().multiply(new BigDecimal(numberOfDays));
    }
    
    public BigDecimal calculateAdditionalServicesPoints(int numberOfDays){
        
        List<AdditionalServices> records = additionalServicesController.getItems();
        
        List<String> records2 = additionalServicesController.getSelectedItems();
        
        BigDecimal sum = BigDecimal.ZERO;
        
        if (records2==null) return sum;
        
        for(AdditionalServices service:additionalServicesController.getItems()){
            
            if (records2.contains(service.getName())){
                BigDecimal servicePoints = service.getPointsPerDay().multiply(new BigDecimal(String.valueOf(numberOfDays)));
                sum=sum.add(servicePoints);
            }
        }
        
        return sum;
    }

    public boolean makeTransaction(BigDecimal pointAmount){
        Account acc = (Account) em.createNamedQuery("Account.findById").setParameter("id", authenticationBean.getLoggedAccount().getId()).getSingleResult();
        
        if (pointAmount.compareTo(acc.getPointsQuantity())>0){
            return false;
        }
        else{
            try{
                acc.setPointsQuantity(authenticationBean.getLoggedAccount().getPointsQuantity().subtract(pointAmount));
                
                return true;
            }
            catch(OptimisticLockException e){
                return false;
            }
            finally{
                
            }

        }
    }

    /**
     * @return the Points
     */
    public BigDecimal getPoints() {
        return Points;
    }

    /**
     * @param Points the Points to set
     */
    public void setPoints(BigDecimal Points) {
        this.Points = Points;
    }

    /**
     * @return the summerhousePoints
     */
    public BigDecimal getSummerhousePoints() {
        return summerhousePoints;
    }

    /**
     * @param summerhousePoints the summerhousePoints to set
     */
    public void setSummerhousePoints(BigDecimal summerhousePoints) {
        this.summerhousePoints = summerhousePoints;
    }

    /**
     * @return the additionalServicesPoints
     */
    public BigDecimal getAdditionalServicesPoints() {
        return additionalServicesPoints;
    }

    /**
     * @param additionalServicesPoints the additionalServicesPoints to set
     */
    public void setAdditionalServicesPoints(BigDecimal additionalServicesPoints) {
        this.additionalServicesPoints = additionalServicesPoints;
    }
}
