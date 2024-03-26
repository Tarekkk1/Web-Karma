package edu.isi.karma.controller.command.publish;
public class JsonQueue {
    private String workSheetId;

    public JsonQueue(String workSheetId) {
        this.workSheetId = workSheetId;
    }

    public String getWorkSheetId() {
        return workSheetId;
    }
}
