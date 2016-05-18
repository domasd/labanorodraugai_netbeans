/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.mif.labanorodraugai.beans;

import io.mif.labanorodraugai.entities.AdditionalServices;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author SFILON
 */
@Named
@SessionScoped
public class AdditionalServicesController implements Serializable  {
    
    private List<AdditionalServices> items;
    
    private List<String> selectedItems;   

    @EJB
    private io.mif.labanorodraugai.beans.AdditionalServicesFacade ejbFacade;
    /**
     * @return the items
     */
    public List<AdditionalServices> getItems() {
               
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    
    }
    /**
     * @param items the items to set
     */
    public void setItems(List<AdditionalServices> items) {
        this.items = items;
    }
    private AdditionalServicesFacade getFacade() {
        return ejbFacade;
    }

    /**
     * @return the selectedItems
     */
    public List<String> getSelectedItems() {
        return selectedItems;
    }

    /**
     * @param selectedItems the selectedItems to set
     */
    public void setSelectedItems(List<String> selectedItems) {
        this.selectedItems = selectedItems;
    }
}
