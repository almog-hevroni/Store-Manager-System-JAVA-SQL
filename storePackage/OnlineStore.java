package storePackage;

import dataBasePackage.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class OnlineStore implements OrderPublisher {
	private static OnlineStore instance;
	@SuppressWarnings("unused")
	private DatabaseConnectionManager databaseConnection;
	private List<OrderSubscriber> observers = new ArrayList<>();
	private double totalProfitOfStore;

	private OnlineStore() {
		databaseConnection = new DatabaseConnectionManager();
		registerDeliveryCompany(new DHL(DeliveryType.EXPRESS_SHIPPING));
		registerDeliveryCompany(new DHL(DeliveryType.STANDARD_SHIPPING));
		registerDeliveryCompany(new Fedex(DeliveryType.EXPRESS_SHIPPING));
		registerDeliveryCompany(new Fedex(DeliveryType.STANDARD_SHIPPING));
	}

	public static OnlineStore getInstance() {
		if (instance == null) {
			instance = new OnlineStore();
		}
		return instance;
	}

	public Set<Product> getProducts() {
		return DatabaseOperations.loadProductsFromDataBase();
	}

	public void setTotalProfitForStore(double profit) {
		this.totalProfitOfStore = profit;
	}

	public double getTotalProfitOfStore() {
		return totalProfitOfStore;
	}

	public boolean addNewProductToStore(Product p) {
		Set<Product> products2 = getProducts();
		for (Product product : products2) {
			if (product.getName().equalsIgnoreCase(p.getName())
					&& product.getClass().getSimpleName().equals(p.getClass().getSimpleName())) {
				return false; // Found a duplicate based on name and type, do not add
			}
		}
		return DatabaseOperations.addNewProduct(p);
	}
	
	public boolean addNewOrder(Order order) {
		if(order == null)
			return false;
			
		return DatabaseOperations.addNewOrderToDataBase(order);
	}

	public boolean removeProductFromStore(String code) {
		Product p = findProductByCode(code);
		if (p != null) {
			// Remove orders related to the product from the allOrders list
			return DatabaseOperations.removeProduct(code);
		}
		return false;
	}

	public Product findProductByCode(String code) {
		return DatabaseOperations.findProductInDataBase(code);
	}
	
	public boolean findOrderByCode(String orderId) {
		return DatabaseOperations.doesOrderExists(orderId);
	}
	
	public List<Order> getOrdersForSpecificProduct(String productId) {
		return DatabaseOperations.loadOrdersFromDataBaseForSpecificProduct(productId);
	}

	public boolean editQuantityOfProduct(String code, int newStock) {
		return DatabaseOperations.editQuantityOfProduct(code, newStock);
	}

	public void setDeliveryCompanyForWebOrder(Order order) {
		double cheapestCost = Double.MAX_VALUE;
		DeliveryCompany cheapestCompany = null;
		notifyDeliveryCompanies((WebOrder) order);
		for (OrderSubscriber observer : observers) {
			DeliveryCompany company = (DeliveryCompany) observer;
			if (company.getType() == ((WebOrder) order).getDeliveryType()) {
				double cost = company.getFinalCost();
				if (cost < cheapestCost) {
					cheapestCost = cost;
					cheapestCompany = company;
				}
			}
		}
		((WebOrder) order).setDeliveryCompanyName(cheapestCompany.getClass().getSimpleName());
		((WebOrder) order).setDeliveryPrice(cheapestCost);
	}

	public void setNewQuantityForProduct(Order order) {
		Product p = findProductByCode(order.getProduct().getCode());
		int newQuantity = p.getStock() - order.getQuantity();
		DatabaseOperations.editQuantityOfProduct(p.getCode(), newQuantity);
	}

	@Override
	public void registerDeliveryCompany(OrderSubscriber subscriber) {
		observers.add(subscriber);
	}

	@Override
	public void removeDeliveryCompany(OrderSubscriber subscriber) {
		observers.remove(subscriber);

	}

	@Override
	public void notifyDeliveryCompanies(WebOrder order) {
		for (OrderSubscriber observer : observers) {
			observer.update(order);
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n== Total profit of the store ==\n").append(totalProfitOfStore).append('₪');
		return sb.toString();
	}

}