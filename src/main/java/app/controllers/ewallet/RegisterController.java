package webapp.controllers.ewallet;

import java.io.BufferedOutputStream;
import java.util.HashMap;

import javax.management.InvalidAttributeValueException;

import java.util.ArrayList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import webapp.controllers.BaseController;
import webapp.types.Host;
import request.Request;
import response.Response;
import response.JsonResponse;
import utils.QuorumUtil;
import utils.AppConfig;
import utils.DBUtil;
import utils.HttpClient;
import utils.HttpResponseClient;
import httpstatus.HttpStatusCode;

public class RegisterController extends BaseController {

    public RegisterController(Request request, BufferedOutputStream responseStream) {
        super(request, responseStream);
    }

    public Response createResponse() {
        HashMap<String, Object> data = new HashMap<String, Object>();
        try {
            if(QuorumUtil.checkQuorum(0)) {
                try {
                    String name = request.getBody().get("nama");
                    String userId = request.getBody().get("user_id");
                    Statement statement = DBUtil.getConnection().createStatement();
                    HttpResponseClient responseHostList = HttpClient.sendGET(AppConfig.LIST_HOSTS_URL);
                    ObjectMapper mapper = new ObjectMapper();
                    Host[] hostLists = mapper.readValue(responseHostList.getData(), Host[].class);
                    for(Host host: hostLists) {
                        if(host.getNpm().equals(userId)) {
                            if(host.getIp().equals(AppConfig.IP_ADDRESS)) {
                                int res = statement
                                    .executeUpdate("insert into users values(" + userId + "," + name + "," + 1000000000 + ")");
                                if(res <= 0) {
                                    throw new SQLException();
                                }
                            } else {
                                int res = statement
                                    .executeUpdate("insert into users values(" + userId + "," + name + "," + 0 + ")");
                                if(res <= 0) {
                                    throw new SQLException();
                                }
                            }
                        }
                    }
                    data.put("registerReturn", 1);
                    return new JsonResponse(data, HttpStatusCode.OK, request, responseStream);
                } catch(SQLException e) {
                    data.put("registerReturn", -4);
                    return new JsonResponse(data, HttpStatusCode.OK, request, responseStream);
                }
            } else {
                data.put("registerReturn", -2);
                return new JsonResponse(data, HttpStatusCode.OK, request, responseStream);
            }
        } catch(Exception e) {
            data.put("registerReturn", -99);
            return new JsonResponse(data, HttpStatusCode.OK, request, responseStream);
        }
    }
}


