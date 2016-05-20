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
import javax.ejb.Singleton;
import javax.ejb.Stateful;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author SFILON
 */
@ApplicationScoped
@Singleton
@Named
public class PriorityGroupsController {
    
    @Inject
    StandartPriorityGenerationService reservationController; 
            
    private Date generationDate;
    
    private Date startOfRegistration;
    
    private int numberOfUsersInOneGroup;
    
    public void regenerateIfNeed(){
        
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        
        //for testing
        this.startOfRegistration = CalendarUtils.getDate(LocalDateTime.now().getYear(), 3, 1);
        this.numberOfUsersInOneGroup=2;
        if (generationDate==null)
            this.generationDate = CalendarUtils.getDate(2015, 3, 1);
        //
        
        if ((generationDate == null || Integer.parseInt(yearFormat.format(generationDate)) < Integer.parseInt(yearFormat.format(new Date())))){
            
            
            reservationController.generateReservationPriority(numberOfUsersInOneGroup, this.startOfRegistration);
            this.generationDate = new Date();
        }
        
    }
    

}
