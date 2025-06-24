package client.programs;


import client.App;
import client.ClientConnection;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import server.Userdata;



/**
 *
 * @author Owner
 */
public class ProgramScreen {
    private Scene scene;
    private VBox contentBox;
    private Label titleLabel;
    private TextArea infoArea;
    private TextField weightField;
    private TextField heightFeetField;
    private TextField heightInchesField;
    private Label macrosLabel;
    private String selectedProgram = null;
    private String macroText = "";
    private App app;
    private Button updateStatusBtn;


    public ProgramScreen(App app) {
        this.app = app;

        titleLabel = new Label("Choose a Program");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        infoArea = new TextArea();
        infoArea.setWrapText(true);
        infoArea.setEditable(false);
        infoArea.setPrefHeight(180);
        infoArea.setStyle("-fx-font-size: 16px;");
        infoArea.setVisible(false);  // Hidden until program selected

        weightField = new TextField();
        weightField.setPromptText("Weight (lbs)");
        weightField.setMaxWidth(150);

        heightFeetField = new TextField();
        heightFeetField.setPromptText("Height (feet)");
        heightFeetField.setMaxWidth(60);

        heightInchesField = new TextField();
        heightInchesField.setPromptText("Height (inches)");
        heightInchesField.setMaxWidth(60);

        HBox heightBox = new HBox(5, heightFeetField, new Label("ft"), heightInchesField, new Label("in"));
        heightBox.setAlignment(Pos.CENTER_LEFT);

        Button bulkBtn = new Button("Bulk");
        Button cutBtn = new Button("Cut");
        Button maintainBtn = new Button("Maintain");

        bulkBtn.setOnAction(e -> {
            selectedProgram = "Bulk";
            showProgramInfo();
        });
        cutBtn.setOnAction(e -> {
            selectedProgram = "Cut";
            showProgramInfo();
        });
        maintainBtn.setOnAction(e -> {
            selectedProgram = "Maintain";
            showProgramInfo();
        });

        HBox programButtons = new HBox(20, bulkBtn, cutBtn, maintainBtn);
        programButtons.setAlignment(Pos.CENTER);

        Button calcButton = new Button("Calculate Macros");
        calcButton.setOnAction(e -> calculateAndShowMacros());

        macrosLabel = new Label();
        macrosLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        macrosLabel.setWrapText(true);

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> app.showWelcomeScreen());

        updateStatusBtn = new Button("Update Status");
updateStatusBtn.setOnAction(e -> {
    if (selectedProgram == null || macroText == null || macroText.isEmpty()) {
        macrosLabel.setText("Please select a program and calculate macros first.");
        return;
    }

    try {
        String sampleMealsText = infoArea.getText().split("\n\n")[1];

        Userdata data = new Userdata();
        data.goal = selectedProgram;
        data.macros = macroText;
        data.sampleMeals = sampleMealsText;

        ClientConnection client = new ClientConnection("localhost", 5000);
        client.send("UPDATE_STATUS_PROGRAM:" + app.getLoggedInUsername() + ":" +
                    selectedProgram + ":" +
                    macroText.replace("\n", "\\n") + ":" +
                    sampleMealsText.replace("\n", "\\n"));
        String response = client.receive();
        client.close();

        System.out.println("Server Response: " + response);
        macrosLabel.setText("Status updated!");
    } catch (Exception ex) {
        ex.printStackTrace();
        macrosLabel.setText("Error updating status.");
    }
});


        contentBox = new VBox(15,
                titleLabel,
                programButtons,
                new Label("Enter your weight and height:"),
                weightField,
                heightBox,
                calcButton,
                macrosLabel,
                infoArea,
                updateStatusBtn,
                backButton);

