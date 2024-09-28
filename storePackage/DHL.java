package storePackage;

public class DHL extends DeliveryCompany {

	private final int deliveryCost = 100;
	private final int importTax = 20; // for now

	public DHL(DeliveryType deliveryType) {
		super("David", "0526325213", deliveryType);
	}

	@Override
	public void update(WebOrder order) {
		double finalCost;
		if (order.getDeliveryType() == DeliveryType.EXPRESS_SHIPPING) {
			finalCost = deliveryCost + importTax;
		} else {
			double productPrice = order.getProduct().getSelling_price();
			finalCost = productPrice * order.getQuantity() * 0.1;
			if (finalCost > 100) {
				finalCost = 100;
			}
		}
		setFinalCost(finalCost);
	}

}
