package edu.isi.karma.controller.command.publish;

import java.util.HashSet;
import java.util.Set;

public class RdfQueues {
    private static Set<RdfQueue> rdfQueue = new HashSet<>();
    
    public static void addRdfQueue(String[] requestParams, String[] workspaceParams) {
     
        
        rdfQueue.add(new RdfQueue(requestParams, workspaceParams));
    }

    public static Set<RdfQueue> getRdfQueue() {
        return rdfQueue;
    }
}
