/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.mif.labanorodraugai.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author SFILON
 */
public class CalendarUtils {
    
    public static Date getDate(int year, int month, int day){
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
  
        return cal.getTime();
    }
    
    public static Date addDays(Date date, int daysToAdd){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, daysToAdd);
        return cal.getTime();
    }
    
    public static int countDaysBetweenDatesBySpecificYear(Date beginDate, Date endDate, int year){
        
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
                
        int count=0;
        
        if (Integer.parseInt(yearFormat.format(beginDate))==year){
            count=1;
        }
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(beginDate);
            
        while (cal.getTime().before(endDate)) {
                 cal.add(Calendar.DATE, 1);
                 if (Integer.parseInt(yearFormat.format(beginDate))==year){
                     count++;
                 }                 
        }
        
        return count;
    }
    
    public static List<String> getDatesBetweenDates(Date beginDate, Date endDate, SimpleDateFormat dateFormat){
        
        List<String> answer = new ArrayList<>();
        
        answer.add(dateFormat.format(beginDate));
            
        Calendar cal = Calendar.getInstance();
        cal.setTime(beginDate);
            
        while (cal.getTime().before(endDate)) {
                 cal.add(Calendar.DATE, 1);
                 String formatted = dateFormat.format(cal.getTime());
                 answer.add(formatted);
        } 
            
        return answer;
    }
    
    public static List<Date> getDatesBetweenDates(Date beginDate, Date endDate){
        
        List<Date> answer = new ArrayList<>();
        
        answer.add(beginDate);
            
        Calendar cal = Calendar.getInstance();
        cal.setTime(beginDate);
            
        while (cal.getTime().before(endDate)) {
                 cal.add(Calendar.DATE, 1);
                 answer.add(cal.getTime());
        } 
            
        return answer;
    }
    
}
