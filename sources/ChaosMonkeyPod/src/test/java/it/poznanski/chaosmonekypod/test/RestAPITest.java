package it.poznanski.chaosmonekypod.test;


import io.fabric8.junit.jupiter.api.KubernetesTest;
import io.fabric8.junit.jupiter.api.LoadKubernetesManifests;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.server.mock.KubernetesServer;
import it.poznanski.chaosmonkeypod.rest.client.K8SRestClient;
import it.poznanski.chaosmonkeypod.rest.client.K8SRestClientException;
import it.poznanski.chaosmonkeypod.utils.Properties;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class RestAPITest {

    private KubernetesServer server;
    private KubernetesClient fabric8Client;

    private String token;
    @Before
    public void initTest() throws IOException{
        Path path = Paths.get("src/test/resources/podList.json");
        List<String> lines = Files.readAllLines(path);
        StringBuffer buffer = new StringBuffer();
        for(String line:lines){
            buffer.append(line);
        }
        String podListText = buffer.toString();
        JSONObject jsonObject = new JSONObject(podListText);
        this.server = new KubernetesServer();
        this.server.expect().get().withPath("/api/v1/namespaces/testing/pods?labelSelector=chaos-monkey%3Dmonkey-victim").andReturn(HttpURLConnection.HTTP_OK, jsonObject).once();
        this.fabric8Client = server.getClient();
        this.token = fabric8Client.getConfiguration().getOauthToken();
    }
    @Test
    public void listPodsWithLabelTest() throws IOException, K8SRestClientException {
        Properties properties = Properties.getInstance();
        String namespace = properties.getProperties().getProperty("target.namespace");
        String label = properties.getProperties().getProperty("target.label");
        K8SRestClient client = new K8SRestClient(this.token);
        List pods = client.getPodsWithLabel(namespace, label);
        assert(pods.size() != 0);
    }
}