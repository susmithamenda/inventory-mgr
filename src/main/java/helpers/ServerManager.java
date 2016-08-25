package helpers;

import static spark.Spark.*;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletRequest;

import org.eclipse.jetty.server.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;

import models.AllocationRequest;
import models.AllocationResponse;
public class ServerManager {
	
	private static final int HTTP_BAD_REQUEST = 400;	
	
	public static void main(String[] args) {		
		Logger logger =  LoggerFactory.getLogger(ServerManager.class);
		
		post("/inventory", (request, response) -> {
			response.type("application/json");
			try {
				ObjectMapper mapper = new ObjectMapper();
				AllocationRequest req = mapper.readValue(request.body(), AllocationRequest.class);
				
				if(!req.isValid()) {
					response.status(HTTP_BAD_REQUEST);
					return "INVALID REQUEST";
				}
				
				AllocationResponse res = AllocationManager.allocate(req);
				
				response.status(200);
												
				return "CREATED ALLOCATION: " + mapper.writeValueAsString(res);
			} catch(JsonParseException jpe) {	
				logger.error(jpe.getMessage());
				jpe.printStackTrace();
				
				response.type("application/json");
				response.status(HTTP_BAD_REQUEST);
				return "FAILED TO PARSE JSON";
			}
		});
	}
	
}
