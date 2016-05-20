/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.mif.labanorodraugai.services;

import io.mif.labanorodraugai.beans.AdditionalServicesController;
import io.mif.labanorodraugai.beans.SummerhouseController;
import io.mif.labanorodraugai.entities.AdditionalServices;
import java.util.List;
import javax.ejb.Stateful;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author SFILON
 */
@Stateful
@Named
@ViewScoped
public class PointsService {
    
    @Inject
    private SummerhouseController summerhouseController;
    
    @Inject
    private AdditionalServicesController additionalServicesController;
    
    private int Points;
    
    public void calculateAllPoints(int numberOfDays){
        int a = calculateSummerhousePoints(numberOfDays);
        int b = calculateAdditionalServicesPoints(numberOfDays);
        Points=a+b;
    }
    
    public int calculateSummerhousePoints(int numberOfDays){
        return summerhouseController.getSelected().getPointsPerDay()*numberOfDays;
    }
    
    public int calculateAdditionalServicesPoints(int numberOfDays){
        
        List<AdditionalServices> records = additionalServicesController.getItems();
        
        List<String> records2 = additionalServicesController.getSelectedItems();
        
        if (records2==null) return 0;
        
        int sum=0;
        
        for(AdditionalServices service:additionalServicesController.getItems()){
            
            if (records2.contains(service.getName())){
            
               sum=sum+service.getPoints()*numberOfDays;
            }
        }
        
        return sum;
    }

    /**
     * @return the Points
     */
    public int getPoints() {
        return Points;
    }

    /**
     * @param Points the Points to set
     */
    public void setPoints(int Points) {
        this.Points = Points;
    }
}
