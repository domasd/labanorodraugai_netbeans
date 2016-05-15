package io.mif.labanorodraugai.utils;

import io.mif.labanorodraugai.beans.AccountFacade;
import io.mif.labanorodraugai.beans.AuthenticationBean;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author dziaudom
 */
@Named
@Stateless
public class EmailValidator implements Validator {

    @Inject
    private AccountFacade accountFacade;

    @Inject
    private AuthenticationBean authenticationBean;

    private Pattern pattern;
    private Matcher matcher;

    public EmailValidator() {
        pattern = Pattern.compile(ConstantsBean.REGEX_EMAIL);
    }

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        int userId = (int) component.getAttributes().get("userId"); 
        matcher = pattern.matcher(value.toString());
        if (!matcher.matches()) {
            String badInputMsg = String.format("%1$s %2$s",
                    ResourceBundle.getBundle("/CommonBundle").getString("BadInputForField"),
                    ResourceBundle.getBundle("/AccountBundle").getString("Title_email"));
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, badInputMsg, null);
            throw new ValidatorException(facesMsg);
        }

        boolean isNotUnique = accountFacade.doesSameEmailExist((String) value, userId);
        if (isNotUnique) {
            String alreadyExistsMsg = ResourceBundle.getBundle("/CommonBundle").getString("EmailAlreadyExists");
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, alreadyExistsMsg, null);
            throw new ValidatorException(msg);
        }
    }

}
