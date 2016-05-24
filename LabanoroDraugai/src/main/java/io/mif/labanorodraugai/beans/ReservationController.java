package io.mif.labanorodraugai.beans;


import io.mif.labanorodraugai.entities.Account;
import io.mif.labanorodraugai.entities.AdditionalServices;
import io.mif.labanorodraugai.entities.AdditionalServicesReservation;
import io.mif.labanorodraugai.entities.AdditionalServicesReservationPK;
import io.mif.labanorodraugai.entities.SummerhouseReservation;
import io.mif.labanorodraugai.entities.SummerhouseReservationPK;
import io.mif.labanorodraugai.services.IPointsService;
import io.mif.labanorodraugai.services.PointsService;
import io.mif.labanorodraugai.services.StandartPriorityGenerationService;
import io.mif.labanorodraugai.utils.CalendarUtils;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.util.converter.LocalDateTimeStringConverter;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.faces.application.FacesMessage;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Qualifier;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.swing.text.html.HTML;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FlowEvent;

/**
 *
 * @author SFILON
 */


@Named
@Stateful
@ViewScoped
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ReservationController implements Serializable{
           
    @EJB
    private IPointsService pointsService;
    
    @Inject
    private AdditionalServicesController additionalServicesController;

    @Inject
    private SummerhouseController summerhouseController;
    
    @Inject
    private AuthenticationBean sessionBean;
    
    @Inject
    private AdministrationController administrationController;
    
    @PersistenceContext
    private EntityManager em;
    
    private Date reservationBeginDate;
    
    private Date reservationEndDate;
    
    private BigDecimal pointsSum;
    
    private BigDecimal summerhousePoints;
    
    private BigDecimal additionalServicesPoints;
    
    private List<String> errorList;

        
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String submitWithPoints(){
        
      if (pointsService.makeTransaction(pointsSum)){
            Date recordDate = new Date();
            submitReservation(recordDate);
            submitAdditionalServices(recordDate);             
 
            return "success.html";
      }
      else{
          RequestContext requestContext = RequestContext.getCurrentInstance();  
          requestContext.execute("PF('pointsPaymentDialogWidgetVar').hide()");
  
          FacesContext f = FacesContext.getCurrentInstance();
          f.addMessage(null,new FacesMessage("Ivyko klaida!"));
          
          return null;
      }
  
    }    
       
    private void submitReservation(Date recordDate) {
        
        SummerhouseReservationPK newRecordPK = new SummerhouseReservationPK(sessionBean.getLoggedAccount().getId(),
                summerhouseController.getSelected().getId(), reservationBeginDate, reservationEndDate);
        SummerhouseReservation newRecord = new SummerhouseReservation(newRecordPK);

        newRecord.setPointsAmount(summerhousePoints);
        newRecord.setRecordCreated(recordDate);
        em.persist(newRecord);       
    }
 
    private void submitAdditionalServices(Date recordDate){
        
        List<AdditionalServices> allItems = additionalServicesController.getItems();
        List<String> selectedItems = additionalServicesController.getSelectedItems();
       
        if (selectedItems==null) return;
        
        for(AdditionalServices service:allItems){
       
            if (selectedItems.contains(service.getName())){
                AdditionalServicesReservationPK newADRecordPK = new AdditionalServicesReservationPK(sessionBean.getLoggedAccount().getId(),
                service.getServiceID(), reservationBeginDate, reservationEndDate);
       
                AdditionalServicesReservation newADRecord = new AdditionalServicesReservation(newADRecordPK);
                newADRecord.setPointsAmount(additionalServicesPoints);
                newADRecord.setRecordCreated(recordDate);
                em.persist(newADRecord);
            }
        }
    }
    
    public int getRemainingDaysTillReservationBegin(){
       
        return CalendarUtils.countDaysBetweenDatesBySpecificYear(new Date(),sessionBean.getLoggedAccount().getReservationGroup().getStartOfReservationDate(),LocalDateTime.now().getYear());

    }
    
    public String onFlowProcess(FlowEvent event) {

        switch(event.getOldStep()){
            case "chooseReservationDate":
                boolean areDatesCorrect = validateSummerhouseReservationDates(reservationBeginDate, reservationEndDate, summerhouseController.getSelected().getId());
                
                if (areDatesCorrect)
                {
                    summerhousePoints=pointsService.calculateSummerhousePoints(CalendarUtils.countDaysBetweenDatesBySpecificYear(reservationBeginDate, reservationEndDate,LocalDateTime.now().getYear()));
                    additionalServicesPoints = pointsService.calculateAdditionalServicesPoints(CalendarUtils.countDaysBetweenDatesBySpecificYear(reservationBeginDate, reservationEndDate,LocalDateTime.now().getYear()));
                    pointsSum=summerhousePoints.add(additionalServicesPoints);

                    return event.getNewStep();
                } else{
                    FacesContext context = FacesContext.getCurrentInstance();
                    context.addMessage(null, new FacesMessage(ResourceBundle.getBundle("/ReservationBundle").getString("DatesNotCorrectError")));               
                    return event.getOldStep();
                }
                
            case "selectAdditionalServices":
                
                
                if (!event.getNewStep().equals("payment"))
                    return event.getNewStep();
                                
                areDatesCorrect = validateAdditionalServicesValidationDates(reservationBeginDate, reservationEndDate,additionalServicesController.getItems(), additionalServicesController.getSelectedItems());
                
                if (areDatesCorrect){
                    summerhousePoints=pointsService.calculateSummerhousePoints(CalendarUtils.countDaysBetweenDatesBySpecificYear(reservationBeginDate, reservationEndDate,LocalDateTime.now().getYear()));
                    additionalServicesPoints = pointsService.calculateAdditionalServicesPoints(CalendarUtils.countDaysBetweenDatesBySpecificYear(reservationBeginDate, reservationEndDate,LocalDateTime.now().getYear()));
                    pointsSum=summerhousePoints.add(additionalServicesPoints);
                    return event.getNewStep();
                } else{
                    
                    String text="";
                    for(String item:errorList){
                        if (item!=null)
                            text=text+" "+item+" ";
                    }
                    
                    FacesContext context = FacesContext.getCurrentInstance();
                    context.addMessage(null, new FacesMessage(ResourceBundle.getBundle("/ReservationBundle").getString("CanNotReserveAdditionalServices")+text));               
                    return event.getOldStep();
                }
            
            case "payment":
          
            default:
                break;
        }
                    
        return event.getNewStep();
        
    }
    
    public boolean validateSummerhouseReservationDates(Date startDate, Date endDate,int summerhouseID){
        
        errorList = new ArrayList<>();
        
        if (CalendarUtils.countDaysBetweenDatesBySpecificYear(startDate, endDate, LocalDateTime.now().getYear())>administrationController.getConfig().getMaxReservationDaysLength()){
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
    
    public List<SummerhouseReservation> getLoggedAccountAllReservations(){
        return getAccountAllReservations(sessionBean.getLoggedAccount());
    }
    
    public List<SummerhouseReservation> getAccountAllReservations(Account account){
                
        return em.createNamedQuery("SummerhouseReservation.findByAccountID").setParameter("accountID", account.getId()).getResultList();
    }
        
    /**
     * @return the reservationBeginDate
     */
    public Date getReservationBeginDate() {
        return reservationBeginDate;
    }

    /**
     * @param reservationBeginDate the reservationBeginDate to set
     */
    public void setReservationBeginDate(Date reservationBeginDate) {
        this.reservationBeginDate = reservationBeginDate;
    }

    /**
     * @return the reservationEndDate
     */
    public Date getReservationEndDate() {
        return reservationEndDate;
    }

    /**
     * @param reservationEndDate the reservationEndDate to set
     */
    public void setReservationEndDate(Date reservationEndDate) {
        this.reservationEndDate = reservationEndDate;
    }

    /**
     * @return the summerhousePoints
     */
    public BigDecimal getSummerhousePoints() {
        return summerhousePoints;
    }

    /**
     * @param summerhousePoints the summerhousePoints to set
     */
    public void setSummerhousePoints(BigDecimal summerhousePoints) {
        this.summerhousePoints = summerhousePoints;
    }

    /**
     * @return the additionalServicesPoints
     */
    public BigDecimal getAdditionalServicesPoints() {
        return additionalServicesPoints;
    }

    /**
     * @param additionalServicesPoints the additionalServicesPoints to set
     */
    public void setAdditionalServicesPoints(BigDecimal additionalServicesPoints) {
        this.additionalServicesPoints = additionalServicesPoints;
    }

    /**
     * @return the pointsSum
     */
    public BigDecimal getPointsSum() {
        return pointsSum;
    }

    /**
     * @param pointsSum the pointsSum to set
     */
    public void setPointsSum(BigDecimal pointsSum) {
        this.pointsSum = pointsSum;
    }

}
