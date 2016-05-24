/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.mif.labanorodraugai.interceptors;

import io.mif.labanorodraugai.interceptors.Log;
import io.mif.labanorodraugai.beans.AuthenticationBean;
import io.mif.labanorodraugai.beans.SummerhouseController;
import io.mif.labanorodraugai.entities.enums.AccountStatus;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 *
 * @author SFILON
 */
@Interceptor
@Log
public class LoggingInterceptor implements Serializable {
   
   @Inject
   private AuthenticationBean authenticationBean;
    
   @Inject
   private SummerhouseController summerhouseController;
   
   @AroundInvoke
   public Object logMethodEntry(InvocationContext ctx) throws Exception{
      
       String methodName = ctx.getMethod().getName();
       String className = ctx.getClass().getName();
       
       switch(methodName){
           case "makeTransaction":
                Date current = new Date();
                SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
                String accountEmail = authenticationBean.getLoggedAccount().getEmail();
                
                AccountStatus status = authenticationBean.getLoggedAccount().getStatus();
                String statusString="";
                switch(status){
                    case Member:
                        statusString="Member";
                        break;
                    case Admin:
                        statusString="Administrator";
                        break;
                    default:
                        statusString="Unknown";
                        break;
                }
                
                int summerhouseNumber = summerhouseController.getSelected().getNumber();
                Object[] params = ctx.getParameters();
                BigDecimal amount= (BigDecimal) params[0];
                String stringToLog = "Date: "+dateFormat.format(current)+"Class: "+className+" Method: "+methodName+" - User: "+accountEmail+" Role: "+ statusString +" Summerhouse:"+summerhouseNumber+" Points:"+amount.doubleValue(); 
                
                try(FileWriter fw = new FileWriter("C:\\LabanoroLogs\\log.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) {
                    out.println(stringToLog);                       
                } catch (IOException e) {
                    
                }                   
               break;
           default:
               break;
       }
       
      return ctx.proceed();
   }
}
