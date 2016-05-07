/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.mif.labanorodraugai.filters;

import io.mif.labanorodraugai.beans.SessionBean;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

/**
 *
 * @author SFILON
 */
public class LoginFilter implements Filter{

   
    private final ArrayList<String> pagesMustNotBeAuthenticated = new ArrayList<String>() {{
        add("registration.html");
        add("login.html");
    }};
    
    
    private final ArrayList<String> pagesDontNeedToBeAuthenticated = new ArrayList<String>() {{
        add("index.hmtl");
        add("registration.html");
        add("login.html");
        add("logout.html");
        add("javax");
    }};
    
    public boolean checkIfUrlDontNeedToBeAuthenticated(String url){
                
      if (!url.matches("(.*)\\.(.*)")) return true;
      
      for (String page  : pagesDontNeedToBeAuthenticated) {
         if (url.indexOf(page)>=0) return true;
      }
      
      return false;
    }
    
    public boolean checkIfUrlMustNotBeAuthentucated(String url){
        
      for (String page  : pagesMustNotBeAuthenticated) {
         if (url.indexOf(page)>=0) return true;
      }
      
      return false;
    }
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        SessionBean session = (SessionBean) req.getSession().getAttribute("authenticationBean");
        String url = req.getRequestURI();
        
        if (session==null || !session.isLogged){
            
            // jeigu bando nera sessijos, bet zmogus bando ieiti i puslapi
            // kur jama negalima arba bando atsijungti 
            
            if (!checkIfUrlDontNeedToBeAuthenticated(url)){
                resp.sendRedirect(req.getServletContext().getContextPath() + "/login/login.html");
            }
            
            else {
                chain.doFilter(request, response);
            }
        } else {
            
            // jeigu sessija yra, o zmogus bando ieiti i puslapius, kur galima patekti tik neprisiloginusiems
            
            if (checkIfUrlMustNotBeAuthentucated(url)){
                resp.sendRedirect(req.getServletContext().getContextPath()+"/index.html");
            } 
                
            // jeigu sessija yra ir zmogus nori atsijungti
            else if (url.indexOf("logout.html")>=0) {
                req.getSession().removeAttribute("authenticationBean");
                resp.sendRedirect(req.getServletContext().getContextPath() + "/login/login.html");
            } else{
                chain.doFilter(request, response);
            }        
        }
    }
   
    @Override
    public void destroy() {
    }
    
}
