package webapp.controllers.ewallet;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
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
import utils.AppConfig;
import utils.DBUtil;
import utils.HttpResponseClient;
import utils.HttpClient;
import request.Request;
import response.Response;
import response.TextResponse;

import httpstatus.HttpStatusCode;
import logger.Logger;

public class CheckDatabaseController extends BaseController {

    public CheckDatabaseController(Request request, BufferedOutputStream responseStream) {
        super(request, responseStream);
    }

    public Response createResponse()  {
        HashMap<String, Object> data = new HashMap<String, Object>();
        try {
            Statement statement = DBUtil.getConnection().createStatement();
            ResultSet res = statement.executeQuery("select * from users;");
            String temp = "";
            while(res.next()) {
                System.out.println("Yes");
                int id = res.getInt("user_id");
                String nama = res.getString("nama");
                int saldo = res.getInt("saldo");

                temp += (id + " " + nama + " " + saldo + "\n");
            }
            return new TextResponse(temp, HttpStatusCode.OK, request, responseStream);
        } catch(SQLException e) {
            e.printStackTrace();
            return new TextResponse("", HttpStatusCode.INTERNAL_SERVER_ERROR, request, responseStream);
        }
    }
}

