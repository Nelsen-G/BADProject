package main;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.window.Window;
import model.Book;
import model.Genre;
import util.Connect;

public class ManageGenreForm {
	Stage primaryStage;
	Scene manageGenreFormScene;
	
	Window window;
	BorderPane bp;
	GridPane windowGP, leftGP;
	
	MenuBar menuBar;
	Menu userMenu, manageMenu;
	MenuItem logOut, bookMenu, genreMenu;
	
	Label nameLbl;
	TextField nameField;
	Button insertButton;
	
	TableView<Genre> genreTable;
	
	Genre genre;
	Alert alertMessage;
	
	public void initialize() {
		bp = new BorderPane();
		windowGP = new GridPane();
		leftGP = new GridPane();
		
		window = new Window();
		
		menuBar = new MenuBar();
		
		userMenu = new Menu("User");
		manageMenu = new Menu("Manage");
		
		logOut = new MenuItem("Logout");
		bookMenu = new MenuItem("Book");
		genreMenu = new MenuItem("Genre");
		
		nameLbl = new Label("Name");
		nameField = new TextField();
		
		insertButton = new Button("Insert");
		insertButton.setPrefWidth(400);
		
		genreTable = new TableView<Genre>();
		
		manageGenreFormScene = new Scene(bp, 1200, 800);
	}
	
	public void layout() {
		menuBar.getMenus().addAll(userMenu, manageMenu);
		
		userMenu.getItems().addAll(logOut);
		manageMenu.getItems().addAll(bookMenu, genreMenu);
		
		leftGP.add(nameLbl, 0, 0);
		leftGP.add(nameField, 0, 1);
		leftGP.add(insertButton, 0, 2);
		
		leftGP.setPrefWidth(300);
		leftGP.setAlignment(Pos.CENTER);
		leftGP.setVgap(0);
		
		windowGP.add(leftGP, 0, 0);
		windowGP.add(genreTable, 1, 0);
		windowGP.setMargin(leftGP, new Insets(70, 30, 30, 30));
		windowGP.setMargin(genreTable, new Insets(0));
		
		window.getContentPane().getChildren().add(windowGP);
		window.setTitle("Manage Genre");
		
		bp.setTop(menuBar);
		bp.setCenter(window);
	}
	
	public void setBookTable() {
		TableColumn<Genre, Integer> genreID = new TableColumn<Genre, Integer>("ID");
		TableColumn<Genre, String> genreName = new TableColumn<Genre, String>("Name");
		
		genreID.setCellValueFactory(new PropertyValueFactory<>("genreID"));
		genreName.setCellValueFactory(new PropertyValueFactory<>("genreName"));
		
		genreID.setMinWidth(850 / 2);
		genreName.setMinWidth(850 / 2);
		
		genreTable.getColumns().addAll(genreID, genreName);
		genreTable.setPrefHeight(800);
		
		Connect cn = new Connect();
		String q = "SELECT * FROM genre";
		ResultSet r = cn.executeSelect(q);
		try {
			while (r.next()) {
				int id = r.getInt("GenreID");
				String gen = r.getString("GenreName");
				Genre g = new Genre(id, gen);
				genreTable.getItems().add(g);
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
		
		bookMenu.setOnAction((event) -> {
			primaryStage.close();
			
			Stage newStage = new Stage();
			try {
				new ManageBookForm().start(newStage);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		genreMenu.setOnAction((event) -> {
			primaryStage.close();
			
			Stage newStage = new Stage();
			try {
				new ManageGenreForm().start(newStage);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		insertButton.setOnAction((event) -> {
			String name = nameField.getText();
			
			if(name.length() < 5 || name.length() > 12) {
				alertMessage = new Alert(AlertType.ERROR);
				alertMessage.setTitle("Error");
				alertMessage.setHeaderText("Genre name must between 5 - 12");
				alertMessage.show();
			
			} else {
				Connect cn = new Connect();
				String q="INSERT INTO genre VALUES(null, \""+name+"\")";
				cn.execute(q);
				alertMessage = new Alert(AlertType.INFORMATION);
				alertMessage.setTitle("Notification");
				alertMessage.setHeaderText("Genre " + name + " added!");
				alertMessage.show();
				genreTable.getItems().clear();
				String que = "SELECT * FROM genre";
				ResultSet r = cn.executeSelect(que);
				try {
					while (r.next()) {
						int id = r.getInt("GenreID");
						String gen = r.getString("GenreName");
						Genre g = new Genre(id, gen);
						genreTable.getItems().add(g);
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
		eventHandler();
		primaryStage.setResizable(false);
		primaryStage.getIcons().add(new Image("file:src/assets/book.png "));
		primaryStage.setTitle("Bookstore");
		primaryStage.setScene(manageGenreFormScene);
		primaryStage.show();
	}

}
