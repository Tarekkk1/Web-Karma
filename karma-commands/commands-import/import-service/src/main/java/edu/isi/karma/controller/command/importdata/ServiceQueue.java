package edu.isi.karma.controller.command.importdata;
public class ServiceQueue {
    private String workSheetId;
    private String serviceUrl;
    private String encoding;
    private String worksheetName;

    public ServiceQueue(String workSheetId, String serviceUrl, String encoding, String worksheetName) {
        this.workSheetId = workSheetId;
        this.serviceUrl = serviceUrl;
        this.encoding = encoding;
        this.worksheetName = worksheetName;
    }

    public String getWorkSheetId() {
        return workSheetId;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public String getEncoding() {
        return encoding;
    }

    public String getWorksheetName() {
        return worksheetName;
    }
}
