package storePackage;

import dataBasePackage.*;
import java.util.Iterator;
import java.util.List;

public abstract class Product implements Comparable<Product>{

	private String product_name, code;
	private double cost_price, selling_price;
	private int stock;
	private int totalProfitForProduct;
	ProductType type;

	public Product(String product_name, String code, double cost_price, double selling_price, int stock,ProductType type) {
		super();
		this.product_name = product_name;
		this.code = code;
		this.cost_price = cost_price;
		this.selling_price = selling_price;
		this.stock = stock;
		this.type = type;
	}

	public ProductType getType() {
		return type;
	}

	public String getCode() {
		return code;
	}
	
	public String getName() {
		return product_name;
	}
	
	public void setTotalProfitForProduct(double totalProfitForProduct) {
		this.totalProfitForProduct += totalProfitForProduct;
	}

	public int getStock() {
		return stock;
	}

	public double getCost_price() {
		return cost_price;
	}

	public double getSelling_price() {
		return selling_price;
	}

	public int getTotalProfitForProduct() {
		return totalProfitForProduct;
	}
	
	public void setStock(int stock) {
		this.stock = stock;
	}

	public String toString(boolean printOrderDetails,char USDorNIS) {
		StringBuilder sb = new StringBuilder();
		sb.append("\n====Product details====\n").append("Name: ").append(product_name).append("\nCode: ").append(code)
				.append("\nPrice: ").append(selling_price).append(USDorNIS).append("\nQuantity in stock: ").append(stock)
				;

		if (printOrderDetails) {
			List<Order> orders = DatabaseOperations.loadOrdersFromDataBaseForSpecificProduct(code);
			sb.append("\nCost: ").append(cost_price).append(USDorNIS);
			if(orders.isEmpty())
				sb.append("\nNo orders for this product yet.\n");
			else {
			sb.append("\n===Orders for the product===\n");
			Iterator<Order> orderIterator = orders.iterator();
			while (orderIterator.hasNext()) {
				Order o = orderIterator.next();
				sb.append(o.toString(true));
				setTotalProfitForProduct(selling_price*o.getQuantity() - cost_price*o.getQuantity());
			}
		}
		}

		return sb.toString();
	}


	  @Override
      public int compareTo(Product other) {
          // Compare by code
          int codeComparison = this.code.compareToIgnoreCase(other.code);
              return codeComparison;
      }

}
