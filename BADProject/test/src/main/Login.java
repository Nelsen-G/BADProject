package main;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import util.Connect;

public class Login extends Application implements EventHandler<MouseEvent> {
	Stage primaryStage;
	Scene loginScene;
	
	BorderPane bp;
	GridPane gp;
	VBox vb;
	
	Text titleTxt, accountTxt;
	Label emailLbl, passwordLbl;
	TextField emailField;
	PasswordField passwordField;
	static Hyperlink registerLink;
	Button loginBtn;
	
	Font font1, font2;
	Alert alertMessage;

	boolean isAdmin = false;
	static int lid = 0;
	
	private void initialize() {
		bp = new BorderPane();
		gp = new GridPane();
		
		font1 = Font.font("Verdana", FontWeight.EXTRA_BOLD, 20);
		font2 = Font.font("Verdana", FontWeight.BOLD, 12);
		
		titleTxt = new Text("Login");
		titleTxt.setFont(font1);
		
		emailLbl = new Label("Email");
		emailField = new TextField();
		emailField.setPromptText("Email");
		emailField.setMaxWidth(300);
		
		passwordLbl = new Label("Password");
		passwordField = new PasswordField();
		passwordField.setPromptText("Password");
		passwordField.setMaxWidth(300);
		
		loginBtn = new Button("Login");
		loginBtn.setMinWidth(300);
		loginBtn.setMinHeight(35);
		loginBtn.setBackground(new Background(new BackgroundFill(Color.SLATEBLUE, null, null)));
		loginBtn.setFont(font2);
		loginBtn.setTextFill(Color.WHITE);
		
		accountTxt = new Text("Don't have an account?");
		
		registerLink = new Hyperlink("Register");
		registerLink.setOnMouseClicked(this);		
				
		loginScene = new Scene(bp, 500, 700);
	}
	
	private void layout() {
		gp.add(emailLbl, 0, 0);
		gp.add(emailField, 1, 0);
		gp.add(passwordLbl, 0, 1);
		gp.add(passwordField, 1, 1);
		
		gp.add(loginBtn, 1, 2);
		
		gp.setHgap(20);
		gp.setVgap(10);
		gp.setAlignment(Pos.CENTER);
		
		vb = new VBox(titleTxt, gp, accountTxt, registerLink);
		vb.setMargin(titleTxt, new Insets(25));
		vb.setMargin(accountTxt, new Insets(35, 0, 15, 0));
		vb.setAlignment(Pos.CENTER);
		
		bp.setCenter(vb);
	}
	
	private void eventHandler() {
		loginBtn.setOnAction((event)->{
			Connect c = new Connect();
			String q = "SELECT * FROM users";
			String em, p, r="", n="";
			int flag = 0;
			ResultSet rs = c.executeSelect(q);
			try {
				while (rs.next()) {
					em = rs.getString("UserEmail");
					p = rs.getString("UserPass");
					
					if (emailField.getText().equals(em) && passwordField.getText().equals(p)) {
						flag = 1;
						r = rs.getString("UserRole");
						n = rs.getString("UserFullName");
					}
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			if(flag == 1) {
				if (r.equals("admin")) {
					alertMessage = new Alert(AlertType.INFORMATION);
					alertMessage.setTitle("Notification");
					alertMessage.setHeaderText("Welcome, Admin!");
					alertMessage.showAndWait();
					
					primaryStage.close();
					Stage newStage = new Stage();
					try {
						new AdminMainForm().start(newStage);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if (r.equals("user")) {
					alertMessage = new Alert(AlertType.INFORMATION);
					alertMessage.setTitle("Notification");
					alertMessage.setHeaderText("Welcome, "+n+"!");
					alertMessage.showAndWait();
					Connect cn=new Connect();
					String que = "SELECT * from users where UserEmail=\""+emailField.getText()+"\"";
					ResultSet rss =cn.executeSelect(que);
					try {
						while(rss.next()) {
							int nn =rss.getInt("UserID");
							lid = nn;
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					primaryStage.close();
					Stage newStage = new Stage();
					
					try {
						new UserMainForm().start(newStage);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
				}
				
			// nnti ini diganti sesuai user :D
			} else {
				alertMessage = new Alert(AlertType.ERROR);
				alertMessage.setTitle("Error");
				alertMessage.setHeaderText("Invalid username or password!");
				alertMessage.showAndWait();		
			}
		});
	}
	
	public Scene getLoginScene() {
		initialize();
		return loginScene;
	}

	public void setLoginScene(Scene loginScene) {
		this.loginScene = loginScene;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}
	
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		lid = 0;
		this.primaryStage = primaryStage;
		initialize();
		layout();
		eventHandler();
		primaryStage.setResizable(false);
		primaryStage.getIcons().add(new Image("file:src/assets/book.png"));
		primaryStage.setTitle("Bookstore");
		primaryStage.setScene(loginScene);
		primaryStage.show();
	}

	@Override
	public void handle(MouseEvent event) {
		// TODO Auto-generated method stub
		if(event.getEventType() == event.MOUSE_CLICKED) {
			if(event.getSource() == registerLink) {
				
				primaryStage.close();
				
				Stage newStage = new Stage();
				try {
					new Register().start(newStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
