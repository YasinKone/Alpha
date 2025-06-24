package client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import client.welcome.WelcomeScreen;
import client.Login.Login;
import client.bmi.BMIScreen;
import client.questionaire.QuestionaireScreen;
import client.status.StatusScreen;
import client.calorietracker.CalorieTrackerScreen;
import client.programs.ProgramScreen;



public class App extends Application{
    private Stage primaryStage;
    private String loggedInUsername;

    public String getLoggedInUsername() {
    return loggedInUsername;
}

public void setLoggedInUsername(String username) {
    this.loggedInUsername = username;
}


    
    @Override
    public void start(Stage primaryStage){
        this.primaryStage = primaryStage;
        showLoginScreen();
        
    }

     public void showLoginScreen() {
        try {
            Login loginScreen = new Login();
            loginScreen.showLoginScreen(this, primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    
    public void showWelcomeScreen() {
        WelcomeScreen welcome = new WelcomeScreen(this);
        primaryStage.setScene(welcome.getScene());
        primaryStage.setTitle("FitSync");
        primaryStage.show();
        primaryStage.setMaximized(true);
    }
    
    public void showBMIScreen(){
        Scene bmiScene = BMIScreen.createBMIScene(primaryStage,this);
        primaryStage.setScene(bmiScene);
        primaryStage.setMaximized(true);
    }
    
    public void showQuestionnaireScreen() {
        QuestionaireScreen questionnaire = new QuestionaireScreen(this);
        Scene questionnaireScene = questionnaire.getScene();
        primaryStage.setScene(questionnaire.getScene());
        primaryStage.setMaximized(true);
    }
    
   public void showCalorieTrackerScreen() {
    CalorieTrackerScreen tracker = new CalorieTrackerScreen(this);
    primaryStage.setScene(tracker.getScene());
    primaryStage.setMaximized(true);
    }
   
   public void showProgramScreen() {
    ProgramScreen screen = new ProgramScreen(this);
    primaryStage.setScene(screen.getScene());
    primaryStage.setMaximized(true);
}

public void showStatusScreen() {
    primaryStage.setScene(new StatusScreen(this).getScene());
}
   
   
   
    
    public static void main(String[] args){
        launch(args);
    }

    public void setTitle(String string) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'setTitle'");
    }

}
