/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.db.backup;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *
 * @author ISU3RU
 */
public class Utils {

    public static String implode(String glue, String[] peices) {
        StringBuilder sb = new StringBuilder("");
        int i = 0;
        for (String s : peices) {
            sb.append(s);
            i++;
            if (i < peices.length) {
                sb.append(glue);
            }
        }
        return sb.toString();
    }

    public static String implode(String glue, ArrayList<String> peices) {
        StringBuilder sb = new StringBuilder("");
        int i = 0;
        for (String s : peices) {
            sb.append(s);
            i++;
            if (i < peices.size()) {
                sb.append(glue);
            }
        }
        return sb.toString();
    }

    public static ArrayList<String> explode(String string, String delimiter) {
        ArrayList<String> al = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(string, delimiter);
        while (st.hasMoreElements()) {
            String s = st.nextToken();
            al.add(s);
        }
        return al;
    }
}
