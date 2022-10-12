import java.util.HashMap;
import java.util.Map;

public class Item {
    public double Number_of_times_pregnant;
    public double Plasma_glucose_concentration_a_2_hours_in_an_oral_glucose_tolerance_test;
    public double Diastolic_blood_pressure;
    public double Triceps_skin_fold_thickness;
    public double two_Hour_serum_insulin;
    public double Body_mass_index;
    public double Diabetes_pedigree_function;
    public double Age;
    public boolean Class_variable;
    public Map<Double,Item> distance_map = new HashMap();
    public static double yes_mean_1;
    public static double yes_mean_2;
    public static double yes_mean_3;
    public static double yes_mean_4;
    public static double yes_mean_5;
    public static double yes_mean_6;
    public static double yes_mean_7;
    public static double yes_mean_8;
    public static double no_mean_1;
    public static double no_mean_2;
    public static double no_mean_3;
    public static double no_mean_4;
    public static double no_mean_5;
    public static double no_mean_6;
    public static double no_mean_7;
    public static double no_mean_8;
    public static int cnt_yes;
    public static int cnt_no;

    public Item(double Attribute_1, double Attribute_2, double Attribute_3, double Attribute_4, double Attribute_5, double Attribute_6, double Attribute_7, double Attribute_8,String class_var){
        Number_of_times_pregnant = Attribute_1;
        Plasma_glucose_concentration_a_2_hours_in_an_oral_glucose_tolerance_test = Attribute_2;
        Diastolic_blood_pressure = Attribute_3;
        Triceps_skin_fold_thickness = Attribute_4;
        two_Hour_serum_insulin = Attribute_5;
        Body_mass_index = Attribute_6;
        Diabetes_pedigree_function = Attribute_7;
        Age = Attribute_8;
        if(class_var.equals("yes")){
            Class_variable = true;
            cnt_yes ++;
            yes_mean_1 += Attribute_1;
            yes_mean_2 += Attribute_2;
            yes_mean_3 += Attribute_3;
            yes_mean_4 += Attribute_4;
            yes_mean_5 += Attribute_5;
            yes_mean_6 += Attribute_6;
            yes_mean_7 += Attribute_7;
            yes_mean_8 += Attribute_8;
        }
        else{
            Class_variable = false;
            cnt_no ++;
            no_mean_1 += Attribute_1;
            no_mean_2 += Attribute_2;
            no_mean_3 += Attribute_3;
            no_mean_4 += Attribute_4;
            no_mean_5 += Attribute_5;
            no_mean_6 += Attribute_6;
            no_mean_7 += Attribute_7;
            no_mean_8 += Attribute_8;}
    }
    public Item(double Attribute_1, double Attribute_2, double Attribute_3, double Attribute_4, double Attribute_5, double Attribute_6, double Attribute_7, double Attribute_8,boolean answer) {
        Number_of_times_pregnant = Attribute_1;
        Plasma_glucose_concentration_a_2_hours_in_an_oral_glucose_tolerance_test = Attribute_2;
        Diastolic_blood_pressure = Attribute_3;
        Triceps_skin_fold_thickness = Attribute_4;
        two_Hour_serum_insulin = Attribute_5;
        Body_mass_index = Attribute_6;
        Diabetes_pedigree_function = Attribute_7;
        Age = Attribute_8;
        this.Class_variable = answer;
    }
    public Item(double Attribute_1, double Attribute_2, double Attribute_3, double Attribute_4, double Attribute_5, double Attribute_6, double Attribute_7, double Attribute_8) {
        Number_of_times_pregnant = Attribute_1;
        Plasma_glucose_concentration_a_2_hours_in_an_oral_glucose_tolerance_test = Attribute_2;
        Diastolic_blood_pressure = Attribute_3;
        Triceps_skin_fold_thickness = Attribute_4;
        two_Hour_serum_insulin = Attribute_5;
        Body_mass_index = Attribute_6;
        Diabetes_pedigree_function = Attribute_7;
        Age = Attribute_8;
    }
    public Item(double Attribute_2,double Attribute_5, double Attribute_6, double Attribute_7, double Attribute_8, String class_var) {
        Plasma_glucose_concentration_a_2_hours_in_an_oral_glucose_tolerance_test = Attribute_2;
        two_Hour_serum_insulin = Attribute_5;
        Body_mass_index = Attribute_6;
        Diabetes_pedigree_function = Attribute_7;
        Age = Attribute_8;if(class_var.equals("yes")){
            Class_variable = true;
            cnt_yes ++;
            yes_mean_2 += Attribute_2;
            yes_mean_5 += Attribute_5;
            yes_mean_6 += Attribute_6;
            yes_mean_7 += Attribute_7;
            yes_mean_8 += Attribute_8;
        }
        else{
            Class_variable = false;
            cnt_no ++;
            no_mean_2 += Attribute_2;
            no_mean_5 += Attribute_5;
            no_mean_6 += Attribute_6;
            no_mean_7 += Attribute_7;
            no_mean_8 += Attribute_8;}
    }
    public Item(double Attribute_2,double Attribute_5, double Attribute_6, double Attribute_7, double Attribute_8, boolean answer) {
        Plasma_glucose_concentration_a_2_hours_in_an_oral_glucose_tolerance_test = Attribute_2;
        two_Hour_serum_insulin = Attribute_5;
        Body_mass_index = Attribute_6;
        Diabetes_pedigree_function = Attribute_7;
        Age = Attribute_8;
        this.Class_variable = answer;
    }
}
