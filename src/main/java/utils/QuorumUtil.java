package utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import utils.HttpClient;
import utils.AppConfig;
import utils.HttpResponseClient;
import logger.Logger;

public class QuorumUtil {

    public static boolean checkQuorum(double percentage) throws IOException{
        HttpResponseClient response = HttpClient.sendGET(AppConfig.LIST_HOSTS_URL);
        if(response.getStatusCode() == 200) {
            ObjectMapper mapper = new ObjectMapper();
            ArrayList<String> hostLists = mapper.readValue(response.getData(), 
                new TypeReference<ArrayList<String>>(){});
            int count = 0;
            for(String host: hostLists) {
                HashMap<String, String> hostDecoded = mapper.readValue(host, 
                    new TypeReference<HashMap<String, String>>() {});
                HttpResponseClient pingResponse = HttpClient.sendPOST(hostDecoded.get("ip"), 
                    "application/json", "");
                if(pingResponse.getStatusCode() == 200) {
                    HashMap<String, Integer> pingResponseDecoded = mapper.readValue(pingResponse.getData(),
                    new TypeReference<HashMap<String, Integer>>() {});
                    if(pingResponseDecoded.get("pingReturned") == 1) {
                        count++;
                    }
                }
            }
            Logger.outputMessage(Integer.toString(count));
            Logger.outputMessage(Integer.toString(hostLists.size()));
            return count >= (int) (hostLists.size() * percentage);
        } else {
            return false;
        }
    }
}