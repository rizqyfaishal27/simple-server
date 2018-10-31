package webapp.controllers.ewallet;

import java.io.BufferedOutputStream;
import java.sql.SQLException;
import java.util.HashMap;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import webapp.controllers.BaseController;
import request.Request;
import response.Response;
import utils.DBUtil;
import utils.QuorumUtil;
import response.JsonResponse;


import httpstatus.HttpStatusCode;

public class TransferController extends BaseController {

    public TransferController(Request request, BufferedOutputStream responseStream) {
        super(request, responseStream);
    }

    public Response createResponse() {
        HashMap<String, Object> data = new HashMap<String, Object>();
        try {
            String env = System.getenv("JAVA_ENV");
            if(env.equals("development") || QuorumUtil.checkQuorum(0.5)) {
                String userId = request.getBody().get("user_id");
                String nilaiStr = request.getBody().get("nilai");
                if(userId == null || nilaiStr == null) {
                    throw new Exception("-99");
                }
                int nilaiInt = Integer.parseInt(nilaiStr);
                if(nilaiInt < 0 || nilaiInt > 1000000000) {
                    throw new Exception("-5");
                }
                String sql = "select * from users where user_id = '" + userId + "'";
                Statement statement = DBUtil.getConnection().createStatement();
                ResultSet res = statement.executeQuery(sql);
                if(res.next()) {
                    int saldo = res.getInt("saldo");
                    int newSaldo = saldo + nilaiInt;
                    String sqlUpdate = "update users set saldo = " + newSaldo + " where user_id = " + userId;
                    int updatedRows = statement.executeUpdate(sqlUpdate);
                    if(updatedRows >= 1) {
                        data.put("transferReturn", 1);
                        return new JsonResponse(data, HttpStatusCode.OK, request, responseStream);
                    } else {
                        throw new Exception("-4");
                    }
                } else {
                    throw new Exception("-1");
                }

            } else {
               throw new Exception("-2");
            }
        } catch(SQLException e) {
            e.printStackTrace();
            data.put("transferReturn", -4);
            return new JsonResponse(data, HttpStatusCode.OK, request, responseStream);            
        } catch (Exception e) {
            e.printStackTrace();    
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

