package storePackage;

public class SoldThroughWebsite extends Product{
	private double productWeight;
	private final static int USD = 4;
	
	public SoldThroughWebsite(String product_name, String code, double cost_price, double selling_price, int stock, double productWeight,ProductType type) {
		super(product_name, code, cost_price/USD, selling_price/USD, stock,type);
		this.productWeight = productWeight;
	}

	public double getProductWeight() {
		return productWeight;
	}

	
	
}

