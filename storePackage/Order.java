package storePackage;

public abstract class Order implements Comparable<Order> {
	private Product product;
	private Customer customer;
	private int quantity;
	private String code,orderType;
	private double profitFromOrder;

	// Constructor for web products.
	public Order(Customer customer, int quantity, String code, Product product,String orderType) {
		this.customer = customer;
		this.quantity = quantity;
		this.code = code;
		this.product = product;
		this.orderType = orderType;
	}

	public Customer getCustomer() {
		return customer;
	}
	
	public String getOrderType() {
		return orderType;
	}

	public String getCode() {
		return code;
	}

	public Product getProduct() {
		return product;
	}

	public int getQuantity() {
		return quantity;
	}
	
	public double getProfitFromOrder() {
		return profitFromOrder;
	}

	public void setProfitFromOrder(double profitFromOrder) {
		this.profitFromOrder = profitFromOrder;
	}

	public String toString(boolean showProfit) {
		StringBuilder sb = new StringBuilder();
		sb.append("\n====Order details====\n").append("Code of order: ").append(code)
			.append("\nName of Product: ").append(product.getName())
			.append("\nQuantity saved for order: ").append(quantity);
			if(showProfit)
				sb.append("\nProfit from order: ").append(profitFromOrder).append('₪');
			
			sb.append("\n== Customer details ==\n").append(customer.toString());
		
		
		
		return sb.toString();
	}

	@Override
	public int compareTo(Order o) {
		// prevent copies by code.
		return this.code.compareTo(o.code);
	}

}
