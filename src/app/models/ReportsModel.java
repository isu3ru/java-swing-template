/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.models;

import app.helpers.SystemReport;
import java.io.File;
import java.util.HashMap;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class ReportsModel extends Model {

    public static JasperPrint prepareReport(SystemReport sr, HashMap<String, Object> params) throws Exception {
        String reportpath = "reports/" + sr.getReportPath();
        JasperReport jr = JasperCompileManager.compileReport(reportpath);
        return JasperFillManager.fillReport(jr, params, con);
    }

    public static JasperPrint prepareReport(String reportpath, HashMap<String, Object> params) throws Exception {
        JasperReport jr = JasperCompileManager.compileReport(reportpath);
        return JasperFillManager.fillReport(jr, params, con);
    }

    public static JasperPrint prepareReport(String reportpath) throws Exception {
        HashMap<String, Object> params = new HashMap<>();
        File f = new File("reports");
        String SUBREPORT_DIR = f.getAbsolutePath() + "\\";
        params.put("SUBREPORT_DIR", SUBREPORT_DIR);
        JasperReport jr = JasperCompileManager.compileReport(reportpath);
        return JasperFillManager.fillReport(jr, params, con);
    }

}
