package it.poznanski.chaosmonkeypod;

import it.poznanski.chaosmonkeypod.quartz.ChaosMonkeyJob;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;


public class ChaosMonkeyPod {

    private static final Logger logger = LogManager.getRootLogger();

    public static void main (String[] args) throws Exception{
        logger.info("Starting scheduler");
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        /*SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();*/
        logger.info("Creating job ChaosMonkey in group killers");
        JobDetail job = JobBuilder.newJob(ChaosMonkeyJob.class)
                .withIdentity("ChaosMonkey", "killers")
                .build();
        logger.info("Creating cron trigger for ChaosMonkey in group killers");
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("ChaosMonkeyTrigger", "killers")
                .startNow()
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(60)
                        .repeatForever())
                .build();
        logger.info("Configuring trigger for ChaosMonkey in scheduler");
        scheduler.scheduleJob(job, trigger);
        scheduler.start();
        logger.info("Job ChaosMonkeyPodJob schedulated");
    }

    //.withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * * * ?"))
    //                .forJob(jobKey)
}
