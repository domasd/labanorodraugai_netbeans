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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author SFILON
 */
@RequestScoped
@Stateful
@Named
public class StandartPriorityGenerationService extends AbstractPriorityGenerationService{

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
        Map<String,Integer> accAndHolidays = new HashMap<>();
        List<Integer> holidays = new ArrayList<>(); 
        int currentYear = LocalDateTime.now().getYear();       
        
        for(Account account:allAccounts){
        
            int numberOfHolidays=0;
            
            List<SummerhouseReservation> listOfReservations = account.getSummerhouseReservationList();
            
            for(SummerhouseReservation record:listOfReservations){
                                
                Date beginDate = record.getSummerhouseReservationPK().getBeginDate();
                Date endDate = record.getSummerhouseReservationPK().getEndDate();
                
                numberOfHolidays+=CalendarUtils.countDaysBetweenDatesBySpecificYear(beginDate, endDate, currentYear-1);
            }
            
            accAndHolidays.put(account.getEmail(),numberOfHolidays);
            holidays.add(numberOfHolidays);
        }
        
        Collections.sort(holidays);
           
        Map<String, Integer> accountAndReservationGroups = new HashMap<>();
        
        for(String key:accAndHolidays.keySet()){
            
            int index = holidays.indexOf(accAndHolidays.get(key));
            int group = new Double(Math.ceil((double)(index/numberOfUsersInOneGroup))).intValue();
            accountAndReservationGroups.put(key, group);
            holidays.set(index,-1);
        }
                     
        return accountAndReservationGroups;
    }
    

    
}
