/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.controllers;

import app.models.UtilityModel;

/**
 *
 * @author Isuru
 */
public class UtilityController {

    public static boolean isPropertyTypeAvailable(int propt) {
        try {
            return UtilityModel.isPropertyTypeAvailable(propt);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean truncateDatabaseTable(String table_name) {
        try {
            return UtilityModel.truncateDatabaseTable(table_name);
        } catch (Exception e) {
            return false;
        }
    }

}
