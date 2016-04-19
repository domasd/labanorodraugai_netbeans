/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.mif.labanorodraugai.logic;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
/**
 *
 * @author SFILON
 */
public class Converter {
    
    public static final String PREFIX = "stream2file";
    public static final String SUFFIX = ".tmp";
    
    public File stream2file (InputStream in) throws IOException {
        final File tempFile = File.createTempFile(PREFIX, SUFFIX);
        tempFile.deleteOnExit();
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            IOUtils.copy(in, out);
        }
        return tempFile;
    }
    
    public FileInputStream file2stream(File file) throws IOException{
        
        FileInputStream fs = new FileInputStream(file);
        return fs;
    }

}
