package client.welcome;

import client.App;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;


/**
 *
 * @author Owner
 */
public class WelcomeScreen{ 
        private Scene scene;
        
        public WelcomeScreen(App app){
           Label welcomeLabel = new Label("Welcome to the FitSync!");
           welcomeLabel.setStyle("-fx-font-size: 48px; -fx-text-fill: black;");
           StackPane.setAlignment(welcomeLabel, Pos.TOP_CENTER);
           StackPane.setMargin(welcomeLabel, new Insets(40, 0, 0, 0));
           
            // Buttons
        Button btnCalorieTracker = new Button("Calorie Tracker");
        Button btnStatus = new Button("Status");
        Button btnPrograms = new Button("Programs");
        Button btnBMICalculator = new Button("BMI Calculator");
        Button btnNotes = new Button("Notes");
        for (Button btn : new Button[]{btnCalorieTracker, btnStatus, btnPrograms, btnNotes, btnBMICalculator}) {
            btn.setStyle("-fx-font-size: 18px;");
        }
        
        VBox buttonBox = new VBox(20, btnCalorieTracker, btnStatus, btnPrograms, btnBMICalculator);
        buttonBox.setAlignment(Pos.CENTER);
        StackPane.setAlignment(buttonBox, Pos.CENTER);
        
        // Questionnaire Button
        Button btnQuestionnaire = new Button("Questionnaire");
        btnQuestionnaire.setStyle("-fx-font-size: 18px;");
        StackPane.setAlignment(btnQuestionnaire, Pos.BOTTOM_CENTER);
        StackPane.setMargin(btnQuestionnaire, new Insets(30));
        
        btnQuestionnaire.setOnAction(e -> app.showQuestionnaireScreen());
        btnBMICalculator.setOnAction(e -> app.showBMIScreen());
        btnCalorieTracker.setOnAction(e -> app.showCalorieTrackerScreen());
        btnPrograms.setOnAction(e -> app.showProgramScreen());
        btnStatus.setOnAction(e -> app.showStatusScreen());
        
        
        // Background
        Image backgroundImage = new Image(getClass().getResource("/images/background.jpg").toExternalForm());
        BackgroundImage background = new BackgroundImage(
           backgroundImage,
            BackgroundRepeat.NO_REPEAT,
           BackgroundRepeat.NO_REPEAT,
           BackgroundPosition.CENTER,
           new BackgroundSize(100, 100, true, true, false, true)
        ); 
        
         StackPane layout = new StackPane(welcomeLabel, buttonBox, btnQuestionnaire);
        layout.setBackground(new Background(background));

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        this.scene = new Scene(layout, screenBounds.getWidth(), screenBounds.getHeight());
    }
    public Scene getScene(){
        return scene;
    }


}
