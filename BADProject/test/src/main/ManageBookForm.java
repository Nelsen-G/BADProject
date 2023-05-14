package main;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
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
import util.Connect;

public class ManageBookForm {
	Stage primaryStage;
	Scene manageBookFormScene;
	
	Window window;
	BorderPane bp;
	GridPane windowGP, leftGP, buttonGP;
	
	MenuBar menuBar;
	Menu userMenu, manageMenu;
	MenuItem logOut, bookMenu, genreMenu;
	
	Label idLbl, nameLbl, authorLbl, genreLbl, priceLbl, stockLbl;
	TextField idField, nameField, authorField, priceField;
	ComboBox<String> genreCB;
	Spinner<Integer> stockSpinner;
	Button insertButton, updateButton, deleteButton;
	
	TableView<Book> bookTable;
	
	Book book;
	Alert alertMessage;
	
	public void initialize() {
		bp = new BorderPane();
		windowGP = new GridPane();
		buttonGP = new GridPane();
		leftGP = new GridPane();
		
		window = new Window();
		
		menuBar = new MenuBar();
		
		userMenu = new Menu("User");
		manageMenu = new Menu("Manage");
		
		logOut = new MenuItem("Logout");
		bookMenu = new MenuItem("Book");
		genreMenu = new MenuItem("Genre");
		
		idLbl = new Label("ID");
		idField = new TextField();
//		idField.setPromptText(value);
		idField.setEditable(false);
		
		nameLbl = new Label("Name");
		nameField = new TextField();
		
		authorLbl = new Label("Author");
		authorField = new TextField();
		
		genreLbl = new Label("Genre");
		genreCB = new ComboBox<>();
		genreCB.setItems(FXCollections.observableArrayList("Fantasy", "Sci-Fi", "Mystery", "Thriller", "Romance", "Programming", "Drama"));
		genreCB.setPrefWidth(300);
		
		priceLbl = new Label("Price");
		priceField = new TextField();
		
		stockLbl = new Label("Stock");
//		stockSpinner = new Spinner<>(1, book.getBookStock(), 1);
		stockSpinner = new Spinner<>(0, 100, 1);
		stockSpinner.setPrefWidth(300);
		
		insertButton = new Button("Insert");
		insertButton.setPrefWidth(300);
		
		updateButton = new Button("Update");
		updateButton.setPrefWidth(300);
		
		deleteButton = new Button("Delete");
		deleteButton.setPrefWidth(300);
		
		bookTable = new TableView<Book>();
		
		manageBookFormScene = new Scene(bp, 1200, 800);
	}
	
	public void layout() {
		menuBar.getMenus().addAll(userMenu, manageMenu);
		
		userMenu.getItems().addAll(logOut);
		manageMenu.getItems().addAll(bookMenu, genreMenu);
		
		buttonGP.add(insertButton, 0, 0);
		buttonGP.add(updateButton, 0, 1);
		buttonGP.add(deleteButton, 0, 2);
		
		leftGP.add(idLbl, 0, 0);
		leftGP.add(idField, 0, 1);
		
		leftGP.add(nameLbl, 0, 2);
		leftGP.add(nameField, 0, 3);
		
		leftGP.add(authorLbl, 0, 4);
		leftGP.add(authorField, 0, 5);
		
		leftGP.add(genreLbl, 0, 6);
		leftGP.add(genreCB, 0, 7);
		
		leftGP.add(priceLbl, 0, 8);
		leftGP.add(priceField, 0, 9);
		
		leftGP.add(stockLbl, 0, 10);
		leftGP.add(stockSpinner, 0, 11);
		
		leftGP.add(buttonGP, 0, 12);
		
		leftGP.setPrefWidth(300);
		leftGP.setVgap(5);
		leftGP.setMargin(buttonGP, new Insets(20, 0, 0, 0));
		
		windowGP.add(leftGP, 0, 0);
		windowGP.add(bookTable, 1, 0);
		windowGP.setMargin(leftGP, new Insets(70, 30, 30, 30));
		
		window.getContentPane().getChildren().add(windowGP);
		window.setTitle("Manage Book");
		
		bp.setTop(menuBar);
		bp.setCenter(window);
	}
	
