/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.mif.labanorodraugai.beans;

import io.mif.labanorodraugai.entities.Summerhouse;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Vies
 */

@ManagedBean
@ViewScoped
public class SummerhouseViewController implements Serializable{
    
    private List<Summerhouse> summerhouses;
    
    private Summerhouse selectedSummerhouse;
    
   
    @PersistenceContext
    private EntityManager em;
    
    @PostConstruct
    public void init(){
        Query query = em.createNamedQuery("Summerhouse.findAll");
        summerhouses = query.getResultList();
    }
    
    public List<Summerhouse> getSummerhouses() {
        return summerhouses;
    }
    
    public Summerhouse getSelectedSummerhouse(){
        return selectedSummerhouse;
    }
    
    public void setSelectedSummerhouse(Summerhouse selectedSummerhouse) {
        this.selectedSummerhouse = selectedSummerhouse;
    }
}
