package webapp.controllers.ewallet;

import java.io.BufferedOutputStream;
import java.util.HashMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import webapp.controllers.BaseController;
import webapp.types.Host;
import request.Request;
import response.Response;
import utils.AppConfig;
import utils.DBUtil;
import utils.HttpClient;
import utils.HttpResponseClient;
import utils.QuorumUtil;
import response.JsonResponse;

import httpstatus.HttpStatusCode;

public class GetTotalSaldoController extends BaseController {

    public GetTotalSaldoController(Request request, BufferedOutputStream responseStream) {
        super(request, responseStream);
    }

    public Response createResponse() {
        HashMap<String, Object> data = new HashMap<String, Object>();
        String env = System.getenv("JAVA_ENV");
        try {
            if(env.equals("development") || QuorumUtil.checkQuorum(1.0)) {
                String userIdString = request.getBody().get("user_id");
                if(userIdString == null) {
                    throw new Exception("-99");
                }                
                HttpResponseClient listHostResponse = HttpClient.sendGET(AppConfig.LIST_HOSTS_URL);
                if(listHostResponse.getStatusCode() == 200) {
                    ObjectMapper mapper = new ObjectMapper();
                    Host[] hosts = mapper.readValue(listHostResponse.getData(), Host[].class);
                    HashMap<String, String> dataToRequest = new HashMap<String, String>();
                    dataToRequest.put("user_id", userIdString);
                    for (Host host: hosts) {
                       
                        if(host.getNpm().equals(userIdString)) {
                            if(host.getIp().equals(AppConfig.IP_ADDRESS)) {
                                int notRegistered = 0;
                                int totalSaldo = 0;
                                for(Host hostJ: hosts) {
                                    String dataReq = mapper.writeValueAsString(dataToRequest);
                                    HttpResponseClient requestSaldoResponse = HttpClient.sendPOST(
                                        "http://" + hostJ.getIp() + "/ewallet/getSaldo",
                                        "application/json",
                                        dataReq
                                    );
									System.out.println(hostJ.getIp() + " " + requestSaldoResponse.getStatusCode());
                                    if(requestSaldoResponse.getStatusCode() == 200) {
                                        HashMap<String, Integer> totalSaldoMap = mapper.readValue(
                                            requestSaldoResponse.getData(),
                                            new TypeReference<HashMap<String, Integer>>() {}    
                                        );
                                        int saldo = totalSaldoMap.get("saldo");
                                        if(saldo == -1) {
                                            notRegistered++;
                                        } else if(saldo >= 0) {
                                            totalSaldo += saldo;
                                        } else {
                                            throw new Exception("-3");
                                        }
                                    } else {
                                        throw new Exception("-3");
                                    }
                                }
                                if(notRegistered == hosts.length) {
                                    data.put("saldo", -1);
                                    return new JsonResponse(data, HttpStatusCode.OK, request, responseStream);
                                } else {
                                    data.put("saldo", totalSaldo);
                                    return new JsonResponse(data, HttpStatusCode.OK, request, responseStream);
                                }
                            } else {
                                HttpResponseClient requestTotalSaldoResponse = HttpClient.sendPOST(
                                    "http://" + host.getIp() + "/ewallet/getTotalSaldo",
                                    "application/json",
                                    mapper.writeValueAsString(dataToRequest)
                                );
                                if(requestTotalSaldoResponse.getStatusCode() == 200) {
                                    HashMap<String, Integer> totalSaldoMap = mapper.readValue(
                                        requestTotalSaldoResponse.getData(),
                                        new TypeReference<HashMap<String, Integer>>() {}    
                                    );
                                    int saldo = totalSaldoMap.get("saldo");
                                    data.put("saldo", saldo);
                                    return new JsonResponse(data, HttpStatusCode.OK, request, responseStream);
                                } else {
                                    throw new Exception("-3");
                                }
                            }   
                        }
                    }
                    throw new Exception("-99");
                }
                throw new Exception("-99");
            } else {
                data.put("saldo", -2);
                return new JsonResponse(data, HttpStatusCode.OK, request, responseStream);
            }
        } catch(SQLException e) {
            e.printStackTrace();
            if(e.getMessage().equals("-1")) {
                data.put("saldo", -1);
                return new JsonResponse(data, HttpStatusCode.OK, request, responseStream);
            } else {
                data.put("saldo", -4);
                return new JsonResponse(data, HttpStatusCode.OK, request, responseStream);
            }
        } catch(Exception e) {
            e.printStackTrace();
            data.put("saldo", Integer.parseInt(e.getMessage()));
            return new JsonResponse(data, HttpStatusCode.OK, request, responseStream);
        }
    }
}

