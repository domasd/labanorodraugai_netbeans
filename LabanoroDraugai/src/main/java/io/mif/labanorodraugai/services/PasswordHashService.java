/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.mif.labanorodraugai.services;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;

/**
 *
 * @author Vytautas
 */
@Stateless
public class PasswordHashService {

    public String HashPassword(String password) {
        MessageDigest md;
        StringBuilder sb = new StringBuilder();
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] b = md.digest();
            for (byte b1 : b) {
                sb.append(Integer.toHexString(b1 & 0xff));
            }
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(PasswordHashService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sb.toString();
    }
    
}
