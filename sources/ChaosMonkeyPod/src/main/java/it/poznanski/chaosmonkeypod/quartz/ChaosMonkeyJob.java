package it.poznanski.chaosmonkeypod.quartz;

import it.poznanski.chaosmonkeypod.rest.client.K8SRestClient;
import it.poznanski.chaosmonkeypod.rest.client.K8SRestClientException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.ArrayList;
import java.util.Random;

public class ChaosMonkeyJob implements Job {

    private static Logger logger = LogManager.getLogger(ChaosMonkeyJob.class);

    public void execute(JobExecutionContext arg0) throws JobExecutionException {

        try{
            logger.info("Reading job properties");
            String namespace = System.getenv().get("TARGET_NAMESPACE");
            String label = System.getenv().get("TARGET_LABEL");
            logger.info("Creating client for Kubernetes");
            K8SRestClient client = new K8SRestClient();
            String msg = new StringBuffer("Getting pod list with label ")
                    .append(label)
                    .append(" in namespace ")
                    .append(namespace).toString();
            logger.info(msg);
            ArrayList pods = client.getPodsWithLabel(namespace, label);
            logger.info("Selecting random pod to kill from pod list");
            if(pods.size() > 0){
                String podToKill = randomChoosePod(pods);
                msg = new StringBuffer("Killing pod ")
                        .append(podToKill)
                        .append(" in namespace ")
                        .append(namespace).toString();
                logger.info(msg);
                client.killPod(podToKill, namespace);
            }
            else {
                msg = new StringBuffer("No pod with label ").append(label).append("in pod list").toString();
                logger.error(msg);
            }

        }catch(K8SRestClientException e){
            String msg = "Error while executing ChaosMonkeyJob";
            logger.error(msg, e);
            throw new JobExecutionException(msg, e);
        }
    }

    private String randomChoosePod(ArrayList<String> podList){
        Random rand = new Random();
        return podList.get(rand.nextInt(podList.size()));
    }
}
