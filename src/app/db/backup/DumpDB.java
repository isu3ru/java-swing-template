/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.db.backup;

import app.config.Config;
import app.db.Database;
import app.db.MySQLUtils;
import app.helpers.Utilities;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author Isuru Ranawaka
 */
public class DumpDB {

    private static String dbname;

    public DumpDB() {
        try {
            dbname = Config.getConfiguration().getProperty("database");
        } catch (Exception e) {
        }
    }

    private static String getColumnsString(String tbl) {
        String bl = "";
        try {
            ArrayList<String> colslist = new ArrayList<String>();
            ResultSet cols = Database.getConnection().prepareStatement("SHOW COLUMNS FROM `" + tbl + "`").executeQuery();
            while (cols.next()) {
                colslist.add(cols.getString(1));
            }
            ArrayList<String> sql_cols = new ArrayList<String>();
            for (String s : colslist) {
                sql_cols.add("`" + s + "`");
            }
            bl = Utilities.implode(",", sql_cols);
        } catch (Exception e) {
        }
        return bl;
    }

    private static String getCombinedInsertsForTable(Connection c, String tbl, int limit) {
        StringBuilder sb = new StringBuilder();

        try {
            ResultSet rsalt = c.createStatement().executeQuery("SELECT * FROM `" + tbl + "`");
            rsalt.last();
            int numberOfRows = rsalt.getRow();
            if (numberOfRows > 0) {
                sb.append("INSERT INTO `").append(tbl).append("`(").append(getColumnsString(tbl)).append(")\n VALUES");
            }
            int colCount = rsalt.getMetaData().getColumnCount();
            rsalt.beforeFirst();
            ArrayList datacs = new ArrayList();
            while (rsalt.next()) {

                if (rsalt.getRow() % limit == 0) {
                    sb.append(Utilities.implode(",", datacs)).append(";\n");
                    datacs = new ArrayList();
                    sb.append("\nINSERT INTO `").append(tbl).append("`(").append(getColumnsString(tbl)).append(")\n VALUES");
                }

                ArrayList al = new ArrayList();
                for (int i = 0; i < colCount; i++) {
                    String s = "";
                    try {
                        al.add("'" + MySQLUtils.mysql_real_escape_string(c, rsalt.getObject(i + 1).toString()) + "'");
                    } catch (Exception e) {
                        al.add("''");
                    }
                    sb.append(s);
                }

                datacs.add("(" + Utilities.implode(",", al) + ")");
            }
            sb.append(Utilities.implode(",", datacs)).append(";\n");
        } catch (Exception e) {
        }
        return sb.toString();
    }

