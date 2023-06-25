package it.poznanski.chaosmonkeypod.utils;

import it.poznanski.chaosmonkeypod.rest.client.K8SRestClientException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CommonUtils {
    private static Logger logger = LogManager.getLogger(CommonUtils.class);
    public static ArrayList<String> parsePodList(String jsonText) throws K8SRestClientException {
        ArrayList<String>pods = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonText);
            JSONArray podsObjects = jsonObject.getJSONArray("items");
            logger.debug("PODS OBJECTS:\n" + podsObjects);
            podsObjects.iterator().forEachRemaining(element -> {
                logger.debug("ELEMENT:\n" + element);
                ((JSONObject) element).keys().forEachRemaining(key -> {
                    logger.debug("KEY:\n" + key);
                    if (key.toString().equals("metadata")) {
                        JSONObject metadata = ((JSONObject) element).getJSONObject("metadata");
                        logger.debug("METADATA:\n" + metadata);
                        pods.add(metadata.getString("name"));
                    }
                });
            });
            logger.debug("POD LIST:\n"+(String.join(", ", pods)));
        }catch (NullPointerException | JSONException e){
            String msg = "Cannot read pod list";
            //logger.error(msg, e);
            throw new K8SRestClientException(msg, e);
        }
        return pods;
    }
}
