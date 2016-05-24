/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.mif.labanorodraugai.beans;

import io.mif.labanorodraugai.entities.Config;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author dziaudom
 */
@Stateless
public class AdministrationFacade extends AbstractFacade<Config>{
    @PersistenceContext
    EntityManager em;


    public AdministrationFacade() {
        super(Config.class);
    }
    
    public Config getConfig(){
        return em.createNamedQuery("Config.getConfig", Config.class)
                .getSingleResult();
    }

    @Override
    protected EntityManager getEntityManager() {
       return em;
    }
    
    
   
    
}
