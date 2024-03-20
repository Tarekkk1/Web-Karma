package edu.isi.karma.linkedapi.server.scheduling;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class SchedulerConfig {

    public static void startScheduler() throws SchedulerException {
        JobDetail job = JobBuilder.newJob(DataIntegrationJob.class)
          .withIdentity("dataIntegrationJob", "group1")
          .build();

        Trigger trigger = TriggerBuilder.newTrigger()
          .withIdentity("dataIntegrationTrigger", "group1")
          .startNow()
          .withSchedule(SimpleScheduleBuilder.simpleSchedule()
            .withIntervalInSeconds(60) // Run every 60 seconds
            .repeatForever())
          .build();

        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.start();
        scheduler.scheduleJob(job, trigger);
    }
}
