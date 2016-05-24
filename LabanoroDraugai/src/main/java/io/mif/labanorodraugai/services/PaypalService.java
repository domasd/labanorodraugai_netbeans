/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.mif.labanorodraugai.services;

import javax.ejb.Stateless;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import com.paypal.base.rest.PayPalResource;
import io.mif.labanorodraugai.utils.GenerateAccessToken;
import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.UUID;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 *
 * @author Vytautas
 */
@Stateless
@Named
public class PaypalService {
    
    public void pay() throws PayPalRESTException {
//        OAuthTokenCredential tokenCredential = Payment.initConfig(new File("../sdk_config.properties"));
        
        ResourceBundle paypalBundle = ResourceBundle.getBundle("/sdk_config");
        
        Map<String, String> map = new HashMap<String, String>();
        map.put("mode", "sandbox");
        String accessToken = new OAuthTokenCredential(paypalBundle.getString("clientId"), paypalBundle.getString("clientSecret"), map).getAccessToken();

        
        String port = ResourceBundle.getBundle("/sdk_config").getString("http.ProxyPort");
    }
    
    
    public Payment createPayment(String paymentId, String approvalPage, List<Item> items, String description) throws PayPalRESTException {
        Map<String, String> map = new HashMap<String, String>();

        PayPalResource.initConfig(convertResourceBundleToProperties(ResourceBundle.getBundle("/sdk_config")));

        Payment createdPayment = null;
        // ###AccessToken
        // Retrieve the access token from
        // OAuthTokenCredential by passing in
        // ClientID and ClientSecret
        APIContext apiContext = null;
        String accessToken = null;

        accessToken = GenerateAccessToken.getAccessToken();

        // ### Api Context
        // Pass in a `ApiContext` object to authenticate
        // the call and to send a unique request id
        // (that ensures idempotency). The SDK generates
        // a request id if you do not pass one explicitly.
        apiContext = new APIContext(accessToken);
        // Use this variant if you want to pass in a request id
        // that is meaningful in your application, ideally
        // a order id.
        /*
			 * String requestId = Long.toString(System.nanoTime(); APIContext
			 * apiContext = new APIContext(accessToken, requestId ));
         */

        // ###Details
        // Let's you specify details of a payment amount.
        Details details = new Details();
        details.setShipping("0");
        details.setTax("0");
        
        String total = calculateTotal(items);
        details.setSubtotal(total);

        // ###Amount
        // Let's you specify a payment amount.
        Amount amount = new Amount();
        amount.setCurrency("EUR");
        // Total must be equal to sum of shipping, tax and subtotal.
        amount.setTotal(total);
        amount.setDetails(details);

        // ###Transaction
        // A transaction defines the contract of a
        // payment - what is the payment for and who
        // is fulfilling it. Transaction is created with
        // a `Payee` and `Amount` types
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription(description);

        // ### Items
        ItemList itemList = new ItemList();
        itemList.setItems(items);

        transaction.setItemList(itemList);

        // The Payment creation API requires a list of
        // Transaction; add the created `Transaction`
        // to a List
        List<Transaction> transactions = new ArrayList<Transaction>();
        transactions.add(transaction);

        // ###Payer
        // A resource representing a Payer that funds a payment
        // Payment Method
        // as 'paypal'
        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        // ###Payment
        // A Payment Resource; create one using
        // the above types and intent as 'sale'
        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        // ###Redirect URLs
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl("http://localhost:8080/LabanoroDraugai/" + approvalPage + "?guid=" + paymentId);
        redirectUrls.setReturnUrl("http://localhost:8080/LabanoroDraugai/" + approvalPage + "?guid=" + paymentId);

        payment.setRedirectUrls(redirectUrls);

        // Create a payment by posting to the APIService
        // using a valid AccessToken
        // The return object contains the status;
        createdPayment = payment.create(apiContext);
        // ###Payment Approval Url

        return createdPayment;
    }

    public Payment executePayment(String payerId, String paymentId) throws PayPalRESTException {

        PayPalResource.initConfig(convertResourceBundleToProperties(ResourceBundle.getBundle("/sdk_config")));

        Payment createdPayment = null;
        // ###AccessToken
        // Retrieve the access token from
        // OAuthTokenCredential by passing in
        // ClientID and ClientSecret
        APIContext apiContext = null;
        String accessToken = null;
        
        accessToken = GenerateAccessToken.getAccessToken();

        // ### Api Context
        // Pass in a `ApiContext` object to authenticate
        // the call and to send a unique request id
        // (that ensures idempotency). The SDK generates
        // a request id if you do not pass one explicitly.
        apiContext = new APIContext(accessToken);
        // Use this variant if you want to pass in a request id
        // that is meaningful in your application, ideally
        // a order id.
        /*
                     * String requestId = Long.toString(System.nanoTime(); APIContext
                     * apiContext = new APIContext(accessToken, requestId ));
         */
        
        
        Payment payment = new Payment();
        if (paymentId != null) {
            payment.setId(paymentId); 
        }

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);
        createdPayment = payment.execute(apiContext, paymentExecution);

        return createdPayment;
    }

    public String getPaymentApprovalUrl(Payment payment) {
        Iterator<Links> links = payment.getLinks().iterator();
        while (links.hasNext()) {
            Links link = links.next();
            if (link.getRel().equalsIgnoreCase("approval_url")) {
                return link.getHref();
            }
        }
        
        return null;
    }
    
    public String calculateTotal(List<Item> items) {
        BigDecimal total = BigDecimal.ZERO;
        for (Item item : items) {
            total = total.add(new BigDecimal(item.getPrice()).multiply(new BigDecimal(item.getQuantity())));
        }
        return total.toString();
    }
    
    private Properties convertResourceBundleToProperties(ResourceBundle resource) {
        Properties properties = new Properties();

        Enumeration keys = resource.getKeys();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            properties.put(key, resource.getString(key));
        }

        return properties;
    }
}
