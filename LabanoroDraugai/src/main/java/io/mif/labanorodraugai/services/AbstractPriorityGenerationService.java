/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.mif.labanorodraugai.services;

import io.mif.labanorodraugai.beans.AuthenticationBean;
import io.mif.labanorodraugai.entities.Account;
import io.mif.labanorodraugai.entities.ReservationGroups;
import io.mif.labanorodraugai.entities.SummerhouseReservation;
import io.mif.labanorodraugai.utils.CalendarUtils;
import java.time.LocalDateTime;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Schedule;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author SFILON
 */

@TransactionManagement(TransactionManagementType.CONTAINER)
public abstract class AbstractPriorityGenerationService {
    
    @Inject
    private AuthenticationBean authenticationBean;
    
    @PersistenceContext
    private EntityManager em;
    
    private Account selectedAccount;
            
    abstract List<ReservationGroups> createReservationGroups(int numberOfGroups, Date reservationStartDate);
        
    abstract Map<String,Integer> setReservationGroupsToAccounts(List<Account> allAccounts, int numberOfUsersInOneGroup);
            
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void generateReservationPriority(int numberOfUsersInOneGroup, Date startOfReservation){
           
        List<ReservationGroups> legacyGroups = em.createNamedQuery("ReservationGroups.findAll").getResultList();
        List<Account> allAccounts = em.createNamedQuery("Account.findAll").getResultList();
        
        for(ReservationGroups rgroup : legacyGroups){
            em.remove(em.merge(rgroup));
        }
               
        int numberOfExistingAccounts = allAccounts.size();          
        int numberOfGroups = (int) Math.ceil((double)numberOfExistingAccounts/numberOfUsersInOneGroup);      
        
        List<ReservationGroups> reservationGroups = createReservationGroups(numberOfGroups, startOfReservation);    
        Map<String, Integer> accAndGroups = setReservationGroupsToAccounts(allAccounts, numberOfUsersInOneGroup);
              
        for(ReservationGroups group:reservationGroups){
            em.persist(group);
        }       
       
        for(String key: accAndGroups.keySet()){
               
            Account accToUpdate  = null;
            
            for (Account acc:allAccounts){
                if (acc.getEmail().equals(key)){
                    accToUpdate=acc;
                }
            }

            ReservationGroups group = em.find(ReservationGroups.class, accAndGroups.get(key));
            accToUpdate.setReservationGroup(group);
            em.merge(accToUpdate);

        }
    }
}
