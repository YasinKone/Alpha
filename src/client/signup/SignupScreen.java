package client.signup;
import client.App;
import client.ClientConnection;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class SignupScreen {
    private Scene scene;

    public SignupScreen(App app, Stage primaryStage) {
        Label userLabel = new Label("Username: ");
        Label passLabel = new Label("Password: ");
        Label passLabel2 = new Label("Re-enter Password: ");
        TextField userField = new TextField();
        PasswordField passField = new PasswordField();
        PasswordField passField2 = new PasswordField();

        Label badPassErrorLabel = new Label("");
        badPassErrorLabel.setStyle("-fx-text-fill: red;");
        Label badPassMatchErrorLabel = new Label("");
        badPassMatchErrorLabel.setStyle("-fx-text-fill: red;");

        Button signUpBtn = new Button("Sign Up");
        Button backBtn = new Button("Back");

        GridPane grid = new GridPane();
        grid.add(userLabel, 0, 0);
        grid.add(userField, 1, 0);
        grid.add(passLabel, 0, 1);
        grid.add(passField, 1, 1);
        grid.add(passLabel2, 0, 2);
        grid.add(passField2, 1, 2);
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);

        VBox layout = new VBox(10, grid, backBtn, signUpBtn, badPassMatchErrorLabel, badPassErrorLabel);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        this.scene = new Scene(new StackPane(layout), 400, 300);

        // Button Actions
        backBtn.setOnAction(e -> app.showLoginScreen());

        signUpBtn.setOnAction(e -> {
            String username = userField.getText().trim();
            String password = passField.getText().trim();
            String password2 = passField2.getText().trim();

            if (username.isEmpty() || password.isEmpty() || password2.isEmpty()) {
                showAlert("Missing Info", "Please complete all fields.");
                return;
            }

            if (!isValid(password)) {
                badPassErrorLabel.setText("Password must be 8+ chars, with upper/lowercase, digit, and symbol.");
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
                    app.showWelcomeScreen(); 
                    app.setLoggedInUsername(userField.getText().trim());

                } else {
                    showAlert("Error", response);
                }
            } catch (Exception ex) {
                showAlert("Server Error", ex.getMessage());
            }
        });
    }

    public Scene getScene() {
        return scene;
    }

    private boolean isValid(String password) {
        boolean hasUpper = false, hasLower = false, hasDigit = false, hasSpecial = false;
        if (password.length() < 8) return false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else if (!Character.isWhitespace(c)) hasSpecial = true;
        }
        return hasUpper && hasLower && hasDigit && hasSpecial;
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}