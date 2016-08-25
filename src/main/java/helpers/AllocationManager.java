package helpers;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import models.AllocationRequest;
import models.AllocationResponse;
import models.AllocationResponse.LineItem;
import models.AllocationRequest.Allocation;

public class AllocationManager {
	private static ConcurrentHashMap<String, Integer> inventory;	
	private static List<AllocationResponse> txHistory = new LinkedList<AllocationResponse>();
	Logger logger = LoggerFactory.getLogger(AllocationManager.class);
	
	static {
		inventory = new ConcurrentHashMap<String, Integer>();
		inventory.put("A", 2);
		inventory.put("B", 3);
		inventory.put("C", 1);
		inventory.put("D", 0);		
		inventory.put("E", 0);
	}
	
	public static ConcurrentHashMap<String, Integer> getInventory() {
		return inventory;
	}
	
	public static List<AllocationResponse> getTxHistory() {
		return txHistory;
	}
	
	public static boolean isInventoryEmpty() {
		boolean isEmpty = true;
		for(Map.Entry<String, Integer> entry : inventory.entrySet()) {
			int v = entry.getValue();
			if(v > 0)
				return false;
		}
		return isEmpty;
	}
	
	public static AllocationResponse allocate(AllocationRequest request) {
		
		//check if inventory is empty
		if(AllocationManager.isInventoryEmpty()) {
			List<AllocationResponse> m = AllocationManager.getTxHistory();
			ObjectMapper mapper = new ObjectMapper();
			Logger logger = LoggerFactory.getLogger(AllocationManager.class);
			for(AllocationResponse x : m) {
				try{
					logger.info(mapper.writeValueAsString(x));
				} catch(JsonParseException jpe) {
					logger.error("Error parsing response: "+ jpe.getMessage());
				} catch(JsonProcessingException jpe) {
					logger.error("[Error processing response: "+ jpe.getMessage());
				}
			}
			spark.Spark.halt();
		}
			
		//serving request
		AllocationResponse response = new AllocationResponse();		
		response.setHeader(request.getHeader());
		
		for(Allocation a : request.getAllocations()) {
			String product = a.getProduct();
			int qtyRequested = a.getQuantity();
			LineItem lineItem = new LineItem(product);
			
			if(!inventory.containsKey(product)) {
				continue;				
			} else {
				int qtyAvailable = inventory.get(product);
				int qtyAllocated = (qtyAvailable - qtyRequested) > 0 ? qtyRequested : qtyAvailable; 
				int qtyBackOrdered = qtyRequested - qtyAllocated;
					
				lineItem.setQtyAllocated(qtyAllocated);
				lineItem.setQtyBackOrdered(qtyBackOrdered);
				
				inventory.put(product, qtyAvailable - qtyAllocated);
				
			}
			response.getLineItems().add(lineItem);
		}
		//add to transaction history
		txHistory.add(response);
		return response;
	}
}
