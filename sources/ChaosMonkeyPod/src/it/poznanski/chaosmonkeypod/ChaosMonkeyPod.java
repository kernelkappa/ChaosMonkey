package it.poznanski.chaosmonkeypod;

import java.util.ArrayList;
import java.util.Random;

public class ChaosMonkeyPod {
    public static void main (String[] args){

    }

    private String randomChoosePod(ArrayList<String> podList){
        Random rand = new Random();
        return podList.get(rand.nextInt(podList.size()));
    }
}
