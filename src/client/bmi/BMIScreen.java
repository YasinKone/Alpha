package client.bmi;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import client.App;
import client.ClientConnection;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;


/**
 *
 * @author Owner
 */
public class BMIScreen {
    public static Scene createBMIScene(Stage stage, App app){
        //THE INPUT FIELDS
        TextField weightField = new TextField();
        weightField.setPromptText("Weight (lbs)");
        weightField.setMaxWidth(200);
        weightField.setStyle("-fx-font-size: 16px;");
        
        TextField heightFeetField = new TextField();
        heightFeetField.setPromptText("Height (feet)");
        heightFeetField.setMaxWidth(60);
        heightFeetField.setStyle("-fx-font-size: 16px;");
        
         TextField heightInchesField = new TextField();
        heightInchesField.setPromptText("Height (inches)");
        heightInchesField.setMaxWidth(60);
        heightInchesField.setStyle("-fx-font-size: 16px;");
        
        Label resultLabel = new Label();
        resultLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");
        
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-font-size: 14px;");
        backButton.setOnAction(e ->{
            app.showWelcomeScreen();
        });
                
        Button calculateButton = new Button("Calculate BMI");
        calculateButton.setStyle(
            "-fx-background-color: #4CAF50; " +
            "-fx-text-fill: white; " +
            "-fx-font-weight: bold; " +
            "-fx-font-size: 16px;"
        );

        Button updateStatusButton = new Button("Update Status");
updateStatusButton.setStyle(
    "-fx-background-color: #2196F3; " +
    "-fx-text-fill: white; " +
    "-fx-font-weight: bold; " +
    "-fx-font-size: 16px;"
);

updateStatusButton.setOnAction(e -> {
    try {
        double weightLbs = Double.parseDouble(weightField.getText());
        int heightFeet = Integer.parseInt(heightFeetField.getText());
        int heightInches = Integer.parseInt(heightInchesField.getText());

        double bmi = BMIcalculator.calculateBMI(weightLbs, heightFeet, heightInches);
        String category = BMIcalculator.getBMICategory(bmi);

        
        ClientConnection client = new ClientConnection("localhost", 5000);
        client.send("UPDATE_STATUS_BMI:" + app.getLoggedInUsername() + ":" +
                    String.format("%.2f", bmi) + ":" + category);
        String response = client.receive();
        client.close();

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Status updated!\n\n" + response);
        alert.setHeaderText("BMI Sent to Server");
        alert.showAndWait();

    } catch (NumberFormatException err) {
        Alert error = new Alert(Alert.AlertType.ERROR, "Please enter valid numbers before updating.");
        error.showAndWait();
    } catch (Exception err) {
        Alert fail = new Alert(Alert.AlertType.ERROR, "Could not connect to server.");
        fail.showAndWait();
    }
});

        
        // LINE AND POINTER SETUP
        Pane bmiLinePane = new Pane();
        bmiLinePane.setPrefSize(300, 40);
        
        Line underweight = new Line(0, 20, 138.75, 20);
        underweight.setStroke(Color.CYAN); underweight.setStrokeWidth(10);
        
        Line healthy = new Line(138.75, 20, 186.75, 20);
        healthy.setStroke(Color.GREEN); healthy.setStrokeWidth(10);
        
        Line overweight = new Line(186.75, 20, 223.5, 20);
        overweight.setStroke(Color.GOLD); overweight.setStrokeWidth(10);
        
        Line obese = new Line(223.5, 20, 300, 20);
        obese.setStroke(Color.RED); obese.setStrokeWidth(10);
        
        Polygon bmiPointer = new Polygon(0.0, 12.0, -7.0, 0.0, 7.0, 0.0);
        bmiPointer.setFill(Color.BLACK);
        
        bmiLinePane.getChildren().addAll(underweight, healthy, overweight, obese, bmiPointer);
        
        
        
        HBox heightBox = new HBox(10, heightFeetField, new Label("ft"), heightInchesField, new Label("in"));
        heightBox.setAlignment(Pos.CENTER_LEFT);
        
         VBox content = new VBox(20, new Label("Enter your weight and height"), weightField, heightBox,
                calculateButton, updateStatusButton, resultLabel, bmiLinePane);
        content.setPadding(new Insets(30));
        content.setMaxWidth(500);
        content.setStyle("-fx-background-color: #ffffff; -fx-border-color: #ddd;");
        content.setAlignment(Pos.CENTER);
        
        HBox topBar = new HBox(backButton);
        topBar.setPadding(new Insets(10));
        topBar.setAlignment(Pos.TOP_LEFT);
        
        
        
        VBox root = new VBox(topBar, content);
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #e0eafc, #cfdef3);");
        root.setPrefSize(900, 650);
        root.setAlignment(Pos.CENTER);
           
        calculateButton.setOnAction(e -> {
            try {
                double weightLbs = Double.parseDouble(weightField.getText());
                int heightFeet = Integer.parseInt(heightFeetField.getText());
                int heightInches = Integer.parseInt(heightInchesField.getText());

                double bmi = BMIcalculator.calculateBMI(weightLbs, heightFeet, heightInches);
                String category = BMIcalculator.getBMICategory(bmi);
                resultLabel.setText(String.format("Your BMI is %.2f (%s)", bmi, category));
                
                // CALCULATES POINTER POS
                double position;
                
                switch (category) {
                    case "Underweight":
                        position = (bmi / 18.5) * 138.75;
                        break;
                    case "Healthy":
                        position = 138.75 + ((bmi - 18.5) / (24.9 - 18.5)) * 48;
                        break;
                    case "Overweight":
                        position = 186.75 + ((bmi - 25) / (29.9 - 25)) * 36.75;
                        break;
                    default:
                        position = 223.5 + ((Math.min(bmi, 40) - 30) / 10.0) * (300 - 223.5);
                        break;
                }
                

                    
                
                bmiPointer.setTranslateX(0);
                bmiPointer.setLayoutX(0);
                bmiPointer.setLayoutY(5);
                
                TranslateTransition transition = new TranslateTransition(Duration.millis(500), bmiPointer);
                transition.setToX(position);
                transition.setOnFinished(event -> Platform.runLater(() -> showProgramPrompt(category, app)));
                try {
    ClientConnection client = new ClientConnection("localhost", 5000);
    client.send("UPDATE_STATUS_BMI:" + app.getLoggedInUsername() + ":" +
                String.format("%.2f", bmi) + ":" + category);
    String response = client.receive();
    client.close();
    System.out.println("BMI update sent. Server replied: " + response);
} catch (Exception err) {
    System.out.println("Failed to update BMI: " + err.getMessage());
}

                transition.play();
            } catch (NumberFormatException ex) {
                resultLabel.setText("Enter valid numbers");
            }
        });
        
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        return new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());
    }
    
private static void showProgramPrompt(String category, App app){
  Alert alert = new Alert(Alert.AlertType.INFORMATION);
  alert.setTitle("Recommended Program");  
  switch (category) {
    case "Underweight":
        alert.setHeaderText("We recommend a bulking program.");
        alert.setContentText("Click below to explore bulking plans.");
        break;
    
    case "Obese":
        alert.setHeaderText("We recommend a weight loss program.");
        alert.setContentText("Click below to explore weight loss plans.");
        break;
    
    case "Overweight":
        alert.setHeaderText("You can either maintain or lose weight.");
        alert.setContentText("Would you like to explore fitness programs?");
        break;
    
    default:
        alert.setHeaderText("You are in a healthy range.");
        alert.setContentText("Would you like to explore fitness programs?");
        break;
}
ButtonType goToPrograms = new ButtonType("Go to Programs");
ButtonType close = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
alert.getButtonTypes().setAll(goToPrograms, close);

alert.showAndWait().ifPresent(response -> {
    if (response == goToPrograms) {
        app.showProgramScreen();
    }
   });
  }
}