        contentBox.setPadding(new Insets(30));
        contentBox.setAlignment(Pos.TOP_CENTER);

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        scene = new Scene(contentBox, screenBounds.getWidth(), screenBounds.getHeight());
    }

    private void showProgramInfo() {
        infoArea.setVisible(true);
        macrosLabel.setText("");  // Clear previous macros
        String sampleMealsText = infoArea.getText().split("\n\n")[1];  // crude split to extract "Sample Meals" section

Userdata data = new Userdata();
data.goal = selectedProgram;
data.macros = macroText;
data.sampleMeals = sampleMealsText;

// Send to server
try {
    String message = "UPDATE_STATUS_PROGRAM:" + app.getLoggedInUsername() + ":" +
                     selectedProgram + ":" +
                     macroText.replace("\n", "\\n") + ":" + 
                     sampleMealsText.replace("\n", "\\n");

    System.out.println("🔼 Sending to server: " + message);  // Debug print

    ClientConnection client = new ClientConnection("localhost", 5000);
    client.send(message);

    String response = client.receive();
    System.out.println("Server Response: " + response);

    client.close();
} catch (Exception ex) {
    ex.printStackTrace();
}
        titleLabel.setText(selectedProgram + " Program");

        switch (selectedProgram) {
            case "Bulk":
                infoArea.setText(
                        "Goal: Gain muscle mass by eating in a calorie surplus.\n\n" +
                        "Sample Meals:\n" +
                        "- Grilled chicken breast, brown rice, steamed vegetables\n" +
                        "- Oatmeal with nuts and fruit\n" +
                        "- Protein shake with banana and peanut butter\n\n" +
                        "Workout Tips:\n" +
                        "- Focus on heavy compound lifts (squats, deadlifts, bench press).\n" +
                        "- Train 4-6 times per week with progressive overload.\n" +
                        "- Ensure adequate rest and recovery.");
                break;

            case "Cut":
                infoArea.setText(
                        "Goal: Lose fat by eating in a calorie deficit while maintaining muscle.\n\n" +
                        "Sample Meals:\n" +
                        "- Lean turkey or chicken breast with mixed greens\n" +
                        "- Greek yogurt with berries and almonds\n" +
                        "- Steamed fish with broccoli and quinoa\n\n" +
                        "Workout Tips:\n" +
                        "- Maintain strength training to preserve muscle.\n" +
                        "- Add moderate cardio (3-5 sessions per week).\n" +
                        "- Monitor progress and adjust calories as needed.");
                break;

            case "Maintain":
                infoArea.setText(
                        "Goal: Maintain current weight and fitness level.\n\n" +
                        "Sample Meals:\n" +
                        "- Balanced meals including lean protein, whole grains, and vegetables\n" +
                        "- Healthy snacks like nuts, fruits, and yogurt\n" +
                        "- Regular hydration and mindful eating\n\n" +
                        "Workout Tips:\n" +
                        "- Keep a consistent exercise routine.\n" +
                        "- Mix strength, cardio, and flexibility workouts.\n" +
                        "- Focus on overall well-being and recovery.");
                break;
        }
    }

    private void calculateAndShowMacros() {
        try {
            if (selectedProgram == null) {
                macrosLabel.setText("Please select a program first.");
                return;
            }

            double weight = Double.parseDouble(weightField.getText());
            int feet = Integer.parseInt(heightFeetField.getText());
            int inches = Integer.parseInt(heightInchesField.getText());
            int totalInches = feet * 12 + inches;

            double maintenanceCalories = weight * 15;

            double calorieTarget;
            if (selectedProgram.equals("Bulk")) {
                calorieTarget = maintenanceCalories * 1.15;
            } else if (selectedProgram.equals("Cut")) {
                calorieTarget = maintenanceCalories * 0.85;
            } else {
                calorieTarget = maintenanceCalories;
            }

            double proteinGrams = weight;

            double fatCalories;
            if (selectedProgram.equals("Bulk")) {
                fatCalories = calorieTarget * 0.20;
            } else {
                fatCalories = calorieTarget * 0.25;
            }

            double fatGrams = fatCalories / 9;
            double proteinCalories = proteinGrams * 4;
            double carbCalories = calorieTarget - (proteinCalories + fatCalories);
            double carbGrams = carbCalories / 4;

            macroText = String.format(
                    "Based on your inputs and the %s program:\n" +
                            "- Total Calories: %.0f kcal\n" +
                            "- Protein: %.0f g\n" +
                            "- Carbs: %.0f g\n" +
                            "- Fats: %.0f g",
                    selectedProgram, calorieTarget, proteinGrams, carbGrams, fatGrams
            );

            macrosLabel.setText(macroText);

        } catch (NumberFormatException e) {
            macrosLabel.setText("Please enter valid numbers for weight and height.");
        }
    }

    public Scene getScene() {
        return scene;
    }
}
