/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.config;

import app.views.PropertiesEditor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.swing.JOptionPane;

/**
 *
 * @author Isuru
 */
public class Config {

    static Properties p;

    public static boolean isValidConfigurationFound() {
        p = new Properties();
        try {
            p.load(new FileInputStream("config.bin"));
            return p.containsKey("hostname");
        } catch (Exception ex) {
            return false;
        }
    }

    public static void createConfig() {
        try {
            File cf = new File("config.bin");
            if (!cf.exists()) {
                cf.createNewFile();
            }
            JOptionPane.showMessageDialog(null, "No valid configuration data found.\nPlease create a new connection profile in the next window.", "Not Found.", JOptionPane.WARNING_MESSAGE);
            PropertiesEditor pe = new PropertiesEditor(null, true);
            pe.pack();
            pe.setVisible(true);
        } catch (Exception e) {
        }
    }

    public static Properties getConfiguration() throws Exception {
        return p;
    }

    public static boolean setConfiguration(Properties prop) throws Exception {
        p = prop;
        File cf = new File("config.bin");
        if (!cf.exists()) {
            cf.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream("config.bin");
        p.store(fos, "DO NOT DELETE THIS FILE. (C) BY SAMILA WIJERATHNA\nCreated At: " + new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        return true;
    }

    public static byte[] serializeToBytes(Object obj) throws Exception {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream o = new ObjectOutputStream(b);
        o.writeObject(obj);
        return b.toByteArray();
    }

    public static Object deserializeFromBytes(byte[] bytes) throws Exception {
        ByteArrayInputStream b = new ByteArrayInputStream(bytes);
        ObjectInputStream o = new ObjectInputStream(b);
        return o.readObject();
    }

}
