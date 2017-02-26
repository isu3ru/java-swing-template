/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.db;

import app.config.Config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Isuru
 */
public class Database {

    private static Connection con;
    private static String host;
    private static String port;
    private static String database;
    private static String username;
    private static String password;

    static {
        try {
            Properties config = Config.getConfiguration();
            host = config.getProperty("hostname");
            port = config.getProperty("port");
            database = config.getProperty("database");
            username = config.getProperty("username");
            password = config.getProperty("password");
        } catch (Exception ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true&zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=UTF-8";
        con = DriverManager.getConnection(url, username, password);
        return con;
    }

        }
