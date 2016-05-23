/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.mif.labanorodraugai.services;

import io.mif.labanorodraugai.beans.SummerhouseController;
import io.mif.labanorodraugai.entities.Summerhouse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;

/**
 *
 * @author SFILON
 */
@Decorator
public class LuxuriousSummerhouseDiscountService implements IPointsService{
    @Inject
    @Delegate
    private IPointsService pointsService;
    
    @Inject
    private SummerhouseController summerhouseController;
    
    @Override
    public BigDecimal calculateSummerhousePoints(int numberOfDays){
        
        BigDecimal summerhousePointsPerDay = summerhouseController.getSelected().getPointsPerDay();
       
        double multiplicator=1;
        
        if (summerhousePointsPerDay.compareTo(new BigDecimal(15))>0){
            multiplicator=0.9;
        }
        
        return pointsService.calculateSummerhousePoints(numberOfDays).multiply(new BigDecimal(multiplicator));
    }
    
    @Override
    public BigDecimal calculateAdditionalServicesPoints(int numberOfDays){  
        return pointsService.calculateAdditionalServicesPoints(numberOfDays);
    }

    @Override
    public boolean makeTransaction(BigDecimal pointAmount) {
        return pointsService.makeTransaction(pointAmount);
    }
}
