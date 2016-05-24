/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.mif.labanorodraugai.beans;

import com.paypal.api.payments.Item;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import io.mif.labanorodraugai.entities.Account;
import io.mif.labanorodraugai.entities.PaypalPointsPayment;
import io.mif.labanorodraugai.services.PaypalService;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

/**
 *
 * @author Vytautas
 */
@RequestScoped 
@Stateful
@Named
public class PaypalPointsBuyController {
    @PersistenceContext
    private EntityManager em;

    @Inject
    private PaypalService paypalService;
    
    @Inject
    private AuthenticationBean authenticationBean;
    
    public void buyPoints() throws IOException {        
        String paymentId = UUID.randomUUID().toString().replaceAll("-", "");
        
        Payment payment;
                
        List<Item> items = Arrays.asList(new Item("Labanoro Draugai taškai", "3", "0.20", "EUR"));
        
        try {
            payment = paypalService.createPayment(paymentId, "payment/paypalPaymentDone.html", items, "Klubo \"Labanoro Draugai\" taškų pirkimas");
        } catch (PayPalRESTException ex) {
            Logger.getLogger(PaypalPointsBuyController.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        
        Account user = authenticationBean.getLoggedAccount();
        
        PaypalPointsPayment paypalPointsPayment = new PaypalPointsPayment(paymentId, 3, new BigDecimal(paypalService.calculateTotal(items)));
        paypalPointsPayment.setAccount(user);
                
        em.createQuery("DELETE FROM PaypalPointsPayment p WHERE p.account.id = :accountId AND p.transferDateTime IS NULL")
                .setParameter("accountId", user.getId())
                .executeUpdate();
        
        em.persist(paypalPointsPayment);
                
        String url = paypalService.getPaymentApprovalUrl(payment);
        FacesContext.getCurrentInstance().getExternalContext().redirect(url);
    }

}
