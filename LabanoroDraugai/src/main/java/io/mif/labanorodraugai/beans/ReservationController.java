package io.mif.labanorodraugai.beans;


import io.mif.labanorodraugai.entities.Account;
import io.mif.labanorodraugai.entities.AdditionalServices;
import io.mif.labanorodraugai.entities.AdditionalServicesReservation;
import io.mif.labanorodraugai.entities.AdditionalServicesReservationPK;
import io.mif.labanorodraugai.entities.SummerhouseReservation;
import io.mif.labanorodraugai.entities.SummerhouseReservationPK;
import io.mif.labanorodraugai.services.PointsService;
import io.mif.labanorodraugai.services.ReservationService;
import io.mif.labanorodraugai.services.StandartPriorityGenerationService;
import io.mif.labanorodraugai.utils.CalendarUtils;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.util.converter.LocalDateTimeStringConverter;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
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

@ViewScoped
@Stateful
@Named
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ReservationController implements Serializable{
       
    @Inject
    private ReservationService reservationService;
    
    @Inject
    private PointsService pointsService;
    
    @Inject
    private AdditionalServicesController additionalServicesController;

    @Inject
    private SummerhouseController summerhouseController;
    
    @Inject
    private AuthenticationBean sessionBean;
    
    @PersistenceContext
    private EntityManager em;
    
    private Date reservationBeginDate;
    
    private Date reservationEndDate;
    
    private Date startOfReservationProcess;
    
    private Date endOfReservationProcess;
    
    private List<String> errorList;

        
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String submitWithPoints(){
        
      if (pointsService.makeTransaction(pointsService.getPoints())){
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

        newRecord.setPointsAmount(pointsService.getSummerhousePoints());
        newRecord.setRecordCreated(recordDate);
        em.persist(newRecord);
//        newRecord.setAccount(sessionBean.getLoggedAccount());
//        newRecord.setSummerhouse(summerhouseController.getSelected());   
                
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
                newADRecord.setPointsAmount(pointsService.getAdditionalServicesPoints());
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
                boolean areDatesCorrect = reservationService.validateSummerhouseReservationDates(reservationBeginDate, reservationEndDate, summerhouseController.getSelected().getId());
                
                if (areDatesCorrect)
                {
                    pointsService.calculateAllPoints(CalendarUtils.countDaysBetweenDatesBySpecificYear(reservationBeginDate, reservationEndDate,LocalDateTime.now().getYear()));
                
                    return event.getNewStep();
                } else{
                    FacesContext context = FacesContext.getCurrentInstance();
                    context.addMessage(null, new FacesMessage(ResourceBundle.getBundle("/ReservationBundle").getString("DatesNotCorrectError")));               
                    return event.getOldStep();
                }
                
            case "selectAdditionalServices":
                
                
                if (!event.getNewStep().equals("payment"))
                    return event.getNewStep();
                                
                areDatesCorrect = reservationService.validateAdditionalServicesValidationDates(reservationBeginDate, reservationEndDate,additionalServicesController.getItems(), additionalServicesController.getSelectedItems());
                
                if (areDatesCorrect){
                    pointsService.calculateAllPoints(CalendarUtils.countDaysBetweenDatesBySpecificYear(reservationBeginDate, reservationEndDate,LocalDateTime.now().getYear()));
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
    
    public List<SummerhouseReservation> getLoggedAccountAllReservations(){
        
        return em.createNamedQuery("SummerhouseReservation.findByAccountID")
                .setParameter("accountID", sessionBean.getLoggedAccount().getId()).getResultList();
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
     * @return the startOfReservationProcess
     */
    public Date getStartOfReservationProcess() {
        return CalendarUtils.getDate(2016,5,1);
    }

    /**
     * @param startOfReservationProcess the startOfReservationProcess to set
     */
    public void setStartOfReservationProcess(Date startOfReservationProcess) {
        this.startOfReservationProcess = startOfReservationProcess;
    }

    /**
     * @return the endOfReservationProcess
     */
    public Date getEndOfReservationProcess() {
        return CalendarUtils.getDate(2016, 7, 31);
    }

    /**
     * @param endOfReservationProcess the endOfReservationProcess to set
     */
    public void setEndOfReservationProcess(Date endOfReservationProcess) {
        this.endOfReservationProcess = endOfReservationProcess;
    }

}