	public void setBookTable() {
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
		
		bookID.setMinWidth(900 / 6.25);
		bookName.setMinWidth(900 / 6.25);
		bookAuthor.setMinWidth(900 / 6.25);
		bookGenre.setMinWidth(900 / 6.25);
		bookStock.setMinWidth(900 / 6.25);
		bookPrice.setMinWidth(900 / 6.25);
		
		bookTable.getColumns().addAll(bookID, bookName, bookAuthor, bookGenre, bookStock, bookPrice);
		bookTable.setPrefHeight(800);
		
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
	
	public boolean checkPrice(String price) {
		boolean priceNumerical = false;
		
		for(int i=0; i<price.length();i++) {
			if(price.charAt(i) >= '0' && price.charAt(i) <= '9') {
				priceNumerical = true;
			}
		}
		
		if(priceNumerical == false) {
			return true;
			
		} else {
			return false;
			
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
			//ini success input buku :D
			String id = idField.getText();
			String name = nameField.getText();
			String author = authorField.getText();
			boolean selectedCB = genreCB.getSelectionModel().isEmpty();
			String price = priceField.getText();
			Integer stock = stockSpinner.getValue();
			
			if(name.length() < 5 || name.length() > 45) {
				alertMessage = new Alert(AlertType.ERROR);
				alertMessage.setTitle("Error");
				alertMessage.setHeaderText("Book name must between 5 - 45!");
				alertMessage.show();
				
			} else if(author.length() < 5 || author.length() > 20) {
				alertMessage = new Alert(AlertType.ERROR);
				alertMessage.setTitle("Error");
				alertMessage.setHeaderText("Author name must between 5 - 20!");
				alertMessage.show();
			
			} else if(selectedCB == true) {
				alertMessage = new Alert(AlertType.ERROR);
				alertMessage.setTitle("Error");
				alertMessage.setHeaderText("Genre must be selected!");
				alertMessage.show();
				
			} else if(checkPrice(price)) {
				alertMessage = new Alert(AlertType.ERROR);
				alertMessage.setTitle("Error");
				alertMessage.setHeaderText("Price must be numerical!");
				alertMessage.show();
				
			} else if(stock <= 0) {
				alertMessage = new Alert(AlertType.ERROR);
				alertMessage.setTitle("Error");
				alertMessage.setHeaderText("Stock must be more than 0!");
				alertMessage.show();
				
			} else {
				alertMessage = new Alert(AlertType.INFORMATION);
				alertMessage.setTitle("Notification");
				alertMessage.setHeaderText("Book " + name + " added!");
				alertMessage.show();
				
				String cb = genreCB.getValue();
				Connect c = new Connect();
				String q="SELECT GenreID from genre where GenreName = \"" + cb + "\"";
				ResultSet r=c.executeSelect(q);
				int gid = 0;
				try {
					while (r.next()) {
						gid = r.getInt("GenreID");
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				String que = "INSERT INTO book values(null," + gid + ",\"" +name+"\",\""+author+"\","+Integer.parseInt(price)+","+stock+")";
				c.execute(que);

				nameField.setText("");
				authorField.setText("");
				genreCB.valueProperty().set(null);
				priceField.setText("");
				stockSpinner.getValueFactory().setValue(1);
				
				bookTable.getItems().clear();
				String qu = "SELECT * FROM book b JOIN genre g ON b.GenreID = g.GenreID";
				ResultSet re = c.executeSelect(qu);
				try {
					while (re.next()) {
						int ids = re.getInt("BookID");
						String nm = re.getString("BookName");
						String auth = re.getString("BookAuthor");
						String gen = re.getString("GenreName");
						int harga = re.getInt("BookPrice");
						int setok = re.getInt("BookStock");
						Book bk = new Book(ids, nm, auth, gen, setok, harga);
						bookTable.getItems().add(bk);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		bookTable.setOnMouseClicked((event) -> {
			String i;
			try {
				i = bookTable.getSelectionModel().getSelectedItem().getBookID().toString();
			} catch (Exception e) {
				// TODO: handle exception
				i ="";
			}
			idField.setText(i);
			nameField.setText(bookTable.getSelectionModel().getSelectedItem().getBookName());
			authorField.setText(bookTable.getSelectionModel().getSelectedItem().getBookAuthor());
			genreCB.valueProperty().set(bookTable.getSelectionModel().getSelectedItem().getBookGenre());
			String pr = bookTable.getSelectionModel().getSelectedItem().getBookPrice().toString();
			priceField.setText(pr);
			stockSpinner.getValueFactory().setValue(bookTable.getSelectionModel().getSelectedItem().getBookStock());
		});
		
		updateButton.setOnAction((event) -> {
			String id = idField.getText();
			String name = nameField.getText();
			String author = authorField.getText();
			boolean selectedCB = genreCB.getSelectionModel().isEmpty();
			String price = priceField.getText();
			Integer stock = stockSpinner.getValue();
			
			// ini kalau ID is not empty :D
			if(id == "") {
				alertMessage = new Alert(AlertType.ERROR);
				alertMessage.setTitle("Error");
				alertMessage.setHeaderText("Please select book first");
				alertMessage.show();
			
			} else {
				if(name.length() < 5 || name.length() > 45) {
					alertMessage = new Alert(AlertType.ERROR);
					alertMessage.setTitle("Error");
					alertMessage.setHeaderText("Book name must between 5 - 45!");
					alertMessage.show();
					
				} else if(author.length() < 5 || author.length() > 20) {
					alertMessage = new Alert(AlertType.ERROR);
					alertMessage.setTitle("Error");
					alertMessage.setHeaderText("Author name must between 5 - 20!");
					alertMessage.show();
				
				} else if(selectedCB == true) {
					alertMessage = new Alert(AlertType.ERROR);
					alertMessage.setTitle("Error");
					alertMessage.setHeaderText("Genre must be selected!");
					alertMessage.show();
					
				} else if(checkPrice(price)) {
					alertMessage = new Alert(AlertType.ERROR);
					alertMessage.setTitle("Error");
					alertMessage.setHeaderText("Price must be numerical!");
					alertMessage.show();
					
				} else if(stock <= 0) {
					alertMessage = new Alert(AlertType.ERROR);
					alertMessage.setTitle("Error");
					alertMessage.setHeaderText("Stock must be more than 0!");
					alertMessage.show();
					
				} else {
					alertMessage = new Alert(AlertType.INFORMATION);
					alertMessage.setTitle("Notification");
					alertMessage.setHeaderText("Book Updated!");
					alertMessage.show();
					Connect c = new Connect();
					String cb = genreCB.getValue();
					String q="SELECT GenreID from genre where GenreName = \"" + cb + "\"";
					ResultSet r=c.executeSelect(q);
					int gid = 0;
					try {
						while (r.next()) {
							gid = r.getInt("GenreID");
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String qq = "UPDATE book SET BookName=\""+name+"\",BookAuthor=\""+author+"\",BookPrice="+price+",BookStock="+stock+",GenreID="+gid+" where BookID="+id;
					c.execute(qq);
					bookTable.getItems().clear();
					String qu = "SELECT * FROM book b JOIN genre g ON b.GenreID = g.GenreID";
					ResultSet re = c.executeSelect(qu);
					try {
						while (re.next()) {
							int ids = re.getInt("BookID");
							String nm = re.getString("BookName");
							String auth = re.getString("BookAuthor");
							String gen = re.getString("GenreName");
							int harga = re.getInt("BookPrice");
							int setok = re.getInt("BookStock");
							Book bk = new Book(ids, nm, auth, gen, setok, harga);
							bookTable.getItems().add(bk);
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			
		});
		
		deleteButton.setOnAction((event) -> {
			String id = idField.getText();
			
			// ini kalau ID is not empty :D
			if (id == "") {
				alertMessage = new Alert(AlertType.ERROR);
				alertMessage.setTitle("Error");
				alertMessage.setHeaderText("Please select book first");
				alertMessage.show();
			}
			else {
				alertMessage = new Alert(AlertType.INFORMATION);
				alertMessage.setTitle("Notification");
				alertMessage.setHeaderText("Book deleted!");
				alertMessage.show();
				Connect c = new Connect();
				String q = "DELETE FROM book where BookID="+id;
				c.execute(q);
				bookTable.getItems().clear();
				String qu = "SELECT * FROM book b JOIN genre g ON b.GenreID = g.GenreID";
				ResultSet re = c.executeSelect(qu);
				try {
					while (re.next()) {
						int ids = re.getInt("BookID");
						String nm = re.getString("BookName");
						String auth = re.getString("BookAuthor");
						String gen = re.getString("GenreName");
						int harga = re.getInt("BookPrice");
						int setok = re.getInt("BookStock");
						Book bk = new Book(ids, nm, auth, gen, setok, harga);
						bookTable.getItems().add(bk);
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
		primaryStage.setScene(manageBookFormScene);
		primaryStage.show();
	}

}
