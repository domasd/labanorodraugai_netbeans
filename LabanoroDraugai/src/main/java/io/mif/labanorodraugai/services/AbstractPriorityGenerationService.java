/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.mif.labanorodraugai.services;

import io.mif.labanorodraugai.beans.AdministrationController;
import io.mif.labanorodraugai.beans.AuthenticationBean;
import io.mif.labanorodraugai.entities.Account;
import io.mif.labanorodraugai.entities.ReservationGroups;
import io.mif.labanorodraugai.utils.CalendarUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author SFILON
 */

@TransactionManagement(TransactionManagementType.CONTAINER)
public abstract class AbstractPriorityGenerationService {
    
    @Inject
    protected AuthenticationBean authenticationBean;
    
    @Inject
    AdministrationController administrationController;
    
    @PersistenceContext
    protected EntityManager em;
    
    protected Account selectedAccount;
            
    protected abstract List<ReservationGroups> createReservationGroups(int numberOfGroups, Date reservationStartDate);
        
    protected abstract Map<String,Integer> setReservationGroupsToAccounts(List<Account> allAccounts, int numberOfUsersInOneGroup);
            
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
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void AddNewUser(Account account){
        List<ReservationGroups> allgroups = em.createNamedQuery("ReservationGroups.findAll").getResultList();
        ReservationGroups lastGroup = allgroups.get(allgroups.size()-1);
        
        if (lastGroup.getAccountList().size()<administrationController.getConfig().getMaxNumberOfAccountsInOneGroup()){
            account.setReservationGroup(lastGroup);
        }
        else{
            ReservationGroups newGroup = new ReservationGroups();
            newGroup.setGroupNumber(lastGroup.getGroupNumber()+1);
            newGroup.setStartOfReservationDate(CalendarUtils.addDays(lastGroup.getStartOfReservationDate(),7));
            

            em.persist(newGroup);
          
            account.setReservationGroup(newGroup);
            
            ArrayList<Account> accountList = new ArrayList<>();
            accountList.add(account);
            
            newGroup.setAccountList(accountList);
            em.merge(account);
        }
    }
}
