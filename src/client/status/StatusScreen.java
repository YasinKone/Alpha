package client.status;

import client.App;
import client.ClientConnection;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import server.Userdata;

public class StatusScreen {
    private Scene scene;

    public StatusScreen(App app) {
        Userdata data = null;
        try {
            ClientConnection client = new ClientConnection("localhost", 5000);
            client.send("GET_STATUS:" + app.getLoggedInUsername());
            Object response = client.receive();
            client.close();
            if (response instanceof Userdata) {
                data = (Userdata) response;
                System.out.println("✅ Userdata received from server.");
            } else {
                System.out.println("⚠️ Server did not return Userdata.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        VBox layout = new VBox(15);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.TOP_LEFT);

        try {
            if (data != null) {
                layout.getChildren().addAll(
                    label("Name: " + safe(data.name)),
                    label("Age: " + (data.age > 0 ? data.age : "Not provided")),
                    label("Height: " + (data.height > 0 ? data.height + " inches" : "Not provided")),
                    label("Weight: " + (data.weight > 0 ? data.weight + " lbs" : "Not provided")),
                    label("Goal: " + safe(data.goal)),
                    label("Macros:\n" + safe(data.macros)),
                    label("Sample Meals:\n" + safe(data.sampleMeals)),
                    label("BMI: " + (data.bmiValue > 0 ? String.format("%.2f", data.bmiValue) : "Not calculated") +
                          (data.bmiCategory != null ? " (" + data.bmiCategory + ")" : "")),
                    label("Advice:\n" + safe(data.advice))
                );
            } else {
                layout.getChildren().add(label("No user data found. Try submitting info via questionnaire and program pages."));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            layout.getChildren().add(label("⚠️ Error displaying user data."));
        }

        Button backButton = new Button("Back to Main Page");
        backButton.setOnAction(e -> app.showWelcomeScreen());
        layout.getChildren().add(backButton);

        // Background setup
        Image backgroundImage = new Image(getClass().getResource("/images/background.jpg").toExternalForm());
        BackgroundImage background = new BackgroundImage(
            backgroundImage,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(100, 100, true, true, false, true)
        );
        StackPane root = new StackPane(layout);
        root.setBackground(new Background(background));

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        this.scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());
    }

    public Scene getScene() {
        return scene;
    }

    private Label label(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 16px; -fx-text-fill: black;");
        return label;
    }

    private String safe(String value) {
        return value != null && !value.trim().isEmpty() ? value : "Not provided";
    }
}

