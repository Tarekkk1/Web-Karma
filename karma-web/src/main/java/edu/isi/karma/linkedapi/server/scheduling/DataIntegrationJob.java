package edu.isi.karma.linkedapi.server.scheduling;

import edu.isi.karma.controller.command.publish.PublishRDFCommandFactory;
import edu.isi.karma.controller.command.publish.RdfQueue;
import edu.isi.karma.controller.command.publish.RdfQueues;
import edu.isi.karma.imp.Import;
import edu.isi.karma.rep.Worksheet;

import java.util.Set;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import edu.isi.karma.controller.command.importdata.ServiceQueues;
import edu.isi.karma.controller.command.importdata.ServiceQueue;
import edu.isi.karma.controller.command.importdata.ImportServiceCommandFactory;

public class DataIntegrationJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("Executing data integration logic...");

        Set<ServiceQueue> serviceQueueSet = ServiceQueues.getServiceQueue();

        System.out.println(serviceQueueSet.size() + " items in the service queue");

        for (ServiceQueue serviceQueue : serviceQueueSet) {
            System.out.println("Service URL: " + serviceQueue.getServiceUrl());
            ImportServiceCommandFactory cf = new ImportServiceCommandFactory();
            try {
                cf.updateWorkSheet(serviceQueue.getWorkSheetId(), serviceQueue.getServiceUrl(), serviceQueue.getEncoding(), serviceQueue.getWorksheetName());
            } catch (Exception e) {
                e.printStackTrace();
            }            
        }

        Set<RdfQueue> rdfQueueSet = RdfQueues.getRdfQueue();
        System.out.println(rdfQueueSet.size() + " items in the RDF queue");

        for (RdfQueue rdfQueue : rdfQueueSet) {
            PublishRDFCommandFactory cf = new PublishRDFCommandFactory();
            cf.createCommand(rdfQueue.getRequestParams(), rdfQueue.getWorkspaceParams());
        }
    }
}
