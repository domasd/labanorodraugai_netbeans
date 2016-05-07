/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.mif.labanorodraugai.beans;

import io.mif.labanorodraugai.entities.Summerhouse;
import io.mif.labanorodraugai.entities.SummerhouseReservation;
import java.util.List;
import javax.ejb.Stateless;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

/**
 *
 * @author SFILON
 */

@Stateless
public class SummerhouseFacade extends AbstractFacade<Summerhouse> {
    
    @PersistenceContext(unitName = "io.mif_LabanoroDraugai_war_1.0-SNAPSHOTPU")   
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SummerhouseFacade() {
        super(Summerhouse.class);
    }
    
    public List<SummerhouseReservation> summerhouseReservationList(int id){
        return em.createNamedQuery("SummerhouseReservation.findBySummerhouseID").setParameter("summerhouseID",id).getResultList();
    }
    
}
