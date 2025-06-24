package client.bmi;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author Owner
 */
public class BMIcalculator { // using FT,Inches Lbs using the formula for BMI
    public static double calculateBMI(double weightLbs, double heightFeet, int heightInches ){
        // feet turned into inches. 
        double totalInches = (heightFeet * 12 ) + heightInches;
        // the formula for BMI
        return (weightLbs / (totalInches * totalInches)) * 703;
    }
    // THE BMI "CALCULATOR"
    public static String getBMICategory(double bmi){
        if (bmi < 18.5){
            return "Underweight";
        }else if (bmi < 25){
            return "Healthy";
        }else if (bmi < 30){
            return "Overweight";
        } else {
            return "Obese";
        }
    }
    
}
