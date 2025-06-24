package server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Userdata implements Serializable {
    public List<String> workouts = new ArrayList<>();
    public int weeklyGoalMinutes = 150;

    // New fields
    public String name;
    public int age;
    public double height; // in cm or inches
    public double weight; // in kg or lbs
    public String goal;   // e.g. "Lose Weight", "Gain Muscle"
    public String macros; // e.g. "40/30/30"
    public String sampleMeals;
    public double bmiValue;
public String bmiCategory;
    public String advice;

    public Userdata() {
        // Default constructor
    }
}