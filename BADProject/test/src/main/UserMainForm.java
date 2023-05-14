package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.Book;
import model.Cart;

public class UserMainForm {
	Stage primaryStage;
	Scene mainFormScene;
	
	BorderPane bp;
	VBox vb;
	
	MenuBar menuBar;
	Menu userMenu, transactionMenu;
	MenuItem logOut, buyBook, transactionHistory;
	
	Image bgImage;
	ImageView bgImageView;
	
	Stage primaryStageBuyBook;
	Scene buyBookScene;
	
	private void initialize() {
		bp = new BorderPane();
		
		menuBar = new MenuBar();
		
		userMenu = new Menu("User");
		transactionMenu = new Menu("Transaction");
		
		logOut = new MenuItem("Logout");
		buyBook = new MenuItem("Buy Book");
		transactionHistory = new MenuItem("View Transaction History");
		
		FileInputStream input = null;
		try {
			input = new FileInputStream("src/assets/flat-world-book-day-illustration.jpg/5075656.jpg");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		bgImage = new Image(input);
		bgImageView = new ImageView(bgImage);
		bgImageView.setFitWidth(1000);
		bgImageView.setFitHeight(800);
		bgImageView.setPreserveRatio(true);
		
		mainFormScene = new Scene(bp);
	}
	
	private void layout() {
		menuBar.getMenus().addAll(userMenu, transactionMenu);
		
		userMenu.getItems().addAll(logOut);
		transactionMenu.getItems().addAll(buyBook, transactionHistory);
		
		bp.setTop(menuBar);
		bp.setCenter(bgImageView);
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
	}
	
	public Scene getMainFormScene() {
		return mainFormScene;
	}

	public void setMainFormScene(Scene mainFormScene) {
		this.mainFormScene = mainFormScene;
	}

	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		this.primaryStage = primaryStage;
		
		initialize();
		layout();
		eventHandler();
		primaryStage.setResizable(false);
		primaryStage.setTitle("Bookstore");
		primaryStage.getIcons().add(new Image("file:src/assets/book.png"));
		primaryStage.setScene(mainFormScene);
		primaryStage.show();
	}

}
