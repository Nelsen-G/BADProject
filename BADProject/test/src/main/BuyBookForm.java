package main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.window.Window;
import model.Book;
import model.Cart;
import model.Transaction;
import util.Connect;

public class BuyBookForm {
	Stage primaryStage;
	Scene buyBookScene;
	
	Window window;
	BorderPane bp;
	VBox vb, quantityVB, buttonVB, upperVB, leftlowerVB, rightlowerVB, lowerVB;
	HBox lowerHB;
	
	MenuBar menuBar;
	Menu userMenu, transactionMenu;
	MenuItem logOut, buyBook, transactionHistory;
	
	TableView<Book> bookTable;
	TableView<Cart> cartTable;
	
	Text bookTxt, cartTxt;
	Label quantityLbl;
	Spinner<Integer> quantity;
	Button addButton, updateButton, removeButton, checkoutButton;
	
	Book book;
	Alert alertMessage;
	
	private void initialize() {
		window = new Window();
		bp = new BorderPane();
		
		vb = new VBox();
		quantityVB = new VBox();
		buttonVB = new VBox();
		upperVB = new VBox();
		leftlowerVB = new VBox();
		rightlowerVB = new VBox();
		lowerVB = new VBox();
		
		lowerHB = new HBox();
		
		bookTable = new TableView<Book>();
		cartTable = new TableView<Cart>();
		
		menuBar = new MenuBar();
		
		userMenu = new Menu("User");
		transactionMenu = new Menu("Transaction");
		
		logOut = new MenuItem("Logout");
		buyBook = new MenuItem("Buy Book");
		transactionHistory = new MenuItem("View Transaction History");
		
		bookTxt = new Text("Book List");
		cartTxt = new Text("My Cart");
		
		quantityLbl = new Label("Quantity");
//		quantity = new Spinner<>(1, book.getBookStock(), 1);
		quantity = new Spinner<>(1, 100, 1);
		quantity.setPrefWidth(300);
		
		addButton = new Button("Add to Cart");
		addButton.setPrefWidth(300);
		
		updateButton = new Button("Update Cart");
		updateButton.setPrefWidth(300);
		
		removeButton = new Button("Remove from Cart");
		removeButton.setPrefWidth(300);
		
		checkoutButton = new Button("Checkout");
		checkoutButton.setPrefWidth(300);
		
		buyBookScene = new Scene(bp, 1000, 800);
	}
	
	private void layout() {
		menuBar.getMenus().addAll(userMenu, transactionMenu);
		
		userMenu.getItems().addAll(logOut);
		transactionMenu.getItems().addAll(buyBook, transactionHistory);
		
		quantityVB.getChildren().addAll(quantityLbl, quantity);
		quantityVB.setMargin(quantityLbl, new Insets(5, 0, 0, 15));
		quantityVB.setMargin(quantity, new Insets(0, 0, 20, 15));
		
		buttonVB.getChildren().addAll(addButton, updateButton, removeButton, checkoutButton);
		
		leftlowerVB.getChildren().addAll(cartTable);
		leftlowerVB.setPrefWidth(700);
		leftlowerVB.setPrefHeight(250);
		leftlowerVB.setMargin(cartTable, new Insets(0, 0, 0, 15));
		
		rightlowerVB.getChildren().addAll(quantityVB, buttonVB);
		rightlowerVB.setMargin(buttonVB, new Insets(20, 0, 0, 15));
		rightlowerVB.setPrefWidth(400);
		rightlowerVB.setAlignment(Pos.TOP_RIGHT);
		
		lowerHB.getChildren().addAll(leftlowerVB, rightlowerVB);
		lowerVB.getChildren().addAll(cartTxt, lowerHB);
		lowerVB.setPrefWidth(1000);
		lowerVB.setMargin(cartTxt, new Insets(10, 0, 10, 15));
		
		upperVB.getChildren().addAll(bookTxt, bookTable);
		upperVB.setMargin(bookTxt, new Insets(15, 0, 0, 15));
		upperVB.setMargin(bookTable, new Insets(0, 15, 0, 15));
		
		vb.getChildren().addAll(upperVB, lowerVB);
		
		window.getContentPane().getChildren().add(vb);
		window.setTitle("Buy Book");
		window.setPrefHeight(700);
		
		bp.setTop(menuBar);
		bp.setCenter(window);
	}
	
