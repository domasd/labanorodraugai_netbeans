/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.mif.labanorodraugai.beans;

import io.mif.labanorodraugai.entities.Account;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author dziaudom
 */
@Stateless
public class AccountFacade extends AbstractFacade<Account> {

    @PersistenceContext(unitName = "io.mif_LabanoroDraugai_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    public AccountFacade() {
        super(Account.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public boolean doesSameEmailExist(String email, int userId) {
        Long count = (Long)em.createNamedQuery("Account.doesSameEmailExist")
                .setParameter("email", email)
                .setParameter("userId", userId)
                .getSingleResult();
        return count > 0;
    }

}
