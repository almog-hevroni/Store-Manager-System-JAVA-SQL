package dataBasePackage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import storePackage.*;

public class DatabaseOperations {
	public static Set<Product> loadProductsFromDataBase() {
		ProductFactory factory = new ProductFactory();
		Set<Product> products = new TreeSet<Product>();
		String sql = "SELECT * FROM product NATURAL LEFT JOIN web_product;";
		Product product = null;
		try {
			Connection conn = DatabaseConnectionManager.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String productTypeString = rs.getString("product_type");
				ProductType productType = ProductType.valueOf(productTypeString);
				String productId = rs.getString("product_id");
				String productName = rs.getString("product_name");
				double sellingPrice = rs.getDouble("selling_price");
				double costPrice = rs.getDouble("cost_price");
				int stockQuantity = rs.getInt("stock_quantity");

				if (!productTypeString.equals(ProductType.WEB.name())) {
					product = factory.createProduct(productType, productId, productName, costPrice, sellingPrice,
							stockQuantity);
				} else {
					double weight = rs.getDouble("weight");
					product = new SoldThroughWebsite(productName, productId, costPrice, sellingPrice, stockQuantity,
							weight, productType);
				}
				products.add(product);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return products;
	}
	
	public static List<Order> loadOrdersFromDataBaseForSpecificProduct(String productId) {
	    List<Order> orders = new ArrayList<Order>();
	    Order order = null;
	    double profit = 0;
	    OnlineStore store = OnlineStore.getInstance();
	    Product product = findProductInDataBase(productId);
	    
	    String sql = "SELECT * FROM orders NATURAL LEFT JOIN customer NATURAL LEFT JOIN web_order NATURAL LEFT JOIN adress WHERE product_id = ?";
	    
	    try {
	        Connection conn = DatabaseConnectionManager.getConnection();
	        PreparedStatement pst = conn.prepareStatement(sql);
	        pst.setString(1, productId);
	        ResultSet rs = pst.executeQuery();

	        while (rs.next()) {
	            String customerName = rs.getString("name");
	            String customerPhone = rs.getString("phone_number");
	            Customer customer = new Customer(customerName, customerPhone);
	            String orderType = rs.getString("order_type");
	            int quantity = rs.getInt("quantity");
	            String orderId = rs.getString("order_id");
	            profit += (product.getSelling_price() - product.getCost_price()) * quantity;
	            store.setTotalProfitForStore(profit);

	            if (orderType.equals(ProductType.STORE.name())) {
	                Invoice accountantInvoice = new InvoiceForAccountant(product.getSelling_price() * quantity,
	                        product.getCost_price() * quantity);
	                Invoice customerInvoice = new InvoiceForCustomer(product.getSelling_price() * quantity);
	                order = new StoreOrder(customer, quantity, orderId, product, customerInvoice, accountantInvoice,
	                        orderType);
	            } else if (orderType.equals(ProductType.WHOLESALERS.name())) {
	                Invoice accountantInvoice = new InvoiceForAccountant(product.getSelling_price() * quantity,
	                        product.getCost_price() * quantity);
	                order = new WholeSalersOrder(customer, quantity, orderId, product, accountantInvoice, orderType);
	            } else {
	                String deliveryTypeString = rs.getString("delivery_type");
	                DeliveryType deliveryType = deliveryTypeString != null ? DeliveryType.valueOf(deliveryTypeString) : null;

	                String street = rs.getString("street");
	                String city = rs.getString("city");
	                String country = rs.getString("country");
	                int houseNumber = rs.getInt("house_number");
	                Address address = new Address(country, city, street, houseNumber);

	                order = new WebOrder(customer, quantity, orderId, product, deliveryType, address, orderType);
	                store.setDeliveryCompanyForWebOrder(order);
	            }

	            order.setProfitFromOrder(profit);
	            orders.add(order);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return orders;
	}
	
	public static Product findProductInDataBase(String productId) {
	    ProductFactory factory = new ProductFactory();
	    Product product = null;
	    String sql = "SELECT * FROM product NATURAL LEFT JOIN web_product WHERE product_id = ?";

	    try {
	        Connection conn = DatabaseConnectionManager.getConnection();
	        PreparedStatement pst = conn.prepareStatement(sql);
	        pst.setString(1, productId);
	        ResultSet rs = pst.executeQuery();

	        if (rs.next()) {
	            String productTypeString = rs.getString("product_type");
	            ProductType productType = ProductType.valueOf(productTypeString);
	            String productName = rs.getString("product_name");
	            int sellingPrice = rs.getInt("selling_price");
	            int costPrice = rs.getInt("cost_price");
	            int stockQuantity = rs.getInt("stock_quantity");

	            if (!productType.name().equals("WEB")) {
	                product = factory.createProduct(productType, productId, productName, costPrice, sellingPrice, stockQuantity);
	            } else {
	                int weight = rs.getInt("weight");
	                product = new SoldThroughWebsite(productName, productId, costPrice, sellingPrice, stockQuantity, weight, productType);
	            }
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return product;
	}

	
	public static boolean doesOrderExists(String orderId) {
	    String sql = "SELECT order_id FROM orders WHERE order_id = ?";
	    try {
	        Connection conn = DatabaseConnectionManager.getConnection();
	        PreparedStatement pst = conn.prepareStatement(sql);
	        pst.setString(1, orderId);
	        ResultSet rs = pst.executeQuery();

	        if (rs.next()) {
	            return true;
	        } else {
	            return false;
	        }
	    } catch (SQLException e) {
	        System.out.println("Something went wrong.");
	        e.printStackTrace();
	        return false;
	    }
	}

	
	public static boolean addNewProduct(Product p) {
	    String sql = "INSERT INTO product(product_id,product_name,selling_price,cost_price,stock_quantity,product_type) "
	               + "VALUES (?, ?, ?, ?, ?, ?)";
	    try {
	        Connection conn = DatabaseConnectionManager.getConnection();
	        PreparedStatement pst = conn.prepareStatement(sql);
	        pst.setString(1, p.getCode());
	        pst.setString(2, p.getName());
	        pst.setDouble(3, p.getSelling_price());
	        pst.setDouble(4, p.getCost_price());
	        pst.setInt(5, p.getStock());
	        pst.setString(6, p.getType().name());
	        pst.executeUpdate();

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	    if (p.getType().name().equals("WEB")) {
	        return addNewWebProduct(p);
	    }
	    return true;
	}

	
	public static boolean addNewWebProduct(Product p) {
	    SoldThroughWebsite p1 = (SoldThroughWebsite) p;
	    String sql = "INSERT INTO web_product(product_id,weight) VALUES (?, ?)";
	    try {
	        Connection conn = DatabaseConnectionManager.getConnection();
	        PreparedStatement pst = conn.prepareStatement(sql);
	        pst.setString(1, p.getCode());
	        pst.setDouble(2, p1.getProductWeight());
	        pst.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	    return true;
	}

	public static boolean editQuantityOfProduct(String productId, int quantity) {
	    String sql = "UPDATE product SET stock_quantity = ? WHERE product_id = ?";
	    try {
	        Connection conn = DatabaseConnectionManager.getConnection();
	        PreparedStatement pst = conn.prepareStatement(sql);
	        pst.setInt(1, quantity);
	        pst.setString(2, productId);

	        if (pst.executeUpdate() == 0) {
	            return false;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	    return true;
	}
	
	public static boolean addNewOrderToDataBase(Order order) {
	    try {
	        Connection conn = DatabaseConnectionManager.getConnection();
	        
	        // Insert into orders table
	        String sql = "INSERT INTO orders (order_id,product_id,quantity,order_type) VALUES (?, ?, ?, ?)";
	        PreparedStatement pst = conn.prepareStatement(sql);
	        pst.setString(1, order.getCode());
	        pst.setString(2, order.getProduct().getCode());
	        pst.setInt(3, order.getQuantity());
	        pst.setString(4, order.getOrderType());
	        pst.executeUpdate();

	        // Insert into customer table
	        sql = "INSERT INTO customer(order_id,name,phone_number) VALUES (?, ?, ?)";
	        pst = conn.prepareStatement(sql);
	        pst.setString(1, order.getCode());
	        pst.setString(2, order.getCustomer().getCustomerName());
	        pst.setString(3, order.getCustomer().getCustomerPhone());
	        pst.executeUpdate();

	        // If it's a web order, insert into web_order and address tables
	        if (order.getOrderType().equals(ProductType.WEB.name())) {
	            WebOrder webOrder = (WebOrder) order;

	            // Insert into web_order table
	            sql = "INSERT INTO web_order(order_id,delivery_type) VALUES (?, ?)";
	            pst = conn.prepareStatement(sql);
	            pst.setString(1, webOrder.getCode());
	            pst.setString(2, webOrder.getDeliveryType().name());
	            pst.executeUpdate();

	            // Insert into address table
	            Address address = webOrder.getDeliveryAdress();
	            sql = "INSERT INTO adress(order_id,street,city,country,house_number) VALUES (?, ?, ?, ?, ?)";
	            pst = conn.prepareStatement(sql);
	            pst.setString(1, webOrder.getCode());
	            pst.setString(2, address.getStreet());
	            pst.setString(3, address.getCity());
	            pst.setString(4, address.getCountry());
	            pst.setInt(5, address.getNumber());
	            pst.executeUpdate();
	        }

	    } catch (SQLException e) {
	        return false;
	    }
	    
	    int newQuantity = order.getProduct().getStock() - order.getQuantity();
	    return editQuantityOfProduct(order.getProduct().getCode(), newQuantity);
	}
	
	public static boolean removeProduct(String productId) {
	    String sql = "SELECT order_id FROM orders WHERE product_id = ?";
	    try {
	        Connection conn = DatabaseConnectionManager.getConnection();
	        PreparedStatement pst = conn.prepareStatement(sql);
	        pst.setString(1, productId);
	        ResultSet rs = pst.executeQuery();

	        while (rs.next()) {
	            String orderId = rs.getString("order_id");

	            // Delete customer
	            String deleteCustomer = "DELETE FROM customer WHERE order_id = ?";
	            PreparedStatement pstDeleteCustomer = conn.prepareStatement(deleteCustomer);
	            pstDeleteCustomer.setString(1, orderId);
	            pstDeleteCustomer.executeUpdate();

	            // Handle web orders
	            String deleteAddress = "DELETE FROM adress WHERE order_id = ?";
	            PreparedStatement pstDeleteAddress = conn.prepareStatement(deleteAddress);
	            pstDeleteAddress.setString(1, orderId);
	            if (pstDeleteAddress.executeUpdate() != 0) {
	                String deleteWebOrder = "DELETE FROM web_order WHERE order_id = ?";
	                PreparedStatement pstDeleteWebOrder = conn.prepareStatement(deleteWebOrder);
	                pstDeleteWebOrder.setString(1, orderId);
	                pstDeleteWebOrder.executeUpdate();
	            }

	            // Delete order
	            String deleteOrder = "DELETE FROM orders WHERE order_id = ?";
	            PreparedStatement pstDeleteOrder = conn.prepareStatement(deleteOrder);
	            pstDeleteOrder.setString(1, orderId);
	            pstDeleteOrder.executeUpdate();
	        }

	        // Handle web products
	        if (productId.startsWith(ProductType.WEB.getPrefixProductCode())) {
	            String deleteWebProduct = "DELETE FROM web_product WHERE product_id = ?";
	            PreparedStatement pstDeleteWebProduct = conn.prepareStatement(deleteWebProduct);
	            pstDeleteWebProduct.setString(1, productId);
	            pstDeleteWebProduct.executeUpdate();
	        }

	        // Delete product
	        String deleteProduct = "DELETE FROM product WHERE product_id = ?";
	        PreparedStatement pstDeleteProduct = conn.prepareStatement(deleteProduct);
	        pstDeleteProduct.setString(1, productId);
	        pstDeleteProduct.executeUpdate();

	    } catch (SQLException e) {
	        return false;
	    }
	    return true;
	}


}
