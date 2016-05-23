/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.mif.labanorodraugai.services;

import java.math.BigDecimal;

/**
 *
 * @author SFILON
 */
public interface IPointsService {
    BigDecimal calculateSummerhousePoints(int numberOfDays);
    BigDecimal calculateAdditionalServicesPoints(int numberOfDays);
    boolean makeTransaction(BigDecimal pointAmount);
}
