/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.models;

import app.db.Database;
import java.sql.Connection;

/**
 *
 * @author Isuru
 */
public class Model {

    /**
     * Database connection
     */
    protected static Connection con;

    /**
     * initialize connection
     */
    static {
        try {
            con = Database.getConnection();
        } catch (Exception e) {
        }
    }
}
