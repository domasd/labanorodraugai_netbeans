package io.mif.labanorodraugai.utils;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;


/**
 *
 * @author dziaudom
 */

@Named("constants")
@ApplicationScoped
public class ConstantsBean {
    
    // global compile-time constants 
    public final static String REGEX_EMAIL = "^[A-Za-z0-9](([_\\.\\-]?[a-zA-Z0-9]+)*)@([A-Za-z0-9]+)(([\\.\\-]?[a-zA-Z0-9]+)*)\\.([A-Za-z]{2,})$";
    
    // Accessors for EL in views
    String regex_email = REGEX_EMAIL;

    public String getRegex_email() {
        return regex_email;
    }
    
}
