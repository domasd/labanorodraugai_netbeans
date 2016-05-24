/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.mif.labanorodraugai.beans;

import io.mif.labanorodraugai.entities.Account;
import io.mif.labanorodraugai.services.AbstractPriorityGenerationService;
import io.mif.labanorodraugai.services.RichAccountPriorityGenerationService;
import io.mif.labanorodraugai.services.StandartPriorityGenerationService;
import io.mif.labanorodraugai.utils.CalendarUtils;
import java.time.LocalDateTime;
import java.util.Date;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author SFILON
 */

@Singleton
@Startup
@Named
public class PriorityGroupsController {
    
    @Inject
    AbstractPriorityGenerationService reservationController; 
    
    private Date startOfRegistration;
    
    private int numberOfUsersInOneGroup;
        
    @Schedule(year="*", month="5", dayOfMonth = "24", hour = "21",minute = "50", second = "2", persistent = false)
    public void regeneratePriorityGroups(){
        System.out.println("Regeneration of priority groups has begun!");
        //for testing
        this.startOfRegistration = CalendarUtils.getDate(LocalDateTime.now().getYear(), 3, 1);
        this.numberOfUsersInOneGroup=2;
            
        reservationController.generateReservationPriority(numberOfUsersInOneGroup, this.startOfRegistration);        
    }   

}
