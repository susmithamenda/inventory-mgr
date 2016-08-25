package models;

import java.util.ArrayList;
import java.util.List;

public class AllocationResponse {
	private String header;
	
	private List<LineItem> lineItems = new ArrayList<LineItem>();
	
	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public List<LineItem> getLineItems() {
		return lineItems;
	}

	public void setLineItems(List<LineItem> lineItems) {
		this.lineItems = lineItems;
	}

	public static class LineItem {
		String product;
		int qtyAllocated;
		int qtyBackOrdered;
		
		public LineItem() {}
		
		public LineItem(String product) {
			this.product = product;
		}
		
		public String getProduct() {
			return product;
		}
		public void setProduct(String product) {
			this.product = product;
		}
		public int getQtyAllocated() {
			return qtyAllocated;
		}
		public void setQtyAllocated(int qtyAllocated) {
			this.qtyAllocated = qtyAllocated;
		}
		public int getQtyBackOrdered() {
			return qtyBackOrdered;
		}
		public void setQtyBackOrdered(int qtyBackOrdered) {
			this.qtyBackOrdered = qtyBackOrdered;
		}
		
	}
}
