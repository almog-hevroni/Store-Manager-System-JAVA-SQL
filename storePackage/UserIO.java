package storePackage;

import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class UserIO {
	private static UserIO instance;
	private static Scanner s = new Scanner(System.in);
	private String input;

	public static UserIO getInstance() {
		if (instance == null) {
			instance = new UserIO();
		}
		return instance;
	}

	public Product getNewProductDetails(OnlineStore store) {
		ProductFactory factory = new ProductFactory();
		ProductType type;
		String name, code;
		int costPrice, sellingPrice;
		int stock;
		String codeType;

		do {
			System.out.println("Please enter the type of the product:");
			System.out.println("0) Sold In Store.");
			System.out.println("1) Sold In Wholesalers.");
			System.out.println("2) Sold Through Website.");
			input = s.nextLine();
			int choice = Integer.parseInt(input);
			switch (choice) {
			case 0:
				type = ProductType.STORE;
				break;
			case 1:
				type = ProductType.WHOLESALERS;
				break;
			case 2:
				type = ProductType.WEB;
				break;
			default:
				System.out.println("Invalid choice. Please enter a valid option.");
				continue;
			}
			codeType = type.getPrefixProductCode();
			break;
		} while (true);

		while (true) {
			System.out.printf("Enter the serial code of the product, the code should start with: %s\n", codeType);
			code = s.nextLine();
			if (!code.startsWith(codeType) || code.length() < 6)
				System.out.printf("Code sould start with: %s and be at least 6 chars long\n", codeType);
			else if (store.findProductByCode(code) != null)
				System.out.println("A product with this code already exists.");

			else
				break;

		}

		System.out.println("Enter the name of the product: ");
		name = s.nextLine();

		System.out.println("Whats the cost of the product? (in ₪)");
		input = s.nextLine();
		costPrice = Integer.parseInt(input);

		System.out.println("Whats the selling price of the product? (in ₪)");
		input = s.nextLine();
		sellingPrice = Integer.parseInt(input);

		System.out.println("How namy items would you like to add?");
		input = s.nextLine();
		stock = Integer.parseInt(input);

		return factory.createProduct(type, code, name, costPrice, sellingPrice, stock);

	}

	public String getCodeOfProduct(String productCodeType) {
		String code;
		do {
			System.out.println("Enter the code of the product:");
			code = s.nextLine();
			if (!code.startsWith(productCodeType))
				System.out.printf("Code should start with: %s\n", productCodeType);
			else
				return code;
		} while (true);

	}

	public int getNewQuantity() {
		int quantity;
		while (true) {
			System.out.println("Enter updated stock: ");
			input = s.nextLine();
			quantity = Integer.parseInt(input);

			if (quantity < 0)
				System.out.println("Quantity must be greater then 0.");
			else
				break;
		}
		return quantity;
	}

	public Order getNewOrderDetails(Set<Product> set) {
		OnlineStore store = OnlineStore.getInstance();
		ProductType type;
		String codeType, code, productCodeType;
		Product p = null;
		Order o = null;
		int quantity;
		do {
			System.out.println("What platform would you like to order from?");
			System.out.println("0) Store.");
			System.out.println("1) Wholesalers.");
			System.out.println("2) Website.");
			input = s.nextLine();
			int choice = Integer.parseInt(input);
			switch (choice) {
			case 0:
				type = ProductType.STORE;
				break;
			case 1:
				type = ProductType.WHOLESALERS;
				break;
			case 2:
				type = ProductType.WEB;
				break;
			default:
				System.out.println("Invalid choice. Please enter a valid option.");
				continue;
			}
			codeType = type.getPrefixOrderCode();
			productCodeType = type.getPrefixProductCode();
			break;
		} while (true);

		do {
			if (!printProductsByType(set, type))
				return null;

			input = getCodeOfProduct(productCodeType);
			p = store.findProductByCode(input);
			if (p == null)
				System.out.println("No product found with this code, try again.");
			else
				break;

		} while (true);

		quantity = getQuantityToOrder(p);
		Customer customer = getCustomerDetails();
		getCodeOfOrder(p, codeType);
		code = input;
		if (type == ProductType.WEB) {
			Address adress = getAdress();
			o = new WebOrder(customer, quantity, code, p, getDeleveryType(p), adress,ProductType.WEB.name());
		} else if (type == ProductType.WHOLESALERS) {
			Invoice accountantInvoice = new InvoiceForAccountant(p.getSelling_price() * quantity,
					p.getCost_price() * quantity);
			o = new WholeSalersOrder(customer, quantity, code, p, accountantInvoice,ProductType.WHOLESALERS.name());
		} else {
			Invoice accountantInvoice = new InvoiceForAccountant(p.getSelling_price() * quantity,
					p.getCost_price() * quantity);
			Invoice customerInvoice = new InvoiceForCustomer(p.getSelling_price() * quantity);
			o = new StoreOrder(customer, quantity, code, p, customerInvoice, accountantInvoice,ProductType.STORE.name());
		}
		double cost = p.getCost_price();
		double sell = p.getSelling_price();
		double profit = (sell * quantity) - (cost * quantity);
		//convert to nis
		if(type == ProductType.WEB)
			profit *= 4;
		
		o.setProfitFromOrder(profit);
		p.setTotalProfitForProduct(profit);
		return o;
	}

	public DeliveryType getDeleveryType(Product p) {
		if (p.getCost_price() > 20) {
			do {
				System.out.println("Choose delevery: 1) Express, 2) Standart.");
				int choise = Integer.parseInt(s.nextLine());
				switch (choise) {
				case 1:
					return DeliveryType.EXPRESS_SHIPPING;
				case 2:
					System.out.println("You should check about the payment of the fees for the delivery.");
					return DeliveryType.STANDARD_SHIPPING;
				default:
					System.out.println("Wrong input.");
				}
			} while (true);
		} else {
			System.out.println("You should check about the payment of the fees for the delivery.");
			return DeliveryType.STANDARD_SHIPPING;
		}
	}

	public int getQuantityToOrder(Product p) {
		int quantity = 0;
		do {
			System.out.println("Enter the qunatity you'd like to order:");
			input = s.nextLine();
			quantity = Integer.parseInt(input);
			if (p.getStock() < quantity)
				System.out.printf("Not enough in stock! Max order for this product is: %d\n", p.getStock());
			else
				break;
		} while (true);
		return quantity;
	}

	public Customer getCustomerDetails() {
		String name, phone;
		do {
			System.out.println("Please enter your name for the order:");
			input = s.nextLine();
			if (!checkValidName(input))
				System.out.println("Please enter letters and spaces only, name must be at least 3 letters.");
			else {
				name = input;
				break;
			}

		} while (true);

		do {
			System.out.println("Please enter your mobile phone number: must be 10 digits and start with 05.");
			input = s.nextLine();
			if (checkValidPhoneNumber(input)) {
				phone = input;
				break;
			}
		} while (true);

		Customer c = new Customer(name, phone);
		return c;
	}

	private void getCodeOfOrder(Product p, String codeType) {
		OnlineStore store = OnlineStore.getInstance();
		do {
			System.out.printf("Please enter the order code, should start with %s:", codeType);
			input = s.nextLine();
			if (!input.startsWith(codeType) || input.length() < 10)
				System.out.printf("Code sould start with: %s and should be at least 10 chars\n", codeType);
			else if (store.findOrderByCode(codeType))
				System.out.println("An order with this code already exists.");
			else
				break;
		} while (true);
	}

	private Address getAdress() {
		String country, city, street;
		int number;
		System.out.println("Enter the Country of delivery:");
		country = s.nextLine();
		System.out.println("Enter the city of delivery:");
		city = s.nextLine();
		System.out.println("Enter the street of delivery:");
		street = s.nextLine();
		System.out.println("Enter the number of the building:");
		number = Integer.parseInt(s.nextLine());
		return new Address(country, city, street, number);
	}

	private boolean checkValidName(String str) {
		if (str.length() < 3)
			return false;

		for (char c : str.toCharArray()) {
			if (!Character.isLetter(c) && !Character.isWhitespace(c)) {
				return false;
			}
		}
		return true;
	}

	private boolean checkValidPhoneNumber(String str) {
		if (str.length() != 10)
			return false;

		String firstTwoDigits = str.substring(0, 2);
		if (firstTwoDigits.compareTo("05") != 0)
			return false;

		return true;
	}

	public String printAllProductsInStore(Set<Product> set, boolean printOrderDetails) {
		char USDorNIS;
		OnlineStore store = OnlineStore.getInstance();
		Iterator<Product> productIterator = set.iterator();
		StringBuilder sb = new StringBuilder();
		sb.append("===Products in Store===\n");
		while (productIterator.hasNext()) {
			Product p = productIterator.next();
			if(p.getClass().getSimpleName().equals("SoldThroughWebsite"))
				USDorNIS = '$';
			else
				USDorNIS = '₪';
			
			sb.append(p.toString(printOrderDetails,USDorNIS));
		}
		if(printOrderDetails)
		sb.append(store.toString());
		
		return sb.toString();
	}

	private boolean printProductsByType(Set<Product> set, ProductType type) {
		Iterator<Product> productIterator = set.iterator();
		char USDorNIS;
		if(type == ProductType.WEB)
			USDorNIS = '$';
		else
			USDorNIS = '₪';
		StringBuilder sb = new StringBuilder();
		while (productIterator.hasNext()) {
			Product p = productIterator.next();
			if (p.getType().equals(type) && p.getStock() > 0)
				sb.append(p.toString(false,USDorNIS));
		}
		if (sb.isEmpty()) {
			return false;
		} else {
			System.out.println(sb);
			return true;
		}
	}

	public void printOrdersOfSpecificProduct(Set<Product> set) {
		OnlineStore store = OnlineStore.getInstance();
		System.out.println(printAllProductsInStore(set, false));
		System.out.println("Please enter the code of the product you'd like to print:");
		input = s.nextLine();
		Product p = store.findProductByCode(input);
		List<Order> orders = store.getOrdersForSpecificProduct(input);
		if(p != null && !orders.isEmpty()) {	
			for(Order o : orders) {
				System.out.println(o.toString(true));
			}
		} else {
			System.out.println("No orders found for this product.");
		}
	}
	
	public void printAllDetailsOfASpecificProduct(Set<Product> set) {
		OnlineStore store = OnlineStore.getInstance();
		char USDorNIS;
		System.out.println(printAllProductsInStore(set, false));
		System.out.println("Please enter the code of the product you'd like to print:");
		input = s.nextLine();
		Product p = store.findProductByCode(input);
		if(p != null) {
			if(p.getClass().getSimpleName().equals("SoldThroughWebsite"))
				USDorNIS = '$';
			else
				USDorNIS = '₪';
			System.out.println("Product type: " + p.getClass().getSimpleName());
			System.out.println(p.toString(true,USDorNIS));
			System.out.println("\n== Total profit of the product ==");
			System.out.println(p.getTotalProfitForProduct());
		} else {
			System.out.println("No product with this code was found.");
		}
	}

}
