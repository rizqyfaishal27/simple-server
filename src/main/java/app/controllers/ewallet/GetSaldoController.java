package webapp.controllers.ewallet;

import java.io.BufferedOutputStream;
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

public class GetSaldoController extends BaseController {

    public GetSaldoController(Request request, BufferedOutputStream responseStream) {
        super(request, responseStream);
    }

    public Response createResponse() {
        HashMap<String, Object> data = new HashMap<String, Object>();
        String env = System.getenv("JAVA_ENV");
        System.out.println(env);
        try {
            if(env.equals("development") || QuorumUtil.checkQuorum(0.5)) {
                System.out.println(request.getBody());
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
                    data.put("saldo", res.getInt("saldo"));
                    return new JsonResponse(data, HttpStatusCode.OK, request, responseStream);
                } else {
                    throw new SQLException("-1");
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
            data.put("saldo", -99);
            return new JsonResponse(data, HttpStatusCode.OK, request, responseStream);
        }
    }
}

