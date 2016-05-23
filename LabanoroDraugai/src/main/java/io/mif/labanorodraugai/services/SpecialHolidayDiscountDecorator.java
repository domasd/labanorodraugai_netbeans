/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.mif.labanorodraugai.services;

import io.mif.labanorodraugai.beans.AdditionalServicesController;
import io.mif.labanorodraugai.beans.SummerhouseController;
import io.mif.labanorodraugai.entities.AdditionalServices;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;

/**
 *
 * @author SFILON
 */
@Decorator
public class SpecialHolidayDiscountDecorator implements IPointsService {
    @Inject
    @Delegate
    private IPointsService pointsService;
        
    @Override
    public BigDecimal calculateSummerhousePoints(int numberOfDays){
        
        LocalDateTime currentDate = LocalDateTime.now();
       
        double multiplicator=1;
        
        if (currentDate.getMonth()==Month.FEBRUARY && currentDate.getDayOfMonth()==16)
            multiplicator=0.8;
        
        return pointsService.calculateSummerhousePoints(numberOfDays).multiply(new BigDecimal(multiplicator));
    }
    
    @Override
    public BigDecimal calculateAdditionalServicesPoints(int numberOfDays){  
        
        LocalDateTime currentDate = LocalDateTime.now();
       
        double multiplicator=1;
        
        if (currentDate.getMonth()==Month.FEBRUARY && currentDate.getDayOfMonth()==16)
            multiplicator=0.8;
        
        return pointsService.calculateAdditionalServicesPoints(numberOfDays).multiply(new BigDecimal(multiplicator));
    }

    @Override
    public boolean makeTransaction(BigDecimal pointAmount) {
        return pointsService.makeTransaction(pointAmount);
    }
}
