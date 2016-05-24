/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.mif.labanorodraugai.services;

import io.mif.labanorodraugai.entities.Account;
import io.mif.labanorodraugai.entities.ReservationGroups;
import io.mif.labanorodraugai.entities.SummerhouseReservation;
import io.mif.labanorodraugai.utils.CalendarUtils;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.inject.Alternative;

/**
 *
 * @author SFILON
 */
public @Alternative class RichAccountPriorityGenerationService extends AbstractPriorityGenerationService {
    
    @Override
    protected List<ReservationGroups> createReservationGroups(int numberOfGroups, Date reservationStartDate) {
        
        List<ReservationGroups> reservationGroups = new ArrayList<>();
        
        for (int i=0; i<numberOfGroups;i++){
            ReservationGroups group = new ReservationGroups(i,reservationStartDate);
            reservationGroups.add(group);         
            reservationStartDate=CalendarUtils.addDays(reservationStartDate,7);
        }    

        return reservationGroups;
    }
    
    @Override
    protected Map<String, Integer> setReservationGroupsToAccounts(List<Account> allAccounts, int numberOfUsersInOneGroup) {
        System.out.println("Using rich user prioritisation algorithm!");
        
        Map<String,BigDecimal> accAndPoints = new HashMap<>();
        List<BigDecimal> points = new ArrayList<>();     
        
        for(Account account:allAccounts){
        
            BigDecimal spendMoney =BigDecimal.ZERO;
            
            List<SummerhouseReservation> listOfReservations = account.getSummerhouseReservationList();
            
            for(SummerhouseReservation record:listOfReservations){               
                spendMoney.add(record.getPointsAmount());
            }
            
            accAndPoints.put(account.getEmail(),spendMoney);
            points.add(spendMoney);
        }
        
        Collections.sort(points);
           
        Map<String, Integer> accountAndReservationGroups = new HashMap<>();
        
        for(String key:accAndPoints.keySet()){
            
            int index = points.indexOf(accAndPoints.get(key));
            int group = new Double(Math.ceil((double)(index/numberOfUsersInOneGroup))).intValue();
            accountAndReservationGroups.put(key, group);
            points.set(index,BigDecimal.valueOf((double)-1));
        }
                     
        return accountAndReservationGroups;
    }
}
