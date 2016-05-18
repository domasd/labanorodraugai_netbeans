/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.mif.labanorodraugai.beans;

import io.mif.labanorodraugai.entities.Account;
import io.mif.labanorodraugai.entities.AdditionalServices;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author SFILON
 */
@Stateless
public class AdditionalServicesFacade extends AbstractFacade<AdditionalServices> {

    @PersistenceContext(unitName = "io.mif_LabanoroDraugai_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    public AdditionalServicesFacade() {
        super(AdditionalServices.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
