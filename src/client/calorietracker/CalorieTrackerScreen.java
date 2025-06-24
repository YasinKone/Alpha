package client.calorietracker;

import client.App;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;


public class CalorieTrackerScreen {

    private Scene scene;

    public CalorieTrackerScreen(App app) {
        TextField foodInput = new TextField();
        foodInput.setPromptText("Food name");

        TextField calorieInput = new TextField();
        calorieInput.setPromptText("Calories");

        Button addButton = new Button("Add");
        ListView<String> foodList = new ListView<>();
        Label totalLabel = new Label("Total Calories: 0");

        final int[] totalCalories = {0};

        addButton.setOnAction(e -> {
            String food = foodInput.getText().trim();
            String calText = calorieInput.getText().trim();
            try {
                int calories = Integer.parseInt(calText);
                totalCalories[0] += calories;
                totalLabel.setText("Total Calories: " + totalCalories[0]);
                foodList.getItems().add(food + " - " + calories + " cal");
                foodInput.clear();
                calorieInput.clear();
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a valid number for calories.");
                alert.show();
            }
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> app.showWelcomeScreen());

        VBox layout = new VBox(10, backButton, foodInput, calorieInput, addButton, foodList, totalLabel);
        layout.setPadding(new Insets(20));

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        layout.setPrefSize(screenBounds.getWidth(), screenBounds.getHeight());

        VBox.setVgrow(foodList, Priority.ALWAYS);

        this.scene = new Scene(layout, screenBounds.getWidth(), screenBounds.getHeight());
    }

    public Scene getScene() {
        return scene;
    }
}