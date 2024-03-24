package edu.isi.karma.controller.command.publish;

public class RdfQueue {
    private String[] requestParams;
    private String[] workspaceParams;

    public RdfQueue(String[] requestParams, String[] workspaceParams) {
        this.requestParams = requestParams;
        this.workspaceParams = workspaceParams;
    }

    public String[] getRequestParams() {
        return requestParams;
    }

    public String[] getWorkspaceParams() {
        return workspaceParams;
    }
}
