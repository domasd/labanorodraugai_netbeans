/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.mif.labanorodraugai.services;

import io.mif.labanorodraugai.entities.AccountApproval;
import io.mif.labanorodraugai.entities.Account;
import java.net.MalformedURLException;
import javax.ejb.Stateless;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailConstants;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

/**
 *
 * @author Vytautas
 */
@Stateless
public class EmailService {
        
    public void SendInvitationEmail(Account sender, String toEmail) throws EmailException, MalformedURLException {
        //ImageHtmlEmail email = new ImageHtmlEmail();
        HtmlEmail email = new HtmlEmail();
        setUpHtmlEmail(email);
        email.addTo(toEmail);
        email.setSubject("Labanoro draugų kvietimas");        
        
        StringBuilder msg = new StringBuilder();
        //msg.append("<div><img src=\"" + baseUrl + "/images/lab  anorodraugai.JPG\"></div>");
        msg.append("<h4>Sveiki,</h4>");
        msg.append("<p>" + sender.getName() + " " + sender.getLastname() + " kviečia Jus tapti bendrijos Labanoro draugai nariu!</p>");
        msg.append("<p>Tą padaryti galite užsiregistravę ");
        msg.append("<a href=" + "\"http://localhost:8080/LabanoroDraugai/registration/registration.html\">");
        msg.append("čia</a></p>");
        
        email.setContent(msg.toString(), EmailConstants.TEXT_HTML);

        email.send();
    }

    
    private void setUpHtmlEmail(HtmlEmail email) throws EmailException {
        email.setHostName("smtp.googlemail.com");
        email.setSmtpPort(465);
        email.setAuthenticator(new DefaultAuthenticator("labanorodraugaibendrija@gmail.com", "labanoro123"));
        email.setSSLOnConnect(true);
        email.setCharset("UTF-8");
        email.setFrom("labanorodraugaibendrija@gmail.com", "Labanoro draugai");
    }

    public void sendApprovalRequestEmail(AccountApproval approval) throws EmailException, MalformedURLException {
        HtmlEmail email = new HtmlEmail();
        setUpHtmlEmail(email);
        email.addTo(approval.getApprover().getEmail());
        email.setSubject("Naujo nario rekomendacija");        
        
        Account candidate = approval.getCandidate();
        
        StringBuilder msg = new StringBuilder();
        //msg.append("<div><img src=\"" + baseUrl + "/images/lab  anorodraugai.JPG\"></div>");
        msg.append("<h4>Sveiki,</h4>");
        msg.append("<p>" + candidate.getName() + " " + candidate.getLastname() + " (" + candidate.getEmail() + ") nori tapti Labanoro Draugai nariu ir prašo Jūsų suteikti rekomendaciją!</p>");
        msg.append("<p>Tą padaryti galite: ");
        msg.append("<a href=" + "\"http://localhost:8080/LabanoroDraugai/registration/approval.html?gen=" + approval.getGeneratedId() + "\">");
        msg.append("čia</a></p>");
        
        email.setContent(msg.toString(), EmailConstants.TEXT_HTML);

        email.send();
    }
}
