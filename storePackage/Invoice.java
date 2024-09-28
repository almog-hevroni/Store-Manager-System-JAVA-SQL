package storePackage;

public abstract class Invoice {

	double orderTotalPrice;

	public Invoice(double transactionDetails) {
		this.orderTotalPrice = transactionDetails;
	}
	
	
}
