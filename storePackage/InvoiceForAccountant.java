package storePackage;

public class InvoiceForAccountant extends Invoice{

	double profit;

	public InvoiceForAccountant(double transactionDetails, double costPrice) {
		super(transactionDetails);
		profit = transactionDetails - costPrice;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n==Accountant Invoice==\n")
		.append("Total price: ").append(orderTotalPrice).append("₪")
		.append("\nProfit: ").append(profit).append("₪");
		return sb.toString();
	}

	
	
}
