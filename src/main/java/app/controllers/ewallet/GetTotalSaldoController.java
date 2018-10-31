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
        System.out.println(env);
        try {
            if(env.equals("development") || QuorumUtil.checkQuorum(1.0)) {
                String userIdString = request.getBody().get("user_id");
                if(userIdString == null) {
                    throw new Exception();
                }
                int userId = Integer.parseInt(userIdString);
                String sql = "select * from users where user_id = '" + userId + "'";
                System.out.println(sql);
                Statement statement = DBUtil.getConnection().createStatement();
                ResultSet res = statement.executeQuery(sql);
                if(res.next()) {
                    HttpResponseClient listHostResponse = HttpClient.sendGET(AppConfig.LIST_HOSTS_URL);
                    if(listHostResponse.getStatusCode() == 200) {
                        ObjectMapper mapper = new ObjectMapper();
                        int notRegistered = 0;
                        int totalSaldo = 0;
                        Host[] hosts = mapper.readValue(listHostResponse.getData(), Host[].class);
                        HashMap<String, String> dataToRequest = new HashMap<String, String>();

                        dataToRequest.put("user_id", userIdString);
                        for (Host host: hosts) {
                            HttpResponseClient requestTotalSaldoResponse = HttpClient.sendPOST(
                                "http://" + host.getIp() + "/ewallet/getSaldo",
                                "application/json",
                                mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dataToRequest)
                            );
                            if(requestTotalSaldoResponse.getStatusCode() == 200) {
                                HashMap<String, Integer> totalSaldoMap = mapper.readValue(
                                    requestTotalSaldoResponse.getData(),
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
                        throw new Exception("-99");
                    }
                } else {
                    HttpResponseClient listHostResponse = HttpClient.sendGET(AppConfig.LIST_HOSTS_URL);
                    if(listHostResponse.getStatusCode() == 200) {
                        ObjectMapper mapper = new ObjectMapper();
                        Host[] hosts = mapper.readValue(listHostResponse.getData(), Host[].class);
                        HashMap<String, String> dataToRequest = new HashMap<String, String>();

                        dataToRequest.put("user_id", userIdString);

                        for (Host host: hosts) {
                            if(host.getNpm().equals(userIdString)) {
                                HttpResponseClient totalSaldoResponse = HttpClient.sendPOST(
                                    "http://" + host.getIp() + "/ewallet/getTotalSaldo",
                                    "application/json",
                                    mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dataToRequest)
                                );
                                if(totalSaldoResponse.getStatusCode() == 200) {
                                    HashMap<String, Object> totalSaldoMap = mapper.readValue(
                                        totalSaldoResponse.getData(),
                                        new TypeReference<HashMap<String, Integer>>() {}    
                                    );
                                    return new JsonResponse(totalSaldoMap, HttpStatusCode.OK, request, responseStream);
                                } else {
                                    throw new Exception("-3");
                                }
                            }
                        }
                        data.put("saldo", -1);
                        return new JsonResponse(data, HttpStatusCode.OK, request, responseStream);
                    } else {
                        throw new Exception("-99");
                    }
                }
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

