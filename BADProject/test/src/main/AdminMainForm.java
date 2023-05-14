package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;

public class AdminMainForm {
	Stage primaryStage;
	Scene mainFormScene;
	
	BorderPane bp;
	VBox vb;
	
	MenuBar menuBar;
	Menu userMenu, manageMenu;
	MenuItem logOut, book, genre;
	
	Image bgImage;
	ImageView bgImageView;
	
	private void initialize() {
		bp = new BorderPane();
		vb = new VBox();
		
		menuBar = new MenuBar();
		
		userMenu = new Menu("User");
		manageMenu = new Menu("Manage");
		
		logOut = new MenuItem("Logout");
		book = new MenuItem("Book");
		genre = new MenuItem("Genre");
		
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
		menuBar.getMenus().addAll(userMenu, manageMenu);
		
		userMenu.getItems().addAll(logOut);
		manageMenu.getItems().addAll(book, genre);
		
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
		
		book.setOnAction((event) -> {
			primaryStage.close();
			
			Stage newStage = new Stage();
			try {
				new ManageBookForm().start(newStage);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		genre.setOnAction((event) -> {
			primaryStage.close();
			
			Stage newStage = new Stage();
			try {
				new ManageGenreForm().start(newStage);
			} catch (Exception e) {
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
		eventHandler();
		primaryStage.setResizable(false);
		primaryStage.getIcons().add(new Image("file:src/assets/book.png "));
		primaryStage.setTitle("Bookstore");
		primaryStage.setScene(mainFormScene);
		primaryStage.show();
	}

}
