package client.questionaire;


import client.App;
import client.ClientConnection;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.scene.image.Image;

public class QuestionaireScreen {
    private Scene scene;

    public QuestionaireScreen(App app) {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_LEFT);

        // Q1: Name
        Label q1 = new Label("1. What is your name?");
        TextField name = new TextField();
        name.setPromptText("Enter your name");

        // Q2: Age
        Label q2 = new Label("2. What is your age?");
        TextField age = new TextField();
        age.setPromptText("Enter your age");

        // Q3: Gender
        Label q3 = new Label("3. What is your gender?");
        ToggleGroup genderGroup = new ToggleGroup();
        RadioButton r1 = new RadioButton("Male");
        RadioButton r2 = new RadioButton("Female");
        RadioButton r3 = new RadioButton("Other");
        r1.setToggleGroup(genderGroup);
        r2.setToggleGroup(genderGroup);
        r3.setToggleGroup(genderGroup);
        HBox genderRow = new HBox(10, r1, r2, r3);

        // Q4: Exercise Frequency
        Label q4 = new Label("4. How often do you exercise?");
        ComboBox<String> ex = new ComboBox<>();
        ex.getItems().addAll("Never", "1-2 times/week", "3-5 times/week", "Daily");
        ex.setPromptText("Select frequency");

        // Q5: Calorie Goal
        Label q5 = new Label("5. What is your daily calorie goal?");
        TextField cal = new TextField();
        cal.setPromptText("e.g. 2200");

        // Q6: Diet Goal
        Label q6 = new Label("6. Choose your diet goal:");
        ChoiceBox<String> goal = new ChoiceBox<>();
        goal.getItems().addAll("Cut", "Bulk", "Maintain");
        goal.setTooltip(new Tooltip("Pick your goal"));

        // Buttons
        Button goBtn = new Button("Submit");
        Label result = new Label();
        result.setWrapText(true);

        Button backBtn = new Button("Back to Main Page");
        backBtn.setOnAction(e -> app.showWelcomeScreen());

        goBtn.setOnAction(e -> {
            String theName = name.getText().trim();
            String theAge = age.getText().trim();
            RadioButton picked = (RadioButton) genderGroup.getSelectedToggle();
            String theGender = (picked == null) ? "Not specified" : picked.getText();
            String theFreq = ex.getValue();
            String theCals = cal.getText().trim();
            String theGoal = goal.getValue();

            if (theName.isEmpty() || theAge.isEmpty() || theFreq == null || theCals.isEmpty() || theGoal == null) {
                result.setText("Please answer all questions.");
                return;
            }

            int ageNum, calNum;
            try {
                ageNum = Integer.parseInt(theAge);
                calNum = Integer.parseInt(theCals);
                if (ageNum <= 0 || calNum <= 0) throw new NumberFormatException();
            } catch (NumberFormatException err) {
                result.setText("Enter valid numbers for age and calories.");
                return;
            }

            String advice;
            switch (theGoal) {
                case "Cut":
                    advice = "To cut fat, stay in a calorie deficit. Focus on protein, fiber, and cardio.";
                    break;
                case "Bulk":
                    advice = "To bulk up, eat in a surplus. Lift heavy and eat protein-rich meals.";
                    break;
                case "Maintain":
                    advice = "To maintain, stay consistent. Monitor weight and activity level.";
                    break;
                default:
                    advice = "No advice available.";
            }

            result.setText(String.format(
                " Summary for %s:\nAge: %d\nGender: %s\nExercise: %s\nCalorie Goal: %d\nDiet Goal: %s\n\n💡 Advice:\n%s",
                theName, ageNum, theGender, theFreq, calNum, theGoal, advice
            ));
         Button updateStatusBtn = new Button("Update Status");
    updateStatusBtn.setOnAction(ev -> {
        try {
            ClientConnection client = new ClientConnection("localhost", 5000);
            client.send("UPDATE_STATUS:" +
                theName + ":" +
                ageNum + ":" +
                theGender + ":" +
                theFreq + ":" +
                calNum + ":" +
                theGoal + ":" +
                advice
            );
            String response = client.receive();
            client.close();
            result.setText("Status updated!\n" + response);
        } catch (Exception exFreq) {
            result.setText("Error updating status: " + exFreq.getMessage());
        }
    });

    layout.getChildren().add(updateStatusBtn);});

 

        layout.getChildren().addAll(
            q1, name,
            q2, age,
            q3, genderRow,
            q4, ex,
            q5, cal,
            q6, goal,
            goBtn,
            result,
            backBtn        );
        
        //Backround image setup
        Image backgroundImage = new Image(getClass().getResource("/images/background.jpg").toExternalForm());
        BackgroundImage background = new BackgroundImage(
        backgroundImage,
        BackgroundRepeat.NO_REPEAT,
        BackgroundRepeat.NO_REPEAT,
        BackgroundPosition.CENTER,
        new BackgroundSize(100, 100, true, true, false, true) // cover whole background
    );
    
    //Stackpane to hold backround and content
        StackPane root = new StackPane();
        root.setBackground(new Background(background));
        root.getChildren().add(layout);


        // Set scene to full screen size
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        this.scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());
    }

    public Scene getScene() {
        return scene;
    }
}
