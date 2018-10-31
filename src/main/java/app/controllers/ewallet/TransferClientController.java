package webapp.controllers.ewallet;

import java.io.BufferedOutputStream;
import java.sql.SQLException;
import java.util.HashMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import webapp.controllers.BaseController;
import request.Request;
import response.Response;
import utils.DBUtil;
import utils.HttpClient;
import utils.HttpResponseClient;
import utils.QuorumUtil;
import response.JsonResponse;

import httpstatus.HttpStatusCode;

public class TransferClientController extends BaseController {

    public TransferClientController(Request request, BufferedOutputStream responseStream) {
        super(request, responseStream);
    }

    public Response createResponse() {
        HashMap<String, Object> data = new HashMap<String, Object>();
        try {
            if(QuorumUtil.checkQuorum(0.5)) {
                String userId = request.getBody().get("user_id");
                String nilaiStr = request.getBody().get("nilai");
                String cabangTarget = request.getBody().get("cabang");
    
                if(userId == null || nilaiStr == null || cabangTarget == null) {
                    throw new Exception("-99");
                }
                int nilaiInt = Integer.parseInt(nilaiStr);
                String sql = "select * from users where user_id = " + userId;
                Statement statement = DBUtil.getConnection().createStatement();
                ObjectMapper mapper = new ObjectMapper();
                ResultSet res = statement.executeQuery(sql);
                if(res.next()) {
                    int saldo = res.getInt("saldo");
                    if(nilaiInt > saldo) {
                        throw new Exception("-5");
                    }
                    HashMap<String, Object> dataToRequest = new HashMap<String, Object>();
                    dataToRequest.put("user_id", userId);  
                    dataToRequest.put("nilai", nilaiInt);
                    HttpResponseClient transferResponse = HttpClient.sendPOST(
                        "http://" + cabangTarget + "/ewallet/transfer",
                        "application/json",
                        mapper.writeValueAsString(dataToRequest)
                    );
                    if(transferResponse.getStatusCode() == 200) {
                        HashMap<String, Object> responseData = mapper.readValue(transferResponse.getData(),
                            new TypeReference<HashMap<String, Object>>() {}
                        );
                        if((Integer) responseData.get("transferReturn") == 1) {
                            int newSaldo = saldo - nilaiInt;
                            String updateSql = "update users set saldo = '" + newSaldo + "' where user_id = '" + userId + "'";
                            int resUpdate = statement.executeUpdate(updateSql);
                            if(resUpdate >= 1) {
                                data.put("transferReturn", 1);
                                return new JsonResponse(data, HttpStatusCode.OK, request, responseStream);
                            }
                            throw new Exception("-4");
                        }
                        data.put("transferReturn", responseData.get("transferReturn"));
                        return new JsonResponse(data, HttpStatusCode.OK, request, responseStream);            
                    }
                    throw new Exception("-99");
                }
                throw new Exception("-1");
            } else {
                throw new Exception("-2");
            }
        } catch(SQLException e) {
            e.printStackTrace();
            data.put("transferReturn", -4);
            return new JsonResponse(data, HttpStatusCode.OK, request, responseStream);            
        } catch(NumberFormatException e) {
            data.put("transferReturn", -5);
            return new JsonResponse(data, HttpStatusCode.OK, request, responseStream);            
        } catch (Exception e) {
            switch(e.getMessage()) {
                case "-1":
                    data.put("transferReturn", -1);
                    break;
                case "-2":
                    data.put("transferReturn", -2);
                    break;
                case "-4":
                    data.put("transferReturn", -4);
                    break;
                case "-5":
                    data.put("transferReturn", -5);
                    break;
                default:
                    data.put("transferReturn", -99);
                    break;
            }
            return new JsonResponse(data, HttpStatusCode.OK, request, responseStream);            
        }
    }
}

