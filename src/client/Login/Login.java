package client.Login;


import java.io.IOException;

import client.App;
import client.ClientConnection;
import client.signup.SignupScreen;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

public class Login extends Application {


    

    private App app;
private Stage primaryStage;

@Override
    public void start(Stage stage) throws IOException {
        throw new UnsupportedOperationException("Use new Login(app, stage).showLoginScreen() instead.");
    }

public void showLoginScreen(App app, Stage stage) {
    this.app = app;
    this.primaryStage = stage;

  


      
      primaryStage.setTitle("Fitness App");


  



         

        // UI Elements
        Label title = new Label("Fitness App");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label userLabel = new Label("Username:");
        TextField userField = new TextField();

        Label passLabel = new Label("Password:");
        PasswordField passField = new PasswordField();

        Label statusLabel = new Label();

        Button loginBtn = new Button("Login");
        Button signupBtn = new Button("Sign Up");

        // Button Actions
        loginBtn.setOnAction(e -> {
    String username = userField.getText().trim();
    String password = passField.getText().trim();

    if (username.isEmpty() || password.isEmpty()) {
        statusLabel.setText("Please fill in all fields.");
        return;
    }

    try {
        ClientConnection client = new ClientConnection("localhost", 5000);
        client.send("LOGIN:" + username + ":" + password);
        String response = client.receive();
        client.close();

        if (response.startsWith("SUCCESS")) {
            
            app.showWelcomeScreen();
            app.setLoggedInUsername(userField.getText().trim());

        } else {
            statusLabel.setText(response);
        }
    } catch (Exception ex) {
        statusLabel.setText("Server error: " + ex.getMessage());
    }
});


       signupBtn.setOnAction(e -> {
     SignupScreen signUp = new SignupScreen(app, primaryStage);
    primaryStage.setScene(signUp.getScene());
    primaryStage.show();
 
});

        // Layout
        VBox fieldsBox = new VBox(10, userLabel, userField, passLabel, passField, loginBtn, signupBtn, statusLabel);
        fieldsBox.setAlignment(Pos.CENTER);
        fieldsBox.setPadding(new Insets(20));

        BorderPane root = new BorderPane();
        root.setTop(title);
        BorderPane.setAlignment(title, Pos.CENTER);
        root.setCenter(fieldsBox);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
        
    }



 private void showSignUpScreen(Stage primaryStage, App app){


  Label userLabel = new Label("Username: ");
  Label passLabel = new Label("password: ");
  Label passLabel2 = new Label("re-enter password: ");
  TextField userField = new TextField ();
  TextField passField = new TextField ();
  TextField passField2 = new TextField ();
  Insets gridpadding = null;

Label badPassErrorLabel = new Label("");
badPassErrorLabel.setStyle("-fx-text-fill: red;");

Label badPassMatchErrorLabel = new Label("");
badPassMatchErrorLabel.setStyle("-fx-text-fill: red;");


   Button signUpBtn = new Button("Sign Up");
  signUpBtn.setDefaultButton(true);
  signUpBtn.setPrefWidth(120);
  signUpBtn.setPrefHeight(40);

   Button backBtn = new Button("Back");
  backBtn.setDefaultButton(true);
  backBtn.setPrefWidth(120);
  backBtn.setPrefHeight(40);










   Scene scene = null;
  GridPane gridpane = new GridPane();

  userField.setEditable(true);
  userField.setText("");
  userField.setPrefWidth(250);
  userField.setPrefHeight(35);

  passField.setEditable(true);
  passField.setText("");
  passField.setPrefWidth(250);
  passField.setPrefHeight(35);

  passField2.setEditable(true);
  passField2.setText("");
  passField2.setPrefWidth(250);
  passField2.setPrefHeight(35);

 
  gridpane.add(userLabel, 0, 0);     //placemnt of the diffrent components in the gridpane
  gridpane.add(userField, 1, 0);
  gridpane.add(passLabel, 0, 1);
  gridpane.add(passField, 1, 1);
  gridpane.add(passLabel2, 0, 2);
  gridpane.add(passField2, 1, 2);

  
  gridpadding = new Insets(20);                 //gridpane spacing
  gridpane.setPadding(gridpadding);
  gridpane.setAlignment(Pos.CENTER);
  gridpane.setHgap(10);
  gridpane.setVgap(10);

   VBox centerBox = new VBox(10);  
  centerBox.setAlignment(Pos.CENTER);          //stacks the grid and button vertically
  centerBox.setSpacing(20); // More breathing room between grid and button

  HBox signUpButtonBox = new HBox(signUpBtn);
  signUpButtonBox.setAlignment(Pos.CENTER_RIGHT);

  HBox backButtonBox = new HBox(backBtn);
  backButtonBox.setAlignment(Pos.CENTER_LEFT);

  centerBox.getChildren().addAll(gridpane, backButtonBox, signUpButtonBox, badPassMatchErrorLabel, badPassErrorLabel );
  
  StackPane root = new StackPane(centerBox);

  scene = new Scene(root, 400, 300);

  

 


 backBtn.setOnAction(e -> {
    try {
        Login loginScreen = new Login(); // ✅ pass both App and Stage
      loginScreen.start(primaryStage);
    } catch (IOException ex) {
        ex.printStackTrace();
    }
});

signUpBtn.setOnAction(e -> {
    String username = userField.getText().trim();
    String password = passField.getText().trim();
    String password2 = passField2.getText().trim();

    if (!isFormValid(userField, passField, passField2)) {
        showAlert("Missing Info", "Please complete all fields.");
        return;
    }

    if (!isValid(password)) {
        badPassErrorLabel.setText("Password must have at least 8 characters including a lowercase, uppercase, digit, and special character.");
        return;
    } else {
        badPassErrorLabel.setText("");
    }

    if (!password.equals(password2)) {
        badPassMatchErrorLabel.setText("Passwords must match!");
        return;
    } else {
        badPassMatchErrorLabel.setText("");
    }

    try {
        ClientConnection client = new ClientConnection("localhost", 5000);
        client.send("SIGNUP:" + username + ":" + password);
        String response = client.receive();
        client.close();

        if (response.startsWith("SUCCESS")) {
            showAlert("Success", "Account created! You can now log in.");
            signUpBtn.setDisable(true); // disable again until next verify
             app.showWelcomeScreen();
        } else {
            showAlert("Error", response);
        }
    } catch (Exception ex) {
        showAlert("Error", "Could not connect to server: " + ex.getMessage());
    }
});



  }

  public static boolean isValid(String password){
        boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;
        boolean validLength = true;
        int valid = 0;
        
        if(password.length() < 8 || password.length() > 16){
            validLength = false;
        }

        for(int i = 0; i < password.length(); i++){
            char letter = password.charAt(i);

            if(Character.isUpperCase(letter)){
                hasUpperCase = true;
            }
           
            else if(Character.isLowerCase(letter)){
                hasLowerCase = true;
            }

            else if(Character.isDigit(letter)){
                hasDigit = true;
            }

            else if(letter == ' '){
                valid--;
            }
            
            else{
                hasSpecialChar= true;
            }

        }

        if(hasUpperCase){
            valid++;
        }

        if(hasLowerCase){
            valid++;
        }

        if(hasDigit){
            valid++;
        }

        if(hasSpecialChar){
            valid++;
        }

        if(validLength && valid >= 4){
            return true;
        }

        else{
            return false;
        }
    }
  
 private boolean isFormValid(TextField userField, TextField passField, TextField passField2) {
        return !userField.getText().isEmpty()
                && !passField.getText().isEmpty()
                && !passField2.getText().isEmpty();
    }

 

     private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
  }