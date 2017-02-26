/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.db.backup;

import app.db.Database;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 *
 * @author Isuru Ranawaka
 */
public class DatabaseUtils {

//    private static final int BUFFER = 999999999;

    /**
     *
     */
    public DatabaseUtils() {
    }

    /**
     * Clear the database table if the table is available from the current
     * connection
     *
     * Returns <b>true</b> if the table was found and cleared otherwise returns
     * 'false' if the table was not found and not cleared
     *
     * available at <i>Database utils</i><br/>
     *
     * © Isuru Ranawaka - isu3ru@gmail.com
     *
     * @param tableName
     * @return
     */
    public static boolean emptyTable(String tableName) {
        boolean state;
        try {
            Connection con = Database.getConnection();
            PreparedStatement clearTable = con.prepareStatement("TRUNCATE TABLE `" + tableName + "`");
            clearTable.executeUpdate();
            clearTable.close();
            state = true;
        } catch (Exception e) {
            state = false;
        }
        return state;
    }

    public static boolean backupDatabase(String fileName) throws Exception {
//        String db = Config.getConfiguration().getProperty("database");
        DumpDB dd = new DumpDB();
        File f = new File(fileName);
        String zipname = f.getParent() + File.separator +  f.getName() + ".zip";
        return dd.createZipFile(zipname, fileName);
    }
}