    public boolean dumpDatabase(String database, String Filename, boolean database_create, boolean drops, boolean seperateInserts, int limit) {
        try {
            FileWriter fw = new FileWriter(Filename);
            BufferedWriter buff = new BufferedWriter(fw);

            StringBuilder sb = new StringBuilder();
            Connection c = Database.getConnection();

            sb.append("--\n").append("-- -------------------------------------\n").append("-- JDBCup Database Backup Utility\n")
                    .append("-- By Isuru Ranawaka - isu3ru@gmail.com\n")
                    .append("-- Created on ")
                    .append(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a").format(new Date())).append("\n")
                    .append("-- -------------------------------------\n"
                            + "--\n\n");

            if (database_create) {
                sb.append("CREATE DATABASE IF NOT EXISTS `").append(database).append("`;\n\n");
                buff.append(sb);
                sb = new StringBuilder();
            }

            sb.append("SET FOREIGN_KEY_CHECKS=0;");
            sb.append("\n\n");

            ResultSet rs = c.createStatement().executeQuery("SHOW FULL TABLES WHERE Table_type != 'VIEW'");
            while (rs.next()) {
                String tbl = rs.getString(1);
                sb.append("\n");
                sb.append("-- ----------------------------\n")
                        .append("-- Table structure for `").append(tbl)
                        .append("`\n-- ----------------------------\n");

                if (drops) {
                    sb.append("DROP TABLE IF EXISTS `").append(tbl).append("`;\n");
                }

                ResultSet rs2 = c.createStatement().executeQuery("SHOW CREATE TABLE `" + tbl + "`");
                rs2.next();
                String crt = rs2.getString(2) + ";";
                sb.append(crt).append("\n");
                sb.append("\n");
                sb.append("-- ----------------------------\n").append("-- Records for `").append(tbl).append("`\n-- ----------------------------\n");

                buff.append(sb.toString());
                sb = new StringBuilder();

                if (seperateInserts) {
                    /*
                     * Outputs seperate inserts
                     */
                    //<editor-fold defaultstate="collapsed" desc="Seperate Inserts">
                    try {
                        ResultSet rss = c.createStatement().executeQuery("SELECT * FROM `" + tbl + "`");
                        int colCount = rss.getMetaData().getColumnCount();
                        if (colCount > 0) {
                            while (rss.next()) {

                                if (rss.getRow() % 500 == 0) {
                                    buff.append(sb.toString());
                                    sb = new StringBuilder();
                                }

                                sb.append("INSERT INTO ").append("`").append(tbl).append("`").append(" VALUES(");
                                for (int i = 0; i < colCount; i++) {
                                    if (i > 0) {
                                        sb.append(",");
                                    }
                                    String s = "";
                                    try {
                                        s += "'";
                                        s += MySQLUtils.mysql_real_escape_string(c, rss.getObject(i + 1).toString());
                                        s += "'";
                                    } catch (Exception e) {
                                        s = "''";
                                    }
                                    sb.append(s);
                                }
                                sb.append(");\n");
                            }
                        }
                    } catch (Exception e) {
                    }
                    //</editor-fold>
                } else {
                    /*
                     * Outputs combined inserts
                     */
                    //<editor-fold defaultstate="collapsed" desc="Combined Inserts">
                    buff.append(getCombinedInsertsForTable(c, tbl, limit));
                    //</editor-fold>
                }

            }

            System.gc();
            sb = new StringBuilder();

            ResultSet rs2 = c.createStatement().executeQuery("SHOW FULL TABLES WHERE Table_type = 'VIEW'");
            while (rs2.next()) {
                String tbl = rs2.getString(1);

                sb.append("\n");
                sb.append("-- ----------------------------\n")
                        .append("-- View structure for `").append(tbl)
                        .append("`\n-- ----------------------------\n");
                sb.append("DROP VIEW IF EXISTS `").append(tbl).append("`;\n");
                ResultSet rs3 = c.createStatement().executeQuery("SHOW CREATE VIEW `" + tbl + "`");
                rs3.next();
                String crt = rs3.getString(2) + ";";
                sb.append(crt).append("\n");

                buff.append(sb.toString());
                sb = new StringBuilder();
            }

            buff.flush();
            buff.close();
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    public boolean createZipFile(String zipf, String filename) {
        try {
            File dump = File.createTempFile(filename, ".wbk");
//            System.out.println("createing bkp file at " + filename);
            dumpDatabase(dbname, dump.getAbsolutePath(), true, true, false, 500);
//            System.out.println("creating zip file at " + zipf);
            FileOutputStream fos = new FileOutputStream(zipf);
            ZipOutputStream zos = new ZipOutputStream(fos);
            int bytesRead;
            byte[] buffer = new byte[1024];
            CRC32 crc = new CRC32();

            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(dump));
            crc.reset();
            while ((bytesRead = bis.read(buffer)) != -1) {
                crc.update(buffer, 0, bytesRead);
            }
            bis.close();
            // Reset to beginning of input stream
            bis = new BufferedInputStream(new FileInputStream(dump));
            ZipEntry entry = new ZipEntry(dump.getName());
            entry.setMethod(ZipEntry.DEFLATED);
//            entry.setCompressedSize(file.length()); //only for STORED method
            entry.setSize(dump.length());
            entry.setCrc(crc.getValue());
            zos.putNextEntry(entry);
            while ((bytesRead = bis.read(buffer)) != -1) {
                zos.write(buffer, 0, bytesRead);
            }
            bis.close();
            zos.flush();
            zos.finish();
            zos.close();
//            System.out.println("backup done!");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
