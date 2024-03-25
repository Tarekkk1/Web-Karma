package edu.isi.karma.controller.command.importdata;

import java.util.HashSet;
import java.util.Set;

public class ServiceQueues {
    private static Set<ServiceQueue> serviceQueue = new HashSet<>();
    
    public static void addServiceQueue(String workSheetId, String serviceUrl, String encoding, String worksheetName) {
     
        if (serviceQueue == null)
            serviceQueue = new HashSet<>();

        serviceQueue.add(new ServiceQueue(workSheetId, serviceUrl, encoding, worksheetName));
    }

    public static Set<ServiceQueue> getServiceQueue() {
        return serviceQueue;
    }
}
