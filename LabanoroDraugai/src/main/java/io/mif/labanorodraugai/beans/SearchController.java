/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.mif.labanorodraugai.beans;

import io.mif.labanorodraugai.beans.util.FilterParam;
import io.mif.labanorodraugai.entities.Summerhouse;
import io.mif.labanorodraugai.entities.SummerhouseReservation;
import io.mif.labanorodraugai.utils.CalendarUtils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author SFILON
 */

@Named
@RequestScoped
public class SearchController implements Serializable{
    
    @Inject
    private SummerhouseController summerhouseController;
    
    @PersistenceContext
    private EntityManager em;
    
    private List<Summerhouse> results;
    
    private FilterParam parameters;
    
    private int minCapacity=4;
    private int maxCapacity=10;
    private String name;
    private Date beginDate;
    private Date endDate;
    private int maxPoints=16;
    
    public void filter(){
      
        List<Summerhouse> allHouses = em.createNamedQuery("Summerhouse.findAll").getResultList();
        
        List<Summerhouse> result = new ArrayList<>();
        
        for(Summerhouse summerhouse:allHouses){
            
            if ((summerhouse.getCapacity()>=getMinCapacity() || getMinCapacity()==0)
                 && (summerhouse.getPointsPerDay().doubleValue()<getMaxPoints() || getMaxPoints()==0)
                 && (summerhouse.getCapacity()<= getMaxCapacity() || getMaxCapacity()==0)
                 && (getBeginDate()==null || getEndDate()==null || checkSummerhouseAvailability(getBeginDate(), getEndDate(), summerhouse.getId()))){
                
                result.add(summerhouse);   
            }
        }
        
        results=result;
    
    }
    
    private boolean checkSummerhouseAvailability(Date startDate, Date endDate, int summerhouseID){
       
        if (startDate.after(endDate)){
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
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * @return the summerhouseController
     */
    public SummerhouseController getSummerhouseController() {
        return summerhouseController;
    }

    /**
     * @param summerhouseController the summerhouseController to set
     */
    public void setSummerhouseController(SummerhouseController summerhouseController) {
        this.summerhouseController = summerhouseController;
    }

    /**
     * @return the results
     */
    public List<Summerhouse> getResults() {
        return results;
    }

    /**
     * @param results the results to set
     */
    public void setResults(List<Summerhouse> results) {
        this.results = results;
    }

    /**
     * @return the parameters
     */
    public FilterParam getParameters() {
        return parameters;
    }

    /**
     * @param parameters the parameters to set
     */
    public void setParameters(FilterParam parameters) {
        this.parameters = parameters;
    }

    /**
     * @return the minCapacity
     */
    public int getMinCapacity() {
        return minCapacity;
    }

    /**
     * @param minCapacity the minCapacity to set
     */
    public void setMinCapacity(int minCapacity) {
        this.minCapacity = minCapacity;
    }

    /**
     * @return the maxCapacity
     */
    public int getMaxCapacity() {
        return maxCapacity;
    }

    /**
     * @param maxCapacity the maxCapacity to set
     */
    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the beginDate
     */
    public Date getBeginDate() {
        return beginDate;
    }

    /**
     * @param beginDate the beginDate to set
     */
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the maxPoints
     */
    public int getMaxPoints() {
        return maxPoints;
    }

    /**
     * @param maxPoints the maxPoints to set
     */
    public void setMaxPoints(int maxPoints) {
        this.maxPoints = maxPoints;
    }

    
}
