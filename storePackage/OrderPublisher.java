package storePackage;

public interface OrderPublisher {
	void registerDeliveryCompany(OrderSubscriber subscriber);
    void removeDeliveryCompany(OrderSubscriber subscriber);
    void notifyDeliveryCompanies(WebOrder order);
}
