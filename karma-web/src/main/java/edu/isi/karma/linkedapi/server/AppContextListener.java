package edu.isi.karma.linkedapi.server;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.quartz.SchedulerException;
import edu.isi.karma.linkedapi.server.scheduling.SchedulerConfig;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            SchedulerConfig.startScheduler();
            System.out.println("Scheduler started...");
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Add logic to shutdown your scheduler if needed
    }
}
