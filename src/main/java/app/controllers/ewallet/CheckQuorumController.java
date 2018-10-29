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
import utils.AppConfig;
import utils.HttpResponseClient;
import utils.HttpClient;
import request.Request;
import response.Response;
import response.JsonResponse;

import httpstatus.HttpStatusCode;

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
                data.put("active_count", count);
                data.put("total_count", hostLists.size());
            }
            return new JsonResponse(data, HttpStatusCode.OK, request, responseStream);
        } catch(IOException e) {
            return new JsonResponse(data, HttpStatusCode.OK, request, responseStream);
        }
    }
}

