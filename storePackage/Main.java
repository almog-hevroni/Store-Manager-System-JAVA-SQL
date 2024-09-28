package storePackage;

import java.util.Scanner;
import java.util.Set;

import dataBasePackage.DatabaseConnectionManager;

public class Main {

	public static void main(String[] args) {
		OnlineStore store = OnlineStore.getInstance();
		UserIO userIO = UserIO.getInstance();
		Scanner s = new Scanner(System.in);
		boolean exit = false;
		do {
			System.out.println("==== Menu ====");
			System.out.println("1) Add new Products.");
			System.out.println("2) Remove a Product.");
			System.out.println("3) Edit the stock of a Product.");
			System.out.println("4) Add an Order to a Product.");
			System.out.println("5) Print all details about a specific Product.");
			System.out.println("6) Print all the products in store with details.");
			System.out.println("7) Print all the orders details of a specific product.");
			System.out.println("8) Exit.");
			
			String input = s.nextLine();

			int choice = Integer.parseInt(input);

			switch (choice) {
			case 1:
				//Add new product to stock with unique code
				if(store.addNewProductToStore(userIO.getNewProductDetails(store)))
					System.out.println("Product added successfully");
				else 
					System.out.println("Product already exists");
				
				break;
				
			case 2:
				//Remove product from stock by code
				Set<Product> products = store.getProducts();
				if(products.size() != 0) {
				System.out.println(userIO.printAllProductsInStore(products, false));
				if(store.removeProductFromStore(userIO.getCodeOfProduct("")))
					System.out.println("Product removed successfully");
				else 
					System.out.println("Product doesn't exist");
				} else
					System.out.println("No products yet, add products first.");
				break;
				
			case 3:
				//Edit quantity of a product in stock.
				Set<Product> products1 = store.getProducts();
				if(products1.size() != 0) {
					System.out.println(userIO.printAllProductsInStore(products1, false));
					String code = userIO.getCodeOfProduct("");
					int quantity = userIO.getNewQuantity();
					if(store.editQuantityOfProduct(code, quantity))
						System.out.println("Stock added successfully.");
					else System.out.println("Something went wrong.");
				} else
					System.out.println("No products yet, add products first.");
				break;
				
			case 4:
				//Add new order
				Set<Product> products2 = store.getProducts();
				if(products2.size() != 0) {
					boolean o = store.addNewOrder(userIO.getNewOrderDetails(products2));
					if(o) {
						System.out.println("Order added successfully.");
					} else {
						System.out.println("Something went wrong, order wasn't added.");
					}
				} else {
					System.out.println("No products yet, add products first.");
				}
				break;
					
			case 5:
				// print product with details
				Set<Product> products3 = store.getProducts();
				if(products3.size() != 0)
					userIO.printAllDetailsOfASpecificProduct(products3);
				else
					System.out.println("No products");
				break;
				
			case 6:
				//Print all products with order details.
				Set<Product> products4 = store.getProducts();
				if(products4.size() != 0)
				System.out.println(userIO.printAllProductsInStore(products4, true));
				else
					System.out.println("No products");
				break;
				
			case 7:
				// print all orders of a specific product.
				Set<Product> products5 = store.getProducts();
				if(products5.size() != 0)
					userIO.printOrdersOfSpecificProduct(products5);
				else
					System.out.println("No products");
				
				break;
				
				
			case 8:
				System.out.println("Bye Bye!");
				DatabaseConnectionManager.closeConnection();
				exit = true;
				break;
				
			default:
				System.out.println("Wrong choise! Try again!");
				break;
			}

		} while (!exit);
		
		s.close();

	}

}
