
package io.mif.labanorodraugai.beans;

import io.mif.labanorodraugai.entities.User;
import java.io.Serializable;
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
    private User user = new User(); 
    private String errorMessage = "";
    private String loginParameter="";
    private boolean remember = false;
    private boolean loggedIn=false;
     
    public void login(){

        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

        Query findUser = null;
        
        try{
            user.setPassword(hashBean.HashPassword(user.getPassword()));
        }
        catch (Exception e){
            
        }
        
        if (getLoginParameter().matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
		+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")){
            
            user.setEmail(loginParameter);            
            findUser = em.createNamedQuery("User.findByEmailAndPassword").setParameter("email",this.user.getEmail()).setParameter("password",this.user.getPassword());
                       
        }
        else{
            user.setName(loginParameter);       
            findUser = em.createNamedQuery("User.findByNameAndPassword").setParameter("name",this.user.getName()).setParameter("password",this.user.getPassword());
            
        }
        

        
                
        if (findUser.getResultList().size()>0){
        
            user.setName(((User) findUser.getResultList().get(0)).getName());
            user.setEmail(((User) findUser.getResultList().get(0)).getEmail());
            
            if (this.remember){
                Cookie ckUsername = new Cookie("username", this.user.getName());
                ckUsername.setMaxAge(3600);
                response.addCookie(ckUsername);
                
                Cookie ckPassword= new Cookie("password", this.user.getPassword());
                ckPassword.setMaxAge(3600);
                response.addCookie(ckPassword);
                
                Cookie ckEmail= new Cookie("email",this.user.getEmail());
                ckEmail.setMaxAge(3600);
                response.addCookie(ckEmail);
                
            }
            
            setLoggedIn(true);
            redirect("index.html");
            
            
        } else {
           this.errorMessage= "Klaida! Neteisingi prisijungimo duomenis";
           redirect("login.html");
        }
    }
    
    public void verifyLogin(ComponentSystemEvent event){
        
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        String uri = request.getRequestURI();
           
        boolean loginPage = !(uri.matches("(.*)\\.(.*)") && !uri.matches("(.*)login\\.html(.*)"));
                
        User usr = checkCookie();
        if (usr!=null){
            
            Query query = em.createNamedQuery("User.findByNameAndPassword").setParameter("name",usr.getName()).setParameter("password",usr.getPassword());
                   
            if (query.getResultList().size()>0){
                this.setUser(usr);
                
                if(loginPage) 
                    redirect("index.html");
            }
            else{
                this.errorMessage = "Klaida! Neteisingi prisijungimo duomenis";
                redirect("login.html");
            }
        }

        else if (!loggedIn){
            if(!loginPage){
                this.errorMessage = "Prisijunkite prie sistemos!";
                redirect("login.html");
            }
        }
        else{
            if (loginPage)
                redirect("index.html");
        }
    }
    
    public void logout(){
        
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

        //Remove cookie
        
        for(Cookie ck:request.getCookies()){
            if (ck.getName().equalsIgnoreCase("username")){
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
        redirect("login.html");
        
    }
    
    private void redirect(String page){
        try{
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.getExternalContext().redirect(page);
            
        }
        catch(Exception e){
            
        }
        
    }
    
    private User checkCookie()
    {
        User user = null;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        String username="", password = "", email="";
        Cookie[] cookies = request.getCookies();
        if (cookies!=null){
            for(Cookie ck:cookies){
                if(ck.getName().equalsIgnoreCase("username"))
                    username = ck.getValue();
                if(ck.getName().equalsIgnoreCase("password"))
                    password = ck.getValue();
                if(ck.getName().equalsIgnoreCase("email"))
                    email = ck.getValue();
            }
        }
        
        if(!username.isEmpty() && !password.isEmpty() && !email.isEmpty())
        {
            user = new User(1,username,password);
            user.setEmail((email));
        }
        
        return user;
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
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
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