	private void setBookTable() {
		TableColumn<Book, Integer> bookID = new TableColumn<Book, Integer>("ID");
		TableColumn<Book, String> bookName = new TableColumn<Book, String>("Name");
		TableColumn<Book, String> bookAuthor = new TableColumn<Book, String>("Author");
		TableColumn<Book, String> bookGenre = new TableColumn<Book, String>("Genre");
		TableColumn<Book, Integer> bookStock = new TableColumn<Book, Integer>("Stock");
		TableColumn<Book, Integer> bookPrice = new TableColumn<Book, Integer>("Price");
		
		bookID.setCellValueFactory(new PropertyValueFactory<>("bookID"));
		bookName.setCellValueFactory(new PropertyValueFactory<>("bookName"));
		bookAuthor.setCellValueFactory(new PropertyValueFactory<>("bookAuthor"));
		bookGenre.setCellValueFactory(new PropertyValueFactory<>("bookGenre"));
		bookStock.setCellValueFactory(new PropertyValueFactory<>("bookStock"));
		bookPrice.setCellValueFactory(new PropertyValueFactory<>("bookPrice"));
		
		bookID.setMinWidth(1000 / 6.2);
		bookName.setMinWidth(1000 / 6.2);
		bookAuthor.setMinWidth(1000 / 6.2);
		bookGenre.setMinWidth(1000 / 6.2);
		bookStock.setMinWidth(1000 / 6.2);
		bookPrice.setMinWidth(1000 / 6.2);
		
		bookTable.getColumns().addAll(bookID, bookName, bookAuthor, bookGenre, bookStock, bookPrice);
		bookTable.setPrefHeight(365);
		
		Connect cn = new Connect();
		String q = "SELECT * FROM book b JOIN genre g ON b.GenreID = g.GenreID";
		ResultSet r = cn.executeSelect(q);
		try {
			while (r.next()) {
				int id = r.getInt("BookID");
				String nm = r.getString("BookName");
				String auth = r.getString("BookAuthor");
				String gen = r.getString("GenreName");
				int harga = r.getInt("BookPrice");
				int setok = r.getInt("BookStock");
				Book bk = new Book(id, nm, auth, gen, setok, harga);
				bookTable.getItems().add(bk);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void setCartTable() {
		TableColumn<Cart, Integer> cartBookID = new TableColumn<Cart, Integer>("Book ID");
		TableColumn<Cart, String> cartBookName = new TableColumn<Cart, String>("Name");
		TableColumn<Cart, String> cartBookAuthor = new TableColumn<Cart, String>("Author");
		TableColumn<Cart, Integer> cartBookPrice = new TableColumn<Cart, Integer>("Price");
		TableColumn<Cart, Integer> cartBookQuantity = new TableColumn<Cart, Integer>("Qty");
		
		cartBookID.setCellValueFactory(new PropertyValueFactory<>("cartBookID"));
		cartBookName.setCellValueFactory(new PropertyValueFactory<>("cartBookName"));
		cartBookAuthor.setCellValueFactory(new PropertyValueFactory<>("cartBookAuthor"));
		cartBookPrice.setCellValueFactory(new PropertyValueFactory<>("cartBookPrice"));
		cartBookQuantity.setCellValueFactory(new PropertyValueFactory<>("cartBookQuantity"));
		
		cartBookID.setMinWidth(700 / 5.5);
		cartBookName.setMinWidth(700 / 5.5);
		cartBookAuthor.setMinWidth(700 / 5.5);
		cartBookPrice.setMinWidth(700 / 5.5);
		cartBookQuantity.setMinWidth(700 / 5.5);
		
		cartTable.getColumns().addAll(cartBookID, cartBookName, cartBookAuthor, cartBookPrice, cartBookQuantity);
		
		Connect c =new Connect();
		String qu ="SELECT c.BookID,BookName,BookAuthor,BookPrice,CartQty from cart c JOIN book b ON c.BookID =b.BookID";
		ResultSet r = c.executeSelect(qu);
		try {
			while (r.next()) {
				int i =r.getInt("BookID");
				String n = r.getString("BookName");
				String a = r.getString("BookAuthor");
				int p = r.getInt("BookPrice");
				int qty = r.getInt("CartQty");
				Cart cr= new Cart(i, n,a,p,qty);
				cartTable.getItems().add(cr);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void eventHandler() {		
		
		logOut.setOnAction((event) -> {
			primaryStage.close();
			
			Stage newStage = new Stage();
			try {
				new Login().start(newStage);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		buyBook.setOnAction((event) -> {
			primaryStage.close();
			
			Stage newStage = new Stage();
			try {
				new BuyBookForm().start(newStage);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		transactionHistory.setOnAction((event) -> {
			primaryStage.close();
			
			Stage newStage = new Stage();
			try {
				new TransactionHistory().start(newStage);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		final Integer[] result = new Integer[2];
		result[0] = 0;
		bookTable.setOnMouseClicked((event) -> {
			result[0] = bookTable.getSelectionModel().getSelectedItem().getBookID();
			result[1] = bookTable.getSelectionModel().getSelectedItem().getBookStock();
		});
		
		final Integer[] ca = new Integer[2];
		ca[0] = 0;
		cartTable.setOnMouseClicked((event) -> {
			ca[0] = cartTable.getSelectionModel().getSelectedItem().getCartBookID();
			ca[1] = cartTable.getSelectionModel().getSelectedItem().getCartBookQuantity();
		});
		
		addButton.setOnAction((event) -> {
			
			if(result[0] == 0) {
				alertMessage = new Alert(AlertType.ERROR);
				alertMessage.setTitle("Error");
				alertMessage.setHeaderText("Please select the book first!");
				alertMessage.show();
			} else {
				if (quantity.getValue() > result[1]) {
					alertMessage = new Alert(AlertType.ERROR);
					alertMessage.setTitle("Error");
					alertMessage.setHeaderText("Quantity must not be higher than stock!");
					alertMessage.show();
				}
				else {
					Connect c = new Connect();
					String q="INSERT INTO cart VALUES("+Login.lid+","+result[0]+","+quantity.getValue()+")";
					try {
						c.execute(q);
					} catch (Exception e) {
						// TODO: handle exception
					}
					cartTable.getItems().clear();
					String qu ="SELECT c.BookID,BookName,BookAuthor,BookPrice,CartQty from cart c JOIN book b ON c.BookID =b.BookID";
					ResultSet r = c.executeSelect(qu);
					try {
						while (r.next()) {
							int i =r.getInt("BookID");
							String n = r.getString("BookName");
							String a = r.getString("BookAuthor");
							int p = r.getInt("BookPrice");
							int qty = r.getInt("CartQty");
							Cart cr= new Cart(i, n,a,p,qty);
							cartTable.getItems().add(cr);
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				
			}
			
		});
		
		updateButton.setOnAction((event) -> {
			
			if(ca[0] == 0) {
				alertMessage = new Alert(AlertType.ERROR);
				alertMessage.setTitle("Error");
				alertMessage.setHeaderText("Please select the book first!");
				alertMessage.show();
			}
			else {
				Connect co = new Connect();
				String query= "SELECT * from book where BookID= "+ca[0];
				ResultSet re = co.executeSelect(query);
				int nn =0;
				try {
					while(re.next()) {
						nn =re.getInt("BookStock");
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if (quantity.getValue() > nn) {
					alertMessage = new Alert(AlertType.ERROR);
					alertMessage.setTitle("Error");
					alertMessage.setHeaderText("Quantity must not be higher than stock!");
					alertMessage.show();
				}
				else {
					Connect c = new Connect();
					String q = "UPDATE cart set CartQty=" + quantity.getValue() + " Where UserID=" + Login.lid +" and BookID="+ca[0];
					try {
						c.execute(q);
					} catch (Exception e) {
						// TODO: handle exception
					}
					ca[0] = 0;
					cartTable.getItems().clear();
					String qu ="SELECT c.BookID,BookName,BookAuthor,BookPrice,CartQty from cart c JOIN book b ON c.BookID =b.BookID";
					ResultSet r = c.executeSelect(qu);
					try {
						while (r.next()) {
							int i =r.getInt("BookID");
							String n = r.getString("BookName");
							String a = r.getString("BookAuthor");
							int p = r.getInt("BookPrice");
							int qty = r.getInt("CartQty");
							Cart cr= new Cart(i, n,a,p,qty);
							cartTable.getItems().add(cr);
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		});
		
		removeButton.setOnAction((event) -> {
			
			if(ca[0] == 0) {
				alertMessage = new Alert(AlertType.ERROR);
				alertMessage.setTitle("Error");
				alertMessage.setHeaderText("Please select the book first!");
				alertMessage.show();
			}
			else {
				Connect c = new Connect();
				String q = "DELETE from cart WHERE BookID = " + ca[0] + " and UserID=" + Login.lid;
				try {
					c.execute(q);
				} catch (Exception e) {
					// TODO: handle exception
				}
				ca[0] = 0;
				cartTable.getItems().clear();
				String qu ="SELECT c.BookID,BookName,BookAuthor,BookPrice,CartQty from cart c JOIN book b ON c.BookID =b.BookID";
				ResultSet r = c.executeSelect(qu);
				try {
					while (r.next()) {
						int i =r.getInt("BookID");
						String n = r.getString("BookName");
						String a = r.getString("BookAuthor");
						int p = r.getInt("BookPrice");
						int qty = r.getInt("CartQty");
						Cart cr= new Cart(i, n,a,p,qty);
						cartTable.getItems().add(cr);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		checkoutButton.setOnAction((event) -> {
			if(cartTable.getItems().size() == 0) {
				alertMessage = new Alert(AlertType.ERROR);
				alertMessage.setTitle("Error");
				alertMessage.setHeaderText("Cart is empty!");
				alertMessage.show();
			} else {
				alertMessage = new Alert(AlertType.INFORMATION);
				alertMessage.setTitle("Notification");
				alertMessage.setHeaderText("Checkout success!");
				alertMessage.show();
				
				Connect c = new Connect();
				String qu0 ="INSERT into headertransaction values(null,"+Login.lid+",\""+LocalDate.now()+"\")";
				c.execute(qu0);
				
				String qu1="SELECT * from headertransaction";
				ResultSet ht= c.executeSelect(qu1);
				int t=0;
				try {
					while(ht.next()) {
						t=ht.getInt("TransactionID");
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				for(int i=0;i<cartTable.getItems().size();i++) {
					String q2="INSERT into detailtransaction values("+t+","+cartTable.getItems().get(i).getCartBookID()+","+cartTable.getItems().get(i).getCartBookQuantity()+")";
					c.execute(q2);
				}
				
				String q3 = "DELETE from cart where UserID="+Login.lid;
				c.execute(q3);
				
				cartTable.getItems().clear();
				String qu ="SELECT c.BookID,BookName,BookAuthor,BookPrice,CartQty from cart c JOIN book b ON c.BookID =b.BookID";
				ResultSet r = c.executeSelect(qu);
				try {
					while (r.next()) {
						int i =r.getInt("BookID");
						String n = r.getString("BookName");
						String a = r.getString("BookAuthor");
						int p = r.getInt("BookPrice");
						int qty = r.getInt("CartQty");
						Cart cr= new Cart(i, n,a,p,qty);
						cartTable.getItems().add(cr);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}			
		});
	}

	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		this.primaryStage = primaryStage;
		
		initialize();
		layout();
		setBookTable();
		setCartTable();
		eventHandler();
		
		primaryStage.setResizable(false);
		primaryStage.getIcons().add(new Image("file:src/assets/book.png"));
		primaryStage.setTitle("Bookstore");
		primaryStage.setScene(buyBookScene);
		primaryStage.show();
	}

}
