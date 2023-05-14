package main;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.window.Window;
import model.Transaction;
import util.Connect;
import model.Cart;

public class TransactionHistory {
	Stage primaryStage;
	Scene transactionHistoryScene;
	
	BorderPane bp;
	GridPane gp;
	
	MenuBar menuBar;
	Menu userMenu, transactionMenu;
	MenuItem logOut, buyBook, transactionHistory;
	
	Window window;
	TableView<Transaction> transactionTable;
	TableView<Cart> detailTransactionTable;
	
	private void initialize() {
		window = new Window();
		bp = new BorderPane();
		gp = new GridPane();
		
		menuBar = new MenuBar();
		
		userMenu = new Menu("User");
		transactionMenu = new Menu("Transaction");
		
		logOut = new MenuItem("Logout");
		buyBook = new MenuItem("Buy Book");
		transactionHistory = new MenuItem("View Transaction History");
		
		transactionTable = new TableView<Transaction>();
		detailTransactionTable = new TableView<Cart>();
		
		transactionHistoryScene = new Scene(bp, 1000, 800);
	}
	
	private void layout() {		
		menuBar.getMenus().addAll(userMenu, transactionMenu);
		
		userMenu.getItems().addAll(logOut);
		transactionMenu.getItems().addAll(buyBook, transactionHistory);
		
		bp.setTop(menuBar);
		
		gp.add(transactionTable, 0, 0);
		gp.add(detailTransactionTable, 1, 0);
		gp.setMargin(transactionTable, new Insets(30, 0, 30, 30));
		gp.setMargin(detailTransactionTable, new Insets(30, 0, 30, 0));
		
		window.getContentPane().getChildren().add(gp);
		window.setTitle("Transaction History");
		
		bp.setTop(menuBar);
		bp.setCenter(window);
	}
	
	private void setTransactionTable() {
		TableColumn<Transaction, Integer> transactionID = new TableColumn<Transaction, Integer>("ID");
		TableColumn<Transaction, String> transactionDate = new TableColumn<Transaction, String>("Date");
		
		transactionID.setCellValueFactory(new PropertyValueFactory<>("transactionID"));
		transactionDate.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
		
		transactionID.setPrefWidth(300 / 2);
		transactionDate.setPrefWidth(300 / 2);
		
		transactionTable.getColumns().addAll(transactionID, transactionDate);
		transactionTable.setPrefHeight(700);
		
		Connect c=new Connect();
		String q ="SELECT * from headertransaction";
		ResultSet r = c.executeSelect(q);
		try {
			while(r.next()) {
				int tid= r.getInt("TransactionID");
				String td=r.getString("TransactionDate");
				Transaction t= new Transaction(tid,td);
				transactionTable.getItems().add(t);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void setDetailTransactionTable() {
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
		
		cartBookID.setMinWidth(700 / 5.55);
		cartBookName.setMinWidth(700 / 5.55);
		cartBookAuthor.setMinWidth(700 / 5.55);
		cartBookPrice.setMinWidth(700 / 5.55);
		cartBookQuantity.setMinWidth(700 / 5.55);
		
		detailTransactionTable.getColumns().addAll(cartBookID, cartBookName, cartBookAuthor, cartBookPrice, cartBookQuantity);
		detailTransactionTable.setPrefHeight(700);
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
		
		transactionTable.setOnMouseClicked((event) -> {
			int dt;
			dt=transactionTable.getSelectionModel().getSelectedItem().getTransactionID();
			detailTransactionTable.getItems().clear();
			Connect cn = new Connect();
			String q="SELECT * from detailtransaction d join book b on d.BookID=b.BookID where TransactionID="+dt;
			ResultSet r=cn.executeSelect(q);
			try {
				while(r.next()) {
					int i=r.getInt("BookID");
					String nm =r.getString("BookName");
					String a =r.getString("BookAuthor");
					int p=r.getInt("BookPrice");
					int qt=r.getInt("TransactionQty");
					Cart c=new Cart(i,nm,a,p,qt);
					detailTransactionTable.getItems().add(c);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}
	
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		this.primaryStage = primaryStage;
		
		initialize();
		layout();
		setTransactionTable();
		setDetailTransactionTable();
		eventHandler();
		primaryStage.setResizable(false);
		primaryStage.getIcons().add(new Image("file:src/assets/book.png "));
		primaryStage.setTitle("Bookstore");
		primaryStage.setScene(transactionHistoryScene);
		primaryStage.show();
	}
}
