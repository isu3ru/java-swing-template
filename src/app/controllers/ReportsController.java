/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.controllers;

import app.helpers.SystemReport;
import app.helpers.Utilities;
import app.models.ReportsModel;
import java.util.HashMap;
import javax.swing.JFrame;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Isuru
 */
public class ReportsController {

    public static void prepareAndViewReport(SystemReport sr, HashMap<String, Object> params) {
        JasperPrint jaspr = prepareReport(sr, params);
        viewReport(jaspr, false);
    }

    public static JasperPrint prepareReport(SystemReport sr, HashMap<String, Object> params) {
        try {
            return ReportsModel.prepareReport(sr, params);
        } catch (Exception e) {
            return null;
        }
    }

    public static JasperPrint prepareReport(String sr, HashMap<String, Object> params) {
        try {
            return ReportsModel.prepareReport(sr, params);
        } catch (Exception e) {
            return null;
        }
    }

    public static JasperPrint prepareReport(String sr) {
        try {
            return ReportsModel.prepareReport(sr);
        } catch (Exception e) {
            return null;
        }
    }

    public static void viewReport(JasperPrint jp, boolean exit_on_close) {
        JasperViewer jv = new JasperViewer(jp, exit_on_close);
        jv.setTitle("Reports Viewer - Application");
        Utilities.setWindowIcon(jv);
        jv.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jv.setExtendedState(JFrame.MAXIMIZED_BOTH);
        jv.setVisible(true);
    }

    public static void printReport(JasperPrint jp, boolean showprintdialog) throws Exception {
        JasperPrintManager.printReport(jp, showprintdialog);
    }

    public static void main(String[] args) {
//        PrintService[] lps = PrintServiceLookup.lookupPrintServices(null, null);
//        
//        PrinterJob printerJob = PrinterJob.getPrinterJob();
//
//        PageFormat pageFormat = PrinterJob.getPrinterJob().defaultPage();
//        printerJob.defaultPage(pageFormat);
//
//        int selectedService = 0;
//
//        AttributeSet attributeSet = new HashPrintServiceAttributeSet(new PrinterName(printerNameShort, null));
//
//        PrintService[] printService = PrintServiceLookup.lookupPrintServices(null, attributeSet);
//
//        try {
//            printerJob.setPrintService(printService[selectedService]);
//
//        } catch (Exception e) {
//
//            System.out.println(e);
//        }
//        JRPrintServiceExporter exporter;
//        PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
//        printRequestAttributeSet.add(MediaSizeName.NA_LETTER);
//        printRequestAttributeSet.add(new Copies(1));
//
//        // these are deprecated
//        exporter = new JRPrintServiceExporter();
//        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
//        exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, printService[selectedService]);
//        exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, printService[selectedService].getAttributes());
//        exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
//        exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
//        exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);
//        exporter.exportReport();
    }

}
