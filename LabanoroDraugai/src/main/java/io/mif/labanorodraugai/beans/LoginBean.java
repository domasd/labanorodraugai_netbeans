
package io.mif.labanorodraugai.beans;

import io.mif.labanorodraugai.entities.Account;
import io.mif.labanorodraugai.entities.enums.AccountStatus;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.ejb.Stateful;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

/**
 *
 * @author SFILON
 */
@ManagedBean (name="loginBean")
@SessionScoped
@Stateful
public class LoginBean implements Serializable {
    
    @Inject
    HashBean hashBean;
    
    @PersistenceContext
    private EntityManager em;
    private Account account = new Account(); 
    private String errorMessage = "";
    private String loginParameter="";
    private boolean remember = false;
    private boolean loggedIn=false;
     
    public void login(){

        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

        Query findAccount = null;
        
        try{
            account.setPassword(hashBean.HashPassword(account.getPassword()));
        }
        catch (Exception e){
            
        }
        
        if (getLoginParameter().matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
		+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")){
            
            account.setEmail(loginParameter);            
            findAccount = em.createNamedQuery("Account.findByEmailAndPassword").setParameter("email",this.account.getEmail()).setParameter("password",this.account.getPassword());
                       
        }
        else{
            account.setName(loginParameter);       
            findAccount = em.createNamedQuery("Account.findByNameAndPassword").setParameter("name",this.account.getName()).setParameter("password",this.account.getPassword());
            
        }
        

        
                
        if (findAccount.getResultList().size()>0){
        
            account.setName(((Account) findAccount.getResultList().get(0)).getName());
            account.setEmail(((Account) findAccount.getResultList().get(0)).getEmail());
            
            if (this.remember){
                Cookie ckUsername = new Cookie("name", this.account.getName());
                ckUsername.setMaxAge(3600);
                response.addCookie(ckUsername);
                
                Cookie ckPassword= new Cookie("password", this.account.getPassword());
                ckPassword.setMaxAge(3600);
                response.addCookie(ckPassword);
                
                Cookie ckEmail= new Cookie("email",this.account.getEmail());
                ckEmail.setMaxAge(3600);
                response.addCookie(ckEmail);
                
            }
            
            setLoggedIn(true);
            redirect(facesContext.getExternalContext().getApplicationContextPath() + "/index.html");
            
            
        } else {
           this.errorMessage= "Klaida! Neteisingi prisijungimo duomenis";
           redirect(facesContext.getExternalContext().getApplicationContextPath() + "/login/login.html");
        }
    }
    
    public void verifyLogin(ComponentSystemEvent event){
        
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        String uri = request.getRequestURI();
           
        boolean loginPage = !(uri.matches("(.*)\\.(.*)") && !uri.matches("(.*)login\\.html(.*)"));
                
        Account acc = checkCookie();
        if (acc!=null){
            
            Query query = em.createNamedQuery("Account.findByNameAndPassword").setParameter("name",acc.getName()).setParameter("password",acc.getPassword());
                   
            if (query.getResultList().size()>0){
                this.setAccount(acc);
                
                if(loginPage) 
                    redirect(facesContext.getExternalContext().getApplicationContextPath() + "/index.html");
            }
            else{
                this.errorMessage = "Klaida! Neteisingi prisijungimo duomenis";
                redirect(facesContext.getExternalContext().getApplicationContextPath() + "/login/login.html");
            }
        }

        else if (!loggedIn){
            if(!loginPage){
                this.errorMessage = "Prisijunkite prie sistemos!";
                redirect(facesContext.getExternalContext().getApplicationContextPath() + "/login/login.html");
            }
        }
        else{
            if (loginPage)
                redirect(facesContext.getExternalContext().getApplicationContextPath() + "/index.html");
        }
    }
    
    public void logout(){
        
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

        //Remove cookie
        
        for(Cookie ck:request.getCookies()){
            if (ck.getName().equalsIgnoreCase("name")){
                ck.setMaxAge(0);
                response.addCookie(ck);
            }
            
            if (ck.getName().equalsIgnoreCase("password")){
                ck.setMaxAge(0);
                response.addCookie(ck);
            }
            
            if (ck.getName().equalsIgnoreCase("email")){
                ck.setMaxAge(0);
                response.addCookie(ck);
            }
        }
        setLoggedIn(false);
        redirect(facesContext.getExternalContext().getApplicationContextPath() + "/login/login.html");
        
    }
    
    private void redirect(String page){
        try{
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.getExternalContext().redirect(page);
            
        }
        catch(Exception e){
            
        }
        
    }
    
    private Account checkCookie()
    {
        Account account = null;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        String name="", password = "", email="";
        Cookie[] cookies = request.getCookies();
        if (cookies!=null){
            for(Cookie ck:cookies){
                if(ck.getName().equalsIgnoreCase("name"))
                    name = ck.getValue();
                if(ck.getName().equalsIgnoreCase("password"))
                    password = ck.getValue();
                if(ck.getName().equalsIgnoreCase("email"))
                    email = ck.getValue();
            }
        }
        
        if(!name.isEmpty() && !password.isEmpty() && !email.isEmpty())
        {
            account = new Account(1, password, name, AccountStatus.Candidate, email, BigDecimal.valueOf(123456789.123456789));
        }
        
        return account;
    }
    
    /**
     * @return the errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @param errorMessage the errorMessage to set
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * @return the account
     */
    public Account getAccount() {
        return account;
    }

    /**
     * @param account the account to set
     */
    public void setAccount(Account account) {
        this.account = account;
    }

    /**
     * @return the remember
     */
    public boolean isRemember() {
        return remember;
    }

    /**
     * @param remember the remember to set
     */
    public void setRemember(boolean remember) {
        this.remember = remember;
    }

    /**
     * @return the loginParameter
     */
    public String getLoginParameter() {
        return loginParameter;
    }

    /**
     * @param loginParameter the loginParameter to set
     */
    public void setLoginParameter(String loginParameter) {
        this.loginParameter = loginParameter;
    }

    /**
     * @return the loggedIn
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * @param loggedIn the loggedIn to set
     */
    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
}
