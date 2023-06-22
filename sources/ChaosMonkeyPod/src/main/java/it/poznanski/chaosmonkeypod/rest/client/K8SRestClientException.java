package it.poznanski.chaosmonkeypod.rest.client;

public class K8SRestClientException extends Exception{

    public K8SRestClientException (String msg, Throwable exception){
        super(msg,exception);
    }
    public K8SRestClientException (String msg){
        super(msg);
    }
}
