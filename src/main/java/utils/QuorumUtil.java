package utils;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import utils.HttpClient;
import utils.AppConfig;
import utils.HttpResponseClient;
import webapp.types.Host;
import logger.Logger;

public class QuorumUtil {

    public static boolean checkQuorum(double percentage) throws IOException{
        try {
            HttpResponseClient response = HttpClient.sendGET(AppConfig.LIST_HOSTS_URL);
            if(response.getStatusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                Host[] hostLists = mapper.readValue(response.getData(), Host[].class);
                int count = 0;
                for(Host host: hostLists) {
                    Logger.outputMessage(host.getIp());
                    try {
                        HttpResponseClient pingResponse = HttpClient.sendPOST("http://" + host.getIp() + "/ewallet/ping", 
                        "application/json", "");
                        if(pingResponse.getStatusCode() == 200) {
                            HashMap<String, Integer> pingResponseDecoded = mapper.readValue(pingResponse.getData(), new TypeReference<HashMap<String, Integer>>() {});
                            if(pingResponseDecoded.get("pingReturn") == 1) {
                                count++;
                            }
                        }
                    } catch(IOException e) {

                    }
                }
                return count >= (int) (percentage * hostLists.length);
            }
            return false;
            
        } catch(IOException e) {
            return false;
        }
    }
}