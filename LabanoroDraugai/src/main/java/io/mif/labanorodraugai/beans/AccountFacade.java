/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.mif.labanorodraugai.beans;

import io.mif.labanorodraugai.entities.Account;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
        Long count = (Long) em.createNamedQuery("Account.doesSameEmailExist")
                .setParameter("email", email)
                .setParameter("userId", userId)
                .getSingleResult();
        return count > 0;
    }

    public Account getAccountByEmail(String email) {
        Query existing = em
                .createNamedQuery("Account.findByEmail")
                .setParameter("email", email);

        List<Account> accountList = existing.getResultList();
        if (accountList.size() > 0) {
            return (Account) accountList.get(0);
        } else{
            return null;
        }
    }
}
