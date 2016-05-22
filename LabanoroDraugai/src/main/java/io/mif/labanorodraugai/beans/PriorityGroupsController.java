/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.mif.labanorodraugai.beans;

import io.mif.labanorodraugai.services.StandartPriorityGenerationService;
import io.mif.labanorodraugai.utils.CalendarUtils;
import java.text.SimpleDateFormat;
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
    StandartPriorityGenerationService priorityGenerationController; 
    
    @Inject
    AdministrationController administrationController;
    
    private Date startOfRegistration;
    
    private int numberOfUsersInOneGroup;
        
    @Schedule(year="*", month="1", dayOfMonth = "1", hour = "0",minute = "0", second = "2", persistent = false)
    public void regeneratePriorityGroups(){
        SimpleDateFormat monthFormat=new SimpleDateFormat("mm");
        SimpleDateFormat daysFormat =new SimpleDateFormat("dd");
        
        Date registrationDateSettings = administrationController.getConfig().getReservationProcessBeginDate();
        this.startOfRegistration = CalendarUtils.getDate(LocalDateTime.now().getYear(),
                Integer.parseInt(monthFormat.format(registrationDateSettings)),
                Integer.parseInt(daysFormat.format(registrationDateSettings)));

        this.numberOfUsersInOneGroup=administrationController.getConfig().getMaxNumberOfAccountsInOneGroup();
            
        priorityGenerationController.generateReservationPriority(numberOfUsersInOneGroup, this.startOfRegistration);        
    }   

}
