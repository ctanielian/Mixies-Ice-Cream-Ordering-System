# Create the database if it does not already exist
CREATE DATABASE IF NOT EXISTS MixiesDB;

# Select the database so all tables are created inside it
USE MixiesDB;

# Table to store employee information
CREATE TABLE Employee (
    employeeID INT NOT NULL AUTO_INCREMENT,   # Unique ID for each employee
    employeeName VARCHAR(100) NOT NULL,       # Employee's name
    employeeRole VARCHAR(20) NOT NULL,        # Employee role (e.g., Manager, Employee)
    PRIMARY KEY (employeeID)                  # Primary key for Employee table
);

# Table to store ice cream flavor information
CREATE TABLE IceCreamFlavor (
    flavorID INT NOT NULL AUTO_INCREMENT,     # Unique ID for each flavor
    flavorName VARCHAR(100) NOT NULL,         # Name of the flavor
    seasonality VARCHAR(50),                  # Season or availability period
    stockLevel INT NOT NULL,                  # Current stock quantity
    remakeThreshold INT NOT NULL,             # Stock level to trigger remake
    allergens VARCHAR(255),                  # Allergen information
    availabilityStatus VARCHAR(30) NOT NULL,  # Availability status
    PRIMARY KEY (flavorID),                   # Primary key
    UNIQUE (flavorName)                       # Prevent duplicate flavors
);

# Table to store available toppings
CREATE TABLE Topping (
    toppingID INT NOT NULL AUTO_INCREMENT,    # Unique ID for topping
    toppingName VARCHAR(100) NOT NULL,        # Topping name
    PRIMARY KEY (toppingID),                  # Primary key
    UNIQUE (toppingName)                      # Prevent duplicates
);

# Table to store customer orders
CREATE TABLE Orders (
    orderID INT NOT NULL AUTO_INCREMENT,      # Unique order ID
    employeeID INT NOT NULL,                  # Employee who created order
    orderDate DATETIME NOT NULL,              # Date/time of order
    tip DECIMAL(6,2) DEFAULT 0.00,            # Tip amount
    total DECIMAL(8,2) NOT NULL,              # Total cost
    orderStatus VARCHAR(20) NOT NULL DEFAULT 'Open', # Order status
    PRIMARY KEY (orderID),

    # Link order to employee
    CONSTRAINT fk_orders_employee
        FOREIGN KEY (employeeID) REFERENCES Employee(employeeID)
        ON DELETE RESTRICT                    # Prevent deleting employee if used
        ON UPDATE CASCADE                     # Update ID if employee changes
);

# Table to store items within an order
CREATE TABLE OrderItem (
    orderItemID INT NOT NULL AUTO_INCREMENT,  # Unique item ID
    orderID INT NOT NULL,                     # Associated order
    flavorID INT NOT NULL,                    # Flavor used
    quantity INT NOT NULL,                    # Number of items
    itemCost DECIMAL(8,2) NOT NULL,           # Cost per item
    refundStatus ENUM('Not Refunded', 'Refunded') NOT NULL DEFAULT 'Not Refunded',
    PRIMARY KEY (orderItemID),

    # Link to Orders table
    CONSTRAINT fk_orderitem_order
        FOREIGN KEY (orderID) REFERENCES Orders(orderID)
        ON DELETE CASCADE                     # Delete items if order is deleted
        ON UPDATE CASCADE,

    # Link to IceCreamFlavor table
    CONSTRAINT fk_orderitem_flavor
        FOREIGN KEY (flavorID) REFERENCES IceCreamFlavor(flavorID)
        ON DELETE RESTRICT                    # Prevent deleting flavor if used
        ON UPDATE CASCADE
);

# Table to store toppings added to each order item
CREATE TABLE OrderItemTopping (
    orderItemID INT NOT NULL,                 # Order item reference
    toppingID INT NOT NULL,                   # Topping reference
    toppingQuantity INT NOT NULL DEFAULT 1,   # Quantity of topping
    PRIMARY KEY (orderItemID, toppingID),     # Composite key

    # Link to OrderItem
    CONSTRAINT fk_orderitemtopping_orderitem
        FOREIGN KEY (orderItemID) REFERENCES OrderItem(orderItemID)
        ON DELETE CASCADE                     # Delete toppings if item deleted
        ON UPDATE CASCADE,

    # Link to Topping
    CONSTRAINT fk_orderitemtopping_topping
        FOREIGN KEY (toppingID) REFERENCES Topping(toppingID)
        ON DELETE RESTRICT                    # Prevent deleting used toppings
        ON UPDATE CASCADE
);

# Show all tables
SHOW TABLES;

# Insert sample employee
INSERT INTO employee (employeeID, employeeName, employeeRole)
VALUES (1, 'Ava', 'Manager');

# Insert default flavors
INSERT INTO icecreamflavor (flavorName, seasonality, stockLevel, remakeThreshold, allergens, availabilityStatus)
VALUES 
('Vanilla', 'All Year', 10, 3, 'Milk', 'Available'),
('Chocolate', 'All Year', 8, 3, 'Milk', 'Available'),
('Cookie n Cream', 'All Year', 9, 3, 'Milk', 'Available'),
('Mint Chocolate Chip', 'All Year', 8, 3, 'Milk', 'Available'),
('Strawberry', 'All Year', 4, 3, 'Milk', 'Available'),
('Cotton Candy', 'All Year', 6, 3, 'Milk', 'Available'),
('Coffee', 'All Year', 9, 3, 'Milk', 'Available'),
('Salted Caramel', 'All Year', 9, 3, 'Milk', 'Available'),
('Pistachio', 'All Year', 9, 3, 'Milk', 'Available'),
('Coconut', 'All Year', 9, 3, 'N/A', 'Available'),
('Rocky Road', 'All Year', 9, 3, 'Milk', 'Available'),
('Butter Pecan', 'All Year', 9, 3, 'Milk', 'Available');

# Insert default toppings
INSERT INTO topping (toppingName)
VALUES 
('Sprinkles'),
('Oreos'),
('Caramel');