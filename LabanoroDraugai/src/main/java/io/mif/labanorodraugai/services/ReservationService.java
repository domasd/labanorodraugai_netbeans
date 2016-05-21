/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.mif.labanorodraugai.services;

import io.mif.labanorodraugai.entities.AdditionalServices;
import io.mif.labanorodraugai.entities.AdditionalServicesReservation;
import io.mif.labanorodraugai.entities.SummerhouseReservation;
import io.mif.labanorodraugai.utils.CalendarUtils;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.Stateful;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author SFILON
 */
@Stateful
@ViewScoped
@Named
public class ReservationService implements Serializable{

    
    @PersistenceContext
    private EntityManager em;
    
    private List<String> errorList;
    
    public boolean validateSummerhouseReservationDates(Date startDate, Date endDate,int summerhouseID){
        
        errorList = new ArrayList<>();
        
        if (CalendarUtils.countDaysBetweenDatesBySpecificYear(startDate, endDate, LocalDateTime.now().getYear())>4){
            errorList.add(ResourceBundle.getBundle("/ReservationBundle").getString("BadReservationLength"));
            return false;
        }
        
        if (startDate.after(endDate)){
            errorList.add(ResourceBundle.getBundle("/ReservationBundle").getString("StartDateIsAfterEndError"));
            return false;
        }
            
        
        List<Date> selectedDates= CalendarUtils.getDatesBetweenDates(startDate, endDate);
        
        List<SummerhouseReservation> reservationHistory =  em.createNamedQuery("SummerhouseReservation.findBySummerhouseID")
                .setParameter("summerhouseID", summerhouseID).getResultList();
        
        List<Date> allSummerhouseDates = new ArrayList<>();
        
        for(SummerhouseReservation record:reservationHistory){
            Date recordStartDate = record.getSummerhouseReservationPK().getBeginDate();
            Date recordEndDate = record.getSummerhouseReservationPK().getEndDate();
            
            allSummerhouseDates.addAll(CalendarUtils.getDatesBetweenDates(recordStartDate, recordEndDate));
        }
        
        for(Date date:selectedDates){
            if (allSummerhouseDates.contains(date)){
                errorList.add(ResourceBundle.getBundle("/ReservationBundle").getString("BadReservationDates"));
                return false;
            }
        }
        
        return true;
    }    
    
    private boolean validateAdditionalServiceReservationDate(int additionalServiceID, List<Date> selectedDates){
        
        List<AdditionalServicesReservation> reservationHistory =  em.createNamedQuery("AdditionalServicesReservation.findByServiceID")
                .setParameter("serviceID", additionalServiceID).getResultList();
        
        List<Date> allServiceDates = new ArrayList<>();
        
        for(AdditionalServicesReservation record:reservationHistory){
            Date recordStartDate = record.getAdditionalServicesReservationPK().getBeginDate();
            Date recordEndDate = record.getAdditionalServicesReservationPK().getEndDate();
            
            allServiceDates.addAll(CalendarUtils.getDatesBetweenDates(recordStartDate, recordEndDate));
        }
        
        for(Date date:selectedDates){
            if (allServiceDates.contains(date)){
                return false;
            }
        }
        
        return true;
    }
    
    public boolean validateAdditionalServicesValidationDates(Date startDate, Date endDate, List<AdditionalServices> allItems, List<String> selectedItems){
        
        errorList = new ArrayList<>();
        
        List<Date> selectedDates= CalendarUtils.getDatesBetweenDates(startDate, endDate);
        
        for(AdditionalServices service:allItems){
            
            if (selectedItems.contains(service.getName())){
            
                if (!validateAdditionalServiceReservationDate(service.getServiceID(), selectedDates)){
                    errorList.add(service.getName());
                    return false;
                }
            }
            
        }
        
        return true;
        
    }

    
    
    /**
     * @return the errorList
     */
    public List<String> getErrorList() {
        return errorList;
    }

    /**
     * @param errorList the errorList to set
     */
    public void setErrorList(List<String> errorList) {
        this.errorList = errorList;
    }
}
