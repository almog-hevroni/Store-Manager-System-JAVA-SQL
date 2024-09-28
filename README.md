# Store Management System

## Project Overview
This Store Management System is a robust e-commerce platform developed using Java and PostgreSQL. It provides a comprehensive solution for managing products, orders, and customers across various sales channels including in-store, wholesale, and online.

## Key Features
- Multi-channel product management (store, wholesale, web)
- Flexible order processing system
- Real-time currency conversion for international sales
- Customer information management
- Delivery logistics handling
- Invoice generation for customers and accountants
- Profit tracking and reporting

## Technical Highlights
- **Object-Oriented Design**: Utilizes inheritance, interfaces, and enums for a scalable and maintainable codebase.
- **Database Integration**: Incorporates a relational database (PostgreSQL) using JDBC for persistent data storage.
- **Design Patterns**: Implements Singleton and Factory patterns for efficient code organization.
- **User Interface**: Features a user-friendly command-line interface for easy interaction.

## System Architecture
1. **Product Management**
   - Abstract `Product` class with specific implementations for different sales channels
   - `ProductFactory` for creating various product types

2. **Order Processing**
   - Abstract `Order` class with channel-specific order types (Store, Wholesalers, Web)
   - Real-time currency conversion for web orders

3. **Customer Management**
   - `Customer` class for storing customer details
   - `Address` class for managing delivery information

4. **Delivery System**
   - `DeliveryCompany` interface with implementations for different carriers
   - `DeliveryType` enum for specifying shipping methods

5. **Financial Management**
   - `Invoice` system for generating customer and accountant invoices
   - Profit calculation and tracking at product and store levels

6. **Database Operations**
   - `DatabaseConnectionManager` for handling database connections
   - `DatabaseOperations` for executing SQL queries and managing data persistence

## Dependencies
To run this project, you'll need the following:

1. **Java Development Kit (JDK)**: Version 8 or higher
   - The core programming language used for this project

2. **PostgreSQL**: Version 9.6 or higher
   - The relational database management system used for data storage

3. **PostgreSQL JDBC Driver**: Compatible with your PostgreSQL version
   - Enables Java application to interact with PostgreSQL database
   - Add the following dependency to your project:
     ```
     <dependency>
         <groupId>org.postgresql</groupId>
         <artifactId>postgresql</artifactId>
         <version>42.2.23</version>
     </dependency>
     ```
   - Or download the .jar file from the [official PostgreSQL JDBC Driver website](https://jdbc.postgresql.org/download.html)

4. **Scanner Class**: Part of Java Standard Library
   - Used for reading user input from the command line

Make sure to have these dependencies properly set up in your development environment before running the application.

## Getting Started
1. Ensure you have Java and PostgreSQL installed on your system.
2. Clone the repository to your local machine.
3. Set up the PostgreSQL database using the provided schema.
4. Update the database connection details in `DatabaseConnectionManager.java`.
5. Compile and run the `Main.java` file to start the application.

## Usage
The system provides a command-line interface with the following options:
1. Add new Products
2. Remove a Product
3. Edit the stock of a Product
4. Add an Order to a Product
5. Print all details about a specific Product
6. Print all the products in store with details
7. Print all the orders details of a specific product
8. Exit

Follow the on-screen prompts to navigate through the system and perform various operations.

