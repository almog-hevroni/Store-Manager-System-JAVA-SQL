package storePackage;

import java.util.Scanner;

public class ProductFactory {
	
	public Product createProduct(ProductType type, String code, String product_name, double cost_price, double selling_price, int stock) {
		switch(type.name()) {
		case "STORE":
			return new SoldInStore(product_name,code,cost_price,selling_price,stock,type);
		case "WHOLESALERS":
			return new SoldInWholesalers(product_name, code, cost_price, selling_price, stock,type);
		case "WEB":
			return createSoldThroughWebsiteProduct(product_name, code, cost_price, selling_price, stock,type);
		default:
			return null;
		}
	}
	
	private Product createSoldThroughWebsiteProduct(String product_name, String code, double cost_price, double selling_price, int stock,ProductType type) {
		@SuppressWarnings("resource")
		Scanner s = new Scanner(System.in);
		System.out.println("What is the weight of the product?");
		double weight = s.nextDouble();
		return new SoldThroughWebsite(product_name, code, cost_price, selling_price,stock,weight,type);
	}
}

