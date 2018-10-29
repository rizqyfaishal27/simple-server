package webapp.controllers.ewallet;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import webapp.controllers.BaseController;
import webapp.types.Host;
import utils.AppConfig;
import utils.HttpResponseClient;
import utils.HttpClient;
import request.Request;
import response.Response;
import response.JsonResponse;

import httpstatus.HttpStatusCode;
import logger.Logger;

public class CheckQuorumController extends BaseController {

    public CheckQuorumController(Request request, BufferedOutputStream responseStream) {
        super(request, responseStream);
    }

    public Response createResponse()  {
        HashMap<String, Object> data = new HashMap<String, Object>();
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
                            System.out.println(response.getData());
                            HashMap<String, Integer> pingResponseDecoded = mapper.readValue(pingResponse.getData(), new TypeReference<HashMap<String, Integer>>() {});
                            if(pingResponseDecoded.get("pingReturn") == 1) {
                                count++;
                            }
                        }
                    } catch(IOException e) {

                    }
                }
                data.put("active_count", count);
                data.put("total_count", hostLists.length);
            }
            return new JsonResponse(data, HttpStatusCode.OK, request, responseStream);
        } catch(IOException e) {
            e.printStackTrace();
            return new JsonResponse(data, HttpStatusCode.OK, request, responseStream);
        }
    }
}

