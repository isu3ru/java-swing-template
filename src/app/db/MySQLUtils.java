/**
 * Mysql Utilities
 *
 * @author Ralph Ritoch <rritoch@gmail.com>
 * @copyright Ralph Ritoch 2011 ALL RIGHTS RESERVED
 * @link http://www.vnetpublishing.com
 *
 */
package app.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class MySQLUtils {

    /**
     * Escape string to protected against SQL Injection
     *
     * You must add a single quote ' around the result of this function for
     * data, or a backtick ` around table and row identifiers. If this function
     * returns null than the result should be changed to "NULL" without any
     * quote or backtick.
     *
     * @param link
     * @param str
     * @return
     * @throws Exception
     */
    public static String mysql_real_escape_string(java.sql.Connection link, String str)
            throws Exception {
        if (str == null) {
            return null;
        }

        if (str.replaceAll("[a-zA-Z0-9_!@#$%^&*()-=+~.;:,\\Q[\\E\\Q]\\E<>{}\\/? ]", "").length() < 1) {
            return str;
        }

        String clean_string = str;
        clean_string = clean_string.replaceAll("\\\\", "\\\\\\\\");
        clean_string = clean_string.replaceAll("\\n", "\\\\n");
        clean_string = clean_string.replaceAll("\\r", "\\\\r");
        clean_string = clean_string.replaceAll("\\t", "\\\\t");
        clean_string = clean_string.replaceAll("\\00", "\\\\0");
        clean_string = clean_string.replaceAll("'", "\\\\'");
        clean_string = clean_string.replaceAll("\\\"", "\\\\\"");

        if (clean_string.replaceAll("[a-zA-Z0-9_!@#$%^&*()-=+~.;:,\\Q[\\E\\Q]\\E<>{}\\/?\\\\\"' ]", "").length() < 1) {
            return clean_string;
        }

        java.sql.Statement stmt = link.createStatement();
        String qry = "SELECT QUOTE('" + clean_string + "')";

        stmt.executeQuery(qry);
        java.sql.ResultSet resultSet = stmt.getResultSet();
        resultSet.first();
        String r = resultSet.getString(1);
        return r.substring(1, r.length() - 1);
    }

    /**
     * Escape data to protected against SQL Injection
     *
     * @param link
     * @param str
     * @return
     * @throws Exception
     */
    public static String quote(java.sql.Connection link, String str)
            throws Exception {
        if (str == null) {
            return "NULL";
        }
        return "'" + mysql_real_escape_string(link, str) + "'";
    }

    /**
     * Escape identifier to protected against SQL Injection
     *
     * @param link
     * @param str
     * @return
     * @throws Exception
     */
    public static String nameQuote(java.sql.Connection link, String str)
            throws Exception {
        if (str == null) {
            return "NULL";
        }
        return "`" + mysql_real_escape_string(link, str) + "`";
    }

    /*
     * @param   path    Path to the SQL file
     * @return          List of query strings 
     */
    public static ArrayList<String> createQueries(File path) {
        String queryLine = "";
        StringBuilder sBuffer = new StringBuilder();
        ArrayList<String> listOfQueries = new ArrayList<>();

        try {
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);

            //read the SQL file line by line
            while ((queryLine = br.readLine()) != null) {
//                System.out.println("queryLine = " + queryLine);
                // ignore comments beginning with #
                int indexOfCommentSign = queryLine.indexOf('#');
                if (indexOfCommentSign != -1) {
                    if (queryLine.startsWith("#")) {
                        queryLine = "";
                    } else {
                        queryLine = new String(queryLine.substring(0, indexOfCommentSign - 1));
                    }
                }
                // ignore comments beginning with --
                indexOfCommentSign = queryLine.indexOf("--");
                if (indexOfCommentSign != -1) {
                    if (queryLine.startsWith("--")) {
                        queryLine = "";
                    } else {
                        queryLine = new String(queryLine.substring(0, indexOfCommentSign - 1));
                    }
                }
                // ignore comments surrounded by /* */
                indexOfCommentSign = queryLine.indexOf("/*");
                if (indexOfCommentSign != -1) {
                    if (queryLine.startsWith("#")) {
                        queryLine = "";
                    } else {
                        queryLine = new String(queryLine.substring(0, indexOfCommentSign - 1));
                    }

                    sBuffer.append(queryLine).append(" ");
                    // ignore all characters within the comment
                    do {
                        queryLine = br.readLine();
                    } while (queryLine != null && !queryLine.contains("*/"));
                    indexOfCommentSign = queryLine.indexOf("*/");
                    if (indexOfCommentSign != -1) {
                        if (queryLine.endsWith("*/")) {
                            queryLine = "";
                        } else {
                            queryLine = new String(queryLine.substring(indexOfCommentSign + 2, queryLine.length() - 1));
                        }
                    }
                }

                //  the + " " is necessary, because otherwise the content before and after a line break are concatenated
                // like e.g. a.xyz FROM becomes a.xyzFROM otherwise and can not be executed 
                if (queryLine != null) {
                    sBuffer.append(queryLine + " ");
                }
            }
            br.close();

            // here is our splitter ! We use ";" as a delimiter for each request 
            String[] splittedQueries = sBuffer.toString().split(";");

            // filter out empty statements
            for (String splittedQuerie : splittedQueries) {
                if (!splittedQuerie.trim().equals("") && !splittedQuerie.trim().equals("\t")) {
                    listOfQueries.add(new String(splittedQuerie));
                }
            }
        } catch (Exception e) {
        }
        return listOfQueries;
    }

}
