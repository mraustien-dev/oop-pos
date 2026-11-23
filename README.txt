JavaPOS (Simple Version) - Basic OOP Point of Sale
===================================================

HOW TO RUN
  Double-click run.bat
  (or:  javac -d out src\*.java   then   java -cp out Main)

LOGIN
  Admin    admin / admin123       (Sell, Products, Users, History)
  Cashier  cashier / cashier123   (Sell, History)

DATA (saved in the data\ folder, created automatically)
  users.csv         username,password,fullName,role
  products.csv      id,name,price,stock
  transactions.csv  id,dateTime,cashier,total,cash,items

FILES
  Main.java          starts the program
  User.java          abstract parent class of Admin and Cashier
  Admin.java         a kind of User
  Cashier.java       a kind of User
  Product.java       one product (checks its own values)
  CartItem.java      one product + quantity in the cart
  Cart.java          the current customer's cart
  Transaction.java   one finished sale / receipt
  Store.java         the "brain": lists, login, products, checkout, save/load
  FileManager.java   reads and writes the text files
  LoginWindow.java   login screen
  MainWindow.java    main screen with tabs
  SellPanel.java     Sell tab
  ProductPanel.java  Products tab (admin only)
  UserPanel.java     Users tab (admin only): see, add, edit and delete users
  HistoryPanel.java  History tab
  Style.java         colors, fonts and styled buttons/tables used by every screen

THE 4 PILLARS OF OOP
  Encapsulation  Product.java - private fields; values are checked in
                 update(), addStock() and removeStock().
  Inheritance    Admin and Cashier extend User.
                 The windows extend JFrame / JPanel.
  Polymorphism   Overriding: Admin and Cashier each override getRole()
                 and canManageProducts().
                 Overloading: Cart.addItem(product) and
                 Cart.addItem(product, quantity).
  Abstraction    User is an abstract class with abstract methods.
