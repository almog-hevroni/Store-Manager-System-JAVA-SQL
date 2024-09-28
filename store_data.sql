CREATE TABLE Product
(product_id VARCHAR(50) NOT NULL,
	product_name VARCHAR(50) NOT NULL,
	selling_price DOUBLE PRECISION NOT NULL,
	cost_price DOUBLE PRECISION NOT NULL,
	stock_quantity INT NOT NULL,
	product_type VARCHAR(50) NOT NULL,
	PRIMARY KEY (product_id));

CREATE TABLE Web_Product (
    product_id VARCHAR(50) NOT NULL,
    weight DOUBLE PRECISION,
	PRIMARY KEY (product_id),
    FOREIGN KEY (product_id) REFERENCES Product (product_id));

CREATE TABLE Orders
(order_id VARCHAR(50) NOT NULL,
	product_id VARCHAR(50) NOT NULL,
	quantity INT NOT NULL,
	order_type VARCHAR(50) NOT NULL,
	PRIMARY KEY (order_id),
	FOREIGN KEY (product_id) REFERENCES Product(product_id));


CREATE TABLE Customer
(order_id VARCHAR(50) NOT NULL,
	name VARCHAR(50) NOT NULL,
	phone_number VARCHAR(50) NOT NULL,
	PRIMARY KEY (order_id),
	FOREIGN KEY (order_id) REFERENCES orders(order_id));

CREATE TABLE Web_Order
(order_id VARCHAR(50),
	delivery_type VARCHAR(50),
	PRIMARY KEY (order_id),
	FOREIGN KEY (order_id) REFERENCES Orders(order_id));

CREATE TABLE Adress
(order_id VARCHAR(50),
	street VARCHAR(50),
	city VARCHAR(50),
	country VARCHAR(50),
	house_number INT NOT NULL,
	PRIMARY KEY (order_id),
	FOREIGN KEY (order_id) REFERENCES Web_Order(order_id));

--ADD PRODUCTS
INSERT INTO product(product_id,product_name,selling_price,cost_price,stock_quantity,product_type)
VALUES ('ST-123','Iphone 13 pro',4500,3000,10,'STORE'),
		('ST-234','LG Smart TV',3000,2000,25,'STORE'),
		('ST-567','Airpods Pro',4000,2500,15,'STORE'),
		('SW-123','Sony Wireless Noise-Canceling Headphones',1225,800,200,'WHOLESALERS'),
		('SW-234','Drunk Elephant Organic Retinol Serun',140,80,500,'WHOLESALERS'),
		('SW-453','Fitbit Charge 5 Fitness Tracker',455,350,150,'WHOLESALERS'),
		('WB-087','Starbucks Reserve Gourmet Coffee Beans',140,100,70,'WEB'),
		('WB-752','Corsair Platinum Mechanical Gaming Keyboard',530,250,20,'WEB'),
		('WB-183','Philips Remote Color Ambiance Smart LED Bulb',105,80,17,'WEB');

--ADD INFORMATION FOR WEB PRODUCTS
INSERT INTO web_product(product_id,weight)
VALUES ('WB-087',2),
		('WB-752',4),
		('WB-183',5);

--ADD ORDERS
INSERT INTO orders(order_id,product_id,quantity,order_type)
VALUES ('ORD_ST_123','ST-123',1,'STORE'),
		('ORD_ST_854','ST-123',2,'STORE'),
		('ORD_ST_872','ST-123',1,'STORE'),
		('ORD_ST_235','ST-234',1,'STORE'),
		('ORD_ST_764','ST-234',3,'STORE'),
		('ORD_ST_456','ST-234',1,'STORE'),
		('ORD_ST_987','ST-567',1,'STORE'),
		('ORD_ST_654','ST-567',2,'STORE'),
		('ORD_ST_321','ST-567',5,'STORE'),
		('ORD_SW_763','SW-123',30,'WHOLESALERS'),
		('ORD_SW_864','SW-123',20,'WHOLESALERS'),
		('ORD_SW_965','SW-123',25,'WHOLESALERS'),
		('ORD_SW_173','SW-234',100,'WHOLESALERS'),
		('ORD_SW_274','SW-234',150,'WHOLESALERS'),
		('ORD_SW_375','SW-234',120,'WHOLESALERS'),
		('ORD_SW_476','SW-453',50,'WHOLESALERS'),
		('ORD_SW_577','SW-453',75,'WHOLESALERS'),
		('ORD_SW_678','SW-453',12,'WHOLESALERS'),
		('ORD_WB_502','WB-087',5,'WEB'),
		('ORD_WB_503','WB-087',10,'WEB'),
		('ORD_WB_504','WB-087',8,'WEB'),
		('ORD_WB_701','WB-752',3,'WEB'),
		('ORD_WB_702','WB-752',2,'WEB'),
		('ORD_WB_703','WB-752',5,'WEB'),
		('ORD_WB_801','WB-183',2,'WEB'),
		('ORD_WB_802','WB-183',7,'WEB'),
		('ORD_WB_803','WB-183',5,'WEB');

