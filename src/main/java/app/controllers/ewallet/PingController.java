package webapp.controllers.ewallet;

import java.io.BufferedOutputStream;
import java.util.HashMap;

import webapp.controllers.BaseController;
import request.Request;
import response.Response;
import response.JsonResponse;

import httpstatus.HttpStatusCode;

public class PingController extends BaseController {

    public PingController(Request request, BufferedOutputStream responseStream) {
        super(request, responseStream);
    }

    public Response createResponse() {
        try {
            HashMap<String, Object> data = new HashMap<String, Object>();
            data.put("pingReturn", 1);
            return new JsonResponse(data, HttpStatusCode.OK, request, responseStream);
        } catch(Exception e) {
            HashMap<String, Object> data = new HashMap<String, Object>();
            data.put("pingReturn", -99);
            return new JsonResponse(data, HttpStatusCode.OK, request, responseStream);
        }
    }
}

