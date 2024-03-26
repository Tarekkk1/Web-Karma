package edu.isi.karma.controller.command.publish;

import java.util.HashSet;
import java.util.Set;

public class JsonQueues {
    private static Set<JsonQueue> jsonQueue = new HashSet<>();
    
    public static void addJsonQueue(String workSheetId) {
        if (jsonQueue == null)
            jsonQueue = new HashSet<>();

        jsonQueue.add(new JsonQueue(workSheetId));
    }

    public static Set<JsonQueue> getJsonQueue() {
        return jsonQueue;
    }
}
