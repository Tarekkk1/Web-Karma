package edu.isi.karma.linkedapi.server.scheduling;

import edu.isi.karma.controller.command.publish.PublishRDFCommandFactory;
import edu.isi.karma.controller.command.publish.RdfQueue;
import edu.isi.karma.controller.command.publish.RdfQueues;

import java.util.Set;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class DataIntegrationJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("Executing data integration logic...");

        Set<RdfQueue> rdfQueueSet = RdfQueues.getRdfQueue();
        System.out.println(rdfQueueSet.size() + " items in the RDF queue");

        for (RdfQueue rdfQueue : rdfQueueSet) {
            // print the request and workspace parameters
            System.out.println("Request parameters: " + String.join(", ", rdfQueue.getRequestParams()));
            System.out.println("Workspace parameters: " + String.join(", ", rdfQueue.getWorkspaceParams()));
            PublishRDFCommandFactory cf = new PublishRDFCommandFactory();
            cf.createCommand(rdfQueue.getRequestParams(), rdfQueue.getWorkspaceParams());
        }
    }
}
