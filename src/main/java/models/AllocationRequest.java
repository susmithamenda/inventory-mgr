package models;

import java.util.List;

public class AllocationRequest {
	private String header;
	private List<Allocation> allocations;
	
	public AllocationRequest() {
		super();
	}

	public AllocationRequest(String header, List<Allocation> allocations) {
		super();
		this.header = header;
		this.allocations = allocations;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public boolean isValid() {
		boolean isValid = true;
		for(Allocation a : allocations) { 
			isValid = isValid && !(a.quantity <= 0 || a.quantity >= 6);
		}
		return isValid;
	}
	
	public List<Allocation> getAllocations() {
		return allocations;
	}

	public void setAllocations(List<Allocation> allocations) {
		this.allocations = allocations;
	}

	public static class Allocation {
		String product;
		int quantity;
		public String getProduct() {
			return product;
		}
		public void setProduct(String product) {
			this.product = product;
		}
		public int getQuantity() {
			return quantity;
		}
		public void setQuantity(int quantity) {
			this.quantity = quantity;
		}
		
	}
}
