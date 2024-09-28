package storePackage;

public abstract class DeliveryCompany implements OrderSubscriber{
	@SuppressWarnings("unused")
	private String contact,whatsappNumber;
	private double finalCost;
	private DeliveryType type;
	
	public DeliveryCompany(String contact, String whatsappNumber,DeliveryType type) {
		this.contact = contact;
		this.whatsappNumber = whatsappNumber;
		finalCost = 0;
		this.type = type;
	}

	public double getFinalCost() {
		return finalCost;
	}

	public void setFinalCost(double finalCost) {
		this.finalCost = finalCost;
	}

	public DeliveryType getType() {
		return type;
	}
	
	
	
	
	
}

