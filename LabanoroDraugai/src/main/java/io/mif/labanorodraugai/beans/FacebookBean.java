package io.mif.labanorodraugai.beans;

import io.mif.labanorodraugai.beans.registration.RegistrationController;
import io.mif.labanorodraugai.entities.Account;
import io.mif.labanorodraugai.entities.enums.AccountStatus;
import java.math.BigDecimal;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Named
@RequestScoped
public class FacebookBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private AuthenticationBean authenticationBean;

    @Inject
    private RegistrationController registrationController;

    @Inject
    private AccountFacade accountFacade;

    public String authenticate() {
        String email = FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("email");
        String firstName = FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("first_name");
        String lastname = FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("last_name");

        Account account = new Account();
        account.setPointsQuantity(BigDecimal.ZERO);
        account.setStatus(AccountStatus.Candidate);
        account.setEmail(email);
        account.setName(firstName);
        account.setLastname(lastname);

        Account retrievedFromDb = (Account) accountFacade.getAccountByEmail(account.getEmail());
        if (retrievedFromDb != null) {
            authenticationBean.setLoggedAccount(retrievedFromDb);
        } else {
            registrationController.setAccount(account);
            registrationController.registerAccount();
            authenticationBean.setLoggedAccount(account);
        }

        return "../index.html?faces-redirect=true";
    }

    public String Login() {
        return "../index.html?faces-redirect=true";
    }
}
