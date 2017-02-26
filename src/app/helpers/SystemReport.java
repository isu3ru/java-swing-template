/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.helpers;

/**
 * This class contains the ENUMERATED CONSTANTS for the REPORTS
 *
 * @author MDCC
 */
public enum SystemReport {

    DUMMY_REPORT_ONE(ReportType.DUMMY_REPORTS, 1, "Dummy Report One",
    "Just another dummy report",
    "", true); //setting the last one to false doesn't list the report in the reports list

    private SystemReport(ReportType rtype, int ReportNumber, String reportName, String reportDescription, String reportPath) {
        this.rtype = rtype;
        this.ReportNumber = ReportNumber;
        this.reportName = reportName;
        this.reportDescription = reportDescription;
        this.reportPath = reportPath;
    }

    private SystemReport(ReportType rtype, int ReportNumber, String reportName, String reportDescription, String reportPath, boolean active) {
        this.rtype = rtype;
        this.ReportNumber = ReportNumber;
        this.reportName = reportName;
        this.reportDescription = reportDescription;
        this.reportPath = reportPath;
        this.active = active;
    }

    private ReportType rtype;
    private int ReportNumber;
    private String reportName;
    private String reportDescription;
    private String reportPath;
    private boolean active;

    public ReportType getRtype() {
        return rtype;
    }

    public void setRtype(ReportType rtype) {
        this.rtype = rtype;
    }

    public int getReportNumber() {
        return ReportNumber;
    }

    public void setReportNumber(int ReportNumber) {
        this.ReportNumber = ReportNumber;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReportDescription() {
        return reportDescription;
    }

    public void setReportDescription(String reportDescription) {
        this.reportDescription = reportDescription;
    }

    @Override
    public String toString() {
        return reportName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Get the value of reportPath
     *
     * @return the value of reportPath
     */
    public String getReportPath() {
        return reportPath;
    }

    /**
     * Set the value of reportPath
     *
     * @param reportPath new value of reportPath
     */
    public void setReportPath(String reportPath) {
        this.reportPath = reportPath;
    }

}
