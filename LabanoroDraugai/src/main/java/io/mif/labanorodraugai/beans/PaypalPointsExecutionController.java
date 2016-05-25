/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.mif.labanorodraugai.beans;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import io.mif.labanorodraugai.entities.PaypalPointsPayment;
import io.mif.labanorodraugai.services.PaypalService;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Vytautas
 */
@RequestScoped 
@Stateful
@Named
public class PaypalPointsExecutionController {
    @PersistenceContext
    private EntityManager em;

    @Inject
    private PaypalService paypalService;
    
    @Inject
    private AuthenticationBean authenticationBean;

    private PaypalPointsPayment payment = null;
    
    /**
     * @return the payment
     */
    public PaypalPointsPayment getPayment() {
        
        this.payment = null;
        
        String guid = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("guid");
        String paymentId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("paymentId");
        String payerID = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("PayerID");
        
        if (guid == null || paymentId == null || payerID == null) {
            return null;
        }
        
        List<PaypalPointsPayment> payments = em.createNamedQuery("PaypalPointsPayment.findByGeneratedId")
                .setParameter("generatedId", guid)
                .getResultList();
        
        if (payments.isEmpty()) {
            return null;
        }
        
        if (payments.get(0).getAccount().getId() != authenticationBean.getLoggedAccount().getId()) {
            return null;
        }
        
        if (payments.get(0).getTransferDateTime() != null) {
            payment = payments.get(0);
            return payment;
        }
        
        try {
            paypalService.executePayment(payerID, paymentId);
            this.payment = payments.get(0);
            this.payment.setTransferDateTime(new Date());
            BigDecimal newPointsQuantity = this.payment.getAccount().getPointsQuantity().add(BigDecimal.valueOf(this.payment.getPointsAmount()));
            this.payment.getAccount().setPointsQuantity(newPointsQuantity);
            this.payment = em.merge(this.payment);
        } catch (PayPalRESTException ex) {
            Logger.getLogger(PaypalPointsExecutionController.class.getName()).log(Level.SEVERE, null, ex);
        }       
        
        return payment;
    }
    
}
