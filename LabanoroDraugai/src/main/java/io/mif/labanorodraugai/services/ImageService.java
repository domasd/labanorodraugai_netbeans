/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.mif.labanorodraugai.services;

import io.mif.labanorodraugai.entities.Account;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author dziaudom
 */
@Named("images")
@ApplicationScoped
public class ImageService {
    
    @PersistenceContext
    EntityManager em;
    
     public StreamedContent getUserImage() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the view. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        }
        else {
            // So, browser is requesting the image. Return a real StreamedContent with the image bytes.
            String id = context.getExternalContext().getRequestParameterMap().get("id");
            byte[] image = em.createNamedQuery("Account.findById", Account.class)
                    .setParameter("id", Integer.valueOf(id))
                    .getSingleResult().getImage();
            return new DefaultStreamedContent(new ByteArrayInputStream(image));
        }
    }
    
}
