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
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

public class RestAPITest {

    /*private KubernetesServer server = new KubernetesServer();
    private KubernetesClient fabric8Client = server.getClient();
    private String token = fabric8Client.getConfiguration().getOauthToken();
    @Before
    public void initTest(){
        server.expect().get().withPath("/api/v1/namespaces/testing/pods?labelSelector=chaos-monkey%3Dmonkey-victim").andReturn(HttpURLConnection.HTTP_OK, )

    }
    @Test
    @KubernetesTest
    @LoadKubernetesManifests("target-deployment.yaml")
    public void listPodsWithLabelTest() throws IOException, K8SRestClientException {
        Properties properties = Properties.getInstance();
        String namespace = properties.getProperties().getProperty("target.namespace");
        String label = properties.getProperties().getProperty("target.label");
        K8SRestClient client = new K8SRestClient(this.token);
        List pods = client.getPodsWithLabel(namespace, label);
        assert(pods.size() != 0);
    }*/
}