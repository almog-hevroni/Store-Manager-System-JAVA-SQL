package storePackage;

public class InvoiceForCustomer extends Invoice{
	
	private final double VAT = 0.17;
	private double VATOfProduct;
	
	public InvoiceForCustomer(double transactionDetails) {
		super(transactionDetails);
		VATOfProduct = transactionDetails * VAT;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n==Customer Invoice==").append("\n")
		.append("Price: ").append(orderTotalPrice).append("₪")
		.append("\nPrice including VAT: ").append(orderTotalPrice+VATOfProduct).append("₪").append("\n");
		return sb.toString();
	}
}
