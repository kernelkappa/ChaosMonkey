package it.poznanski.chaosmonkeypod.rest.client;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.ResponseProcessingException;
import jakarta.ws.rs.core.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class K8SRestClient {
    private static Client client;
    private static final String apiServer = "https://kubernetes.default.svc";
    private static final String bearerTokenPath = "/var/run/secrets/kubernetes.io/serviceaccount/token";
    private static String bearerToken;

    public K8SRestClient() throws K8SRestClientException {
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
            throw new K8SRestClientException("Cannot process api response", e);
        }
        checkStatusCode(response, targetUri);
        String jsonText = response.readEntity(String.class);
        JSONObject jsonObject;
        ArrayList<String>pods = new ArrayList<String>();
        try {
            jsonObject = new JSONObject(jsonText);
            JSONArray podsObjects = jsonObject.getJSONArray("items");
            podsObjects.iterator().forEachRemaining(element -> {
                ((JSONObject)element).keys().forEachRemaining(key -> {
                    if(key == "name"){
                        pods.add(((JSONObject)element).getString("name"));
                    }
                });
            });
        }catch (NullPointerException|JSONException e){
            throw new K8SRestClientException("Cannot read pod list", e);
        }
        if(pods.isEmpty()){
            throw new K8SRestClientException("Pod list is empty");
        }
        return pods;
    }

    public void killPod(String podName, String namespace) throws K8SRestClientException{
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
        checkStatusCode(response, targetUri);
    }

    private static void checkStatusCode(Response response, String targetUri) throws K8SRestClientException{
        boolean succeded = false;
        if(response.getStatus() >= 200  && response.getStatus() <300){
            succeded = true;
        }
        if(!succeded){
            String msg = new StringBuffer("Error while calling uri")
                    .append(targetUri)
                    .append(" with status code ")
                    .append(response.getStatus())
                    .append("\nServer response:\n")
                    .append(response.readEntity(String.class)).toString();
            throw new K8SRestClientException(msg);
        }
    }
    private static String getAuthHeaderValue(){
        return new StringBuffer("Bearer ").append(bearerToken).toString();
    }
}