package io.mif.labanorodraugai.beans;

import io.mif.labanorodraugai.beans.util.JsfUtil;
import io.mif.labanorodraugai.entities.Account;
import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.inject.Inject;
import io.mif.labanorodraugai.services.PasswordHashService;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import org.apache.commons.io.IOUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author dziaudom
 */
@Named
@Stateful
@RequestScoped
public class ProfileController {

    @Inject
    private AccountController accountController;

    @Inject
    private AuthenticationBean authenticationBean;

    @Inject
    private PasswordHashService passwordHashService;

    private String password1;
    private String password2;
    private String currentPassword;
    private UploadedFile uploadedFile; // TODO move to seperate service

    private Account currentAccount;

    @PostConstruct
    public void init() {
        currentAccount = authenticationBean.getLoggedAccount();
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public void upload() throws IOException { // TODO move to seperate service
        InputStream inputstream = uploadedFile.getInputstream();
        byte[] contents = IOUtils.toByteArray(inputstream);

        currentAccount.setImage(contents);
        accountController.setSelected(currentAccount);
        accountController.update();
    }

    // TODO move to another class - proabably another table and write service
    public StreamedContent getImage() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the HTML. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        } else {
            // So, browser is requesting the image. Return a real StreamedContent with the image bytes.
            String accountId = context.getExternalContext().getRequestParameterMap().get("accountId");
            byte[] imageBytes = currentAccount.getImage();
            return new DefaultStreamedContent(new ByteArrayInputStream(imageBytes));
        }
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getPassword1() {
        return password1;
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public void update() {
        if (!this.isPasswordValid()) {
            return;
        }
        accountController.setSelected(currentAccount);
        accountController.update();
    }

    private boolean isPasswordValid() {
        // check if there is a need to change password
        if (this.password1 != null && !this.password1.isEmpty()) {

            String currentAccountPassword = currentAccount.getPassword();
            if (currentAccountPassword != null && !currentAccountPassword.isEmpty()) {
                if (this.currentPassword == null || this.currentPassword.isEmpty()) {
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/ProfileBundle").getString("BadPassword"));
                    return false;
                }
                String hashedCurrentPassword = passwordHashService.HashPassword(this.currentPassword);
                if (!hashedCurrentPassword.equals(currentAccountPassword)) {
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/ProfileBundle").getString("BadPassword"));
                    return false;
                }
            }

            if (!this.password1.equals(this.password2)) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/ProfileBundle").getString("PasswordMismatch"));
                return false;
            }
            
            String hashedPassword = passwordHashService.HashPassword(this.password1);
            currentAccount.setPassword(hashedPassword);
        }
        return true;
    }
}
