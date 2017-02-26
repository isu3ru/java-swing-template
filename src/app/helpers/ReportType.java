/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.helpers;

/**
 *
 * @author Isuru
 */
public enum ReportType {

    DUMMY_REPORTS(1, "DUMMY_REPORT_TYPE");

    private ReportType(int reportCode, String reportName) {
        this.reportCode = reportCode;
        this.reportName = reportName;
    }

    private int reportCode;
    private String reportName;

    public int getReportCode() {
        return reportCode;
    }

    public void setReportCode(int reportCode) {
        this.reportCode = reportCode;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    @Override
    public String toString() {
        return reportName;
    }

}
