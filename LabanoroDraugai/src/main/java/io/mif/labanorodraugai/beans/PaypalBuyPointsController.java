/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.mif.labanorodraugai.beans;

import com.paypal.api.payments.Item;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import io.mif.labanorodraugai.beans.util.JsfUtil;
import io.mif.labanorodraugai.entities.Account;
import io.mif.labanorodraugai.entities.PaypalPointsPayment;
import io.mif.labanorodraugai.services.PaypalService;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateful;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Vytautas
 */
@ViewScoped
@Stateful
@Named
public class PaypalBuyPointsController {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private PaypalService paypalService;

    @Inject
    private AuthenticationBean authenticationBean;

    private int pointCount = 5;

    private BigDecimal pointPrice = new BigDecimal("0.2");

    public void buyPoints() throws IOException {
        String paymentId = UUID.randomUUID().toString().replaceAll("-", "");

        Payment payment;
        
        List<Item> items = Arrays.asList(new Item("Labanoro Draugai taškai", "" + pointCount, pointPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString(), "EUR"));

        try {
            payment = paypalService.createPayment(paymentId, "payment/paypalPaymentComplete.html", items, "Klubo \"Labanoro Draugai\" taškų pirkimas");
        } catch (PayPalRESTException ex) {
            Logger.getLogger(PaypalBuyPointsController.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("Nepavyko nukreipti į PayPal");
            return;
        }

        Account user = authenticationBean.getLoggedAccount();

        PaypalPointsPayment paypalPointsPayment = new PaypalPointsPayment(paymentId, this.pointCount, new BigDecimal(paypalService.calculateTotal(items)));
        paypalPointsPayment.setAccount(user);

        em.createQuery("DELETE FROM PaypalPointsPayment p WHERE p.account.id = :accountId AND p.transferDateTime IS NULL")
                .setParameter("accountId", user.getId())
                .executeUpdate();

        em.persist(paypalPointsPayment);

        String url = paypalService.getPaymentApprovalUrl(payment);
        FacesContext.getCurrentInstance().getExternalContext().redirect(url);
    }

    public BigDecimal getTotalPoints() {
        return authenticationBean.getLoggedAccount().getPointsQuantity().add(new BigDecimal("" + this.pointCount));
    }
    
    public String getTotalPrice() {
        return this.pointPrice.multiply(new BigDecimal("" + this.pointCount)).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }
    
    /**
     * @return the pointCount
     */
    public int getPointCount() {
        return pointCount;
    }

    /**
     * @param pointCount the pointCount to set
     */
    public void setPointCount(int pointCount) {
        this.pointCount = pointCount;
    }

    /**
     * @return the pointPrice
     */
    public String getPointPrice() {
        return pointPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }
}
