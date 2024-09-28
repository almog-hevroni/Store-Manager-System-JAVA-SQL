package storePackage;

public class Fedex extends DeliveryCompany {

	private final int deliveryCostExpress = 50;
	private final int deliveryCostStandart = 10;
	private final int importTax = 20; //in dollars.
	
	public Fedex(DeliveryType deliveryType) {
		super("Boris", "0547659817", deliveryType);
	}

	@Override
	public void update(WebOrder order) {
		double finalCost;
		SoldThroughWebsite p = (SoldThroughWebsite) order.getProduct();
		double weight = p.getProductWeight() * order.getQuantity();
		if(order.getDeliveryType() == DeliveryType.EXPRESS_SHIPPING) {
			finalCost = (weight/10) * deliveryCostExpress + importTax;
		} else {
			finalCost = (weight/10) * deliveryCostStandart;
		}
		setFinalCost(finalCost);
	}

}
