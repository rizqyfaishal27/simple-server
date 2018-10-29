// package webapp.controllers.ewallet;

// import java.io.BufferedOutputStream;
// import java.util.HashMap;
// import java.util.ArrayList;

// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.ResultSet;
// import java.sql.SQLException;
// import java.sql.Statement;

// import com.fasterxml.jackson.core.JsonGenerationException;
// import com.fasterxml.jackson.core.type.TypeReference;
// import com.fasterxml.jackson.databind.JsonMappingException;
// import com.fasterxml.jackson.databind.ObjectMapper;

// import webapp.controllers.BaseController;
// import request.Request;
// import response.Response;
// import response.JsonResponse;
// import utils.QuorumUtil;
// import utils.AppConfig;
// import utils.DBUtil;
// import utils.HttpClient;
// import utils.HttpResponseClient;
// import httpstatus.HttpStatusCode;

// public class RegisterController extends BaseController {

//     public RegisterController(Request request, BufferedOutputStream responseStream) {
//         super(request, responseStream);
//     }

//     public Response createResponse() {
//         try {
//             HashMap<String, Object> data = new HashMap<String, Object>();
//             if(QuorumUtil.checkQuorum(0.5)) {
//                 String name = request.getBody().get("nama");
//                 String userId = request.getBody().get("user_id");
//                 Statement statement = DBUtil.getConnection().createStatement();
//                 HttpResponseClient responseHostList = HttpClient.sendGET(AppConfig.LIST_HOSTS_URL);
//                 ObjectMapper mapper = new ObjectMapper();
//                 ArrayList<String> hostLists = mapper.readValue(responseHostList.getData(), 
//                     new TypeReference<ArrayList<String>>() {});
//                 for(String host: hostLists) {
//                     HashMap<String, String> hostDecoded = mapper.readValue(host,
//                         new TypeReference<HashMap<String, String>>() {});
//                     if(hostDecoded.get("npm").equals(userId)) {
//                         if(hostDecoded.get("ip").equals(AppConfig.IP_ADDRESS)) {
//                             ResultSet res = statement
//                                 .executeQuery("insert into users values(" + userId + "," + name + "," + 1000000000 + ")");
//                         } else {
//                             ResultSet res = statement
//                                 .executeQuery("insert into users values(" + userId + "," + name + "," + 0 + ")");
//                         }
//                     }
//                 }
//             } else {
//                 data.put("registerReturn", -2);
//                 return new JsonResponse(data, HttpStatusCode.OK, request, responseStream);
//             }
//         } catch(Exception e) {
            
//         }
//     }
// }


