package storePackage;

public class WebOrder extends Order {
	private DeliveryType deliveryType;
	private double deliveryPrice;
	private String deliveryCompanyName;
	private Address deliveryAdress;

	public WebOrder(Customer customer, int quantity, String code, Product product, DeliveryType deliveryType,Address deliveryAdress,String orderType) {
		super(customer, quantity, code, product,orderType);
		this.deliveryType = deliveryType;
		this.deliveryAdress = deliveryAdress;
	}

	public Address getDeliveryAdress() {
		return deliveryAdress;
	}

	public DeliveryType getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryPrice(double deliveryPrice) {
		this.deliveryPrice = deliveryPrice;
	}

	public void setDeliveryCompanyName(String deliveryCompanyName) {
		this.deliveryCompanyName = deliveryCompanyName;
	}


	public String toString(boolean showProfit) {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString(showProfit))
		.append("\n==Delivery details==\n")
		.append("\nDelivery Company: ").append(deliveryCompanyName)
		.append("\nDelivery type: ").append(deliveryType)
		.append("\nDelivery Price ").append(deliveryPrice)
		.append(deliveryAdress.toString());
		
		return sb.toString();
	}

}
