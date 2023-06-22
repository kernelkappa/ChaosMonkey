package it.poznanski.chaosmonkeypod.rest.client;

import it.poznanski.chaosmonkeypod.utils.CommonUtils;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.ResponseProcessingException;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class K8SRestClient {

    private static Logger logger = LogManager.getLogger(K8SRestClient.class);
    private static Client client;
    private static final String apiServer = "https://kubernetes.default.svc";
    private static final String bearerTokenPath = "/var/run/secrets/kubernetes.io/serviceaccount/token";
    private static String bearerToken;

    public K8SRestClient() throws K8SRestClientException {
        logger.info("Creating Kubernetes REST client");
        try {
            Path path = Paths.get(bearerTokenPath);
            bearerToken = Files.readAllLines(path).get(0);
        }
        catch (IOException e){
            throw new K8SRestClientException("Cannot read Kubernetes token", e);
        }
        this.client = ClientBuilder.newClient();
    }

    public ArrayList getPodsWithLabel(String namespace, String label) throws K8SRestClientException{
        logger.info("Sending request for listing pod");
        String targetUri = new StringBuffer(apiServer)
                .append("/api/v1/namespaces/")
                .append(namespace)
                .append("/")
                .append("pods").toString();

        Response response;
        try {
            response = client.target(targetUri)
                    .queryParam("labelSelector", label)
                    .request("application/json")
                    .header("Authorization", getAuthHeaderValue())
                    .get();
        }catch (ResponseProcessingException e){
            String msg = "Cannot process api response";
            logger.error(msg, e);
            throw new K8SRestClientException(msg, e);
        }
        logger.info("Checking response code");
        checkStatusCode(response, targetUri);
        logger.info("Parsing json pod list");
        String jsonText = response.readEntity(String.class);
        ArrayList<String> pods = CommonUtils.parsePodList(jsonText);
        if(pods.isEmpty()){
            String msg = "Pod list is empty";
            K8SRestClientException e = new K8SRestClientException("Pod list is empty");
            logger.error(msg, e);
            throw new K8SRestClientException(msg, e);
        }
        return pods;
    }

    public void killPod(String podName, String namespace) throws K8SRestClientException{
        logger.info("Sending request for killing pod");
        String targetUri = new StringBuffer(apiServer)
                .append("/api/v1/namespaces/")
                .append(namespace)
                .append("/")
                .append("pods")
                .append("/")
                .append(podName).toString();

        Response response;
        try {
            response = client.target(targetUri)
                    .request("application/json")
                    .header("Authorization", getAuthHeaderValue())
                    .delete();
        }catch (ResponseProcessingException e) {
            throw new K8SRestClientException("Cannot process api response", e);
        }
        logger.info("Checking response code");
        checkStatusCode(response, targetUri);
    }

    private static void checkStatusCode(Response response, String targetUri) throws K8SRestClientException{
        boolean succeded = false;
        if(response.getStatus() >= 200  && response.getStatus() <300){
            String msg = new StringBuffer("Response returned status code ").append(response.getStatus()).append(" which is in accepted range [200-299]").toString();
            logger.info(msg);
            succeded = true;
        }
        if(!succeded){
            String msg = new StringBuffer("Error while calling uri")
                    .append(targetUri)
                    .append(", status code ")
                    .append(response.getStatus()).append(" is not in accepted range [200-299]")
                    .append("\nServer response:\n")
                    .append(response.readEntity(String.class)).toString();
            K8SRestClientException e = new K8SRestClientException(msg);
            logger.info(msg, e);
            throw e;
        }
    }
    private static String getAuthHeaderValue(){
        return new StringBuffer("Bearer ").append(bearerToken).toString();
    }
}