--ADD CUSTOMERS
INSERT INTO customer(order_id,name,phone_number)
VALUES ('ORD_ST_123','Lior Kapshitar','0526325213'),
		('ORD_ST_854','Almog Hevroni','0538667915'),
		('ORD_ST_872','David Cohen','0548726540'),
		('ORD_ST_235','Eli Sheva','0543216789'),
		('ORD_ST_764','Yael Abramov','0527648391'),
		('ORD_ST_456','Ori Shavit','0527364859'),
		('ORD_ST_987','Moran Atias','0532145879'),
		('ORD_ST_654','Tomer Russo','0526987412'),
		('ORD_ST_321','Neta Lee','0543218624'),
		('ORD_SW_763','Noa Tubul','0568369103'),
		('ORD_SW_864','Yaron Malichi','0548326710'),
		('ORD_SW_965','Tal Revivo','0537216942'),
		('ORD_SW_173','Shira Eini','0526453987'),
		('ORD_SW_274','Itai Levi','0527639482'),
		('ORD_SW_375','Liat Nadav','0548216973'),
		('ORD_SW_476','Maor Cohen','0548362716'),
		('ORD_SW_577','Aviv Alush','0539146827'),
		('ORD_SW_678','Rita Malki','0528746391'),
		('ORD_WB_502','Roberto Raven','0528631063'),
		('ORD_WB_503','Miriam Levi','0548321760'),
		('ORD_WB_504','Isaac Cohen','0539271642'),
		('ORD_WB_701','Dan Abramov','0528637490'),
		('ORD_WB_702','Noa Kirel','0548329112'),
		('ORD_WB_703','Eyal Golan','0539271583'),
		('ORD_WB_801','Sara Levi','0528643176'),
		('ORD_WB_802','Omer Adam','0548336710'),
		('ORD_WB_803','Gal Gadot','0539274752');

--HANDLE WEB ORDERS
INSERT INTO web_order(order_id,delivery_type)
VALUES ('ORD_WB_502','EXPRESS_SHIPPING'),
		('ORD_WB_503','STANDARD_SHIPPING'),
		('ORD_WB_504','EXPRESS_SHIPPING'),
		('ORD_WB_701','STANDARD_SHIPPING'),
		('ORD_WB_702','EXPRESS_SHIPPING'),
		('ORD_WB_703','STANDARD_SHIPPING'),
		('ORD_WB_801','EXPRESS_SHIPPING'),
		('ORD_WB_802','STANDARD_SHIPPING'),
		('ORD_WB_803','EXPRESS_SHIPPING');


--ADD ADDRESSES
INSERT INTO adress(order_id,country,city,street,house_number)
VALUES ('ORD_WB_502','Israel','Holon','Hagalil',4),
		('ORD_WB_503','Israel','Tel Aviv','Dizengoff',122),
		('ORD_WB_504','Israel','Jerusalem','King George',19),
		('ORD_WB_701','Israel','Herzliya','Ben Gurion',8),
		('ORD_WB_702','Israel','Haifa','Horev',33),
		('ORD_WB_703','Israel','Rishon LeZion','Rothschild',77),
		('ORD_WB_801','Israel','Beer Sheva','Yehuda',45),
		('ORD_WB_802','Israel','Netanya','Sokolov',12),
		('ORD_WB_803','Israel','Ashdod','HaAtzmaut', 56);