import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class CrossValidation {
        public static List<Item> training = new ArrayList<>();
        public static List<Item> testing = new ArrayList<>();
        public static void Read_File(String filename,int literate) throws IOException {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line = null;
            boolean IfTrain = false;
            while((line=reader.readLine())!=null){
                if(line.equals("fold"+literate+",,,,,,,,")){
                    IfTrain = true;
                    continue;
                }
                else if(line.startsWith("fold")){
                    IfTrain = false;
                    continue;
                }
                else if(line.equals(",,,,,,,,")){continue;}
                else{
                    String items[] = line.split(",");
                    List<String> item_list = new ArrayList<>();
                    for(String i:items){
                        item_list.add(i);
                    }
                    if(IfTrain){
                        String class_var = item_list.get(8);
                        item_list.remove(8);
                        List<Double> attr_list =  new ArrayList<>();
                        for(String i:item_list){
                            attr_list.add(Double.parseDouble(i));
                        }
                        Item x = new Item(attr_list.get(0),attr_list.get(1),attr_list.get(2),attr_list.get(3),attr_list.get(4),attr_list.get(5),attr_list.get(6),attr_list.get(7),class_var);
                        training.add(x);
                    }
                    else {
                        List<Double> attr_list = new ArrayList<>();
                        String class_var = item_list.get(8);
                        item_list.remove(8);
                        for (String i : item_list) {
                            attr_list.add(Double.parseDouble(i));
                        }
                        if(class_var.equals("yes")){
                            Item x = new Item(attr_list.get(0), attr_list.get(1), attr_list.get(2), attr_list.get(3), attr_list.get(4), attr_list.get(5), attr_list.get(6), attr_list.get(7),true);
                            testing.add(x);
                        }
                        else{
                            Item x = new Item(attr_list.get(0), attr_list.get(1), attr_list.get(2), attr_list.get(3), attr_list.get(4), attr_list.get(5), attr_list.get(6), attr_list.get(7),false);
                            testing.add(x);
                        }
                    }
                }
            }
        }
    public static void Read_File_CFS(String filename,int literate) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line = null;
        boolean IfTrain = false;
        while((line=reader.readLine())!=null){
            if(line.equals("fold"+literate + ",,,,,,,,")){
                IfTrain = true;
                continue;
            }
            else if(line.startsWith("fold")){
                IfTrain = false;
                continue;
            }
            else if(line.equals(",,,,,,,,")){
                continue;}
            else{
                String items[] = line.split(",");
                List<String> item_list = new ArrayList<>();
                for(String i:items){
                    item_list.add(i);
                }
                if(IfTrain){
                    String class_var = item_list.get(8);
                    item_list.remove(8);
                    List<Double> attr_list =  new ArrayList<>();
                    for(String i:item_list){
                        attr_list.add(Double.parseDouble(i));
                    }
                    Item x = new Item(attr_list.get(1),attr_list.get(4),attr_list.get(5),attr_list.get(6),attr_list.get(7),class_var);
                    training.add(x);
                }
                else {
                    List<Double> attr_list = new ArrayList<>();
                    String class_var = item_list.get(8);
                    item_list.remove(8);
                    for (String i : item_list) {
                        attr_list.add(Double.parseDouble(i));
                    }
                    if(class_var.equals("yes")){
                        Item x = new Item(attr_list.get(1), attr_list.get(4), attr_list.get(5), attr_list.get(6), attr_list.get(7),true);
                        testing.add(x);
                    }
                    else{
                        Item x = new Item(attr_list.get(1), attr_list.get(4), attr_list.get(5), attr_list.get(6), attr_list.get(7),false);
                        testing.add(x);
                    }
                }
            }
        }
    }
        public static double PDF(double x,double mean,double standard_deviation){
            double answer;
            answer = ((1/(standard_deviation*Math.sqrt(2*Math.PI)))*Math.pow(Math.E,(-Math.pow(x - mean,2)/(2*Math.pow(standard_deviation,2)))));
            return answer;
        }
        public static double KNN(int K){
            double yeah = 0;
            double nah = 0;
            for(Item i : testing){
                for(Item ii : training){
                    double distance =Math.sqrt(Math.pow(i.Number_of_times_pregnant - ii.Number_of_times_pregnant,2)
                            +Math.pow(i.Plasma_glucose_concentration_a_2_hours_in_an_oral_glucose_tolerance_test - ii.Plasma_glucose_concentration_a_2_hours_in_an_oral_glucose_tolerance_test,2)
                            +Math.pow(i.Diastolic_blood_pressure - ii.Diastolic_blood_pressure,2)
                            +Math.pow(i.Triceps_skin_fold_thickness - ii.Triceps_skin_fold_thickness,2)
                            +Math.pow(i.two_Hour_serum_insulin - ii.two_Hour_serum_insulin,2)
                            +Math.pow(i.Body_mass_index - ii.Body_mass_index,2)
                            +Math.pow(i.Diabetes_pedigree_function - ii.Diabetes_pedigree_function,2)
                            +Math.pow(i.Age - ii.Age,2)
                    );
                    i.distance_map.put(distance,ii);
                }
                List<Double> keys = new ArrayList<>();
                for(Double xxx:i.distance_map.keySet()){
                    keys.add(xxx);
                }
                Collections.sort(keys);
                List<Item> Nei = new ArrayList<>();
                for(int neighbor = 0;neighbor < K;neighbor++){
                    Nei.add(i.distance_map.get(keys.get(neighbor)));
                }
                int cnt_true = 0;
                int cnt_false = 0;
                for(Item xx:Nei){
                    if(xx.Class_variable){cnt_true ++;}
                    else{cnt_false++;}
                }
                if(cnt_true > cnt_false){
                    if(i.Class_variable){yeah++; }
                    else{nah++;}
                }
                else{
                    if(!i.Class_variable){yeah++;}
                    else{nah++;}}
            }
            return yeah/(yeah+nah);
        }
    public static double KNN_CFS(int K){
        double yeah = 0;
        double nah = 0;
        for(Item i : testing){
            for(Item ii : training){
                double distance =
                        Math.pow(i.Plasma_glucose_concentration_a_2_hours_in_an_oral_glucose_tolerance_test - ii.Plasma_glucose_concentration_a_2_hours_in_an_oral_glucose_tolerance_test,2)
                        +Math.pow(i.two_Hour_serum_insulin - ii.two_Hour_serum_insulin,2)
                        +Math.pow(i.Body_mass_index - ii.Body_mass_index,2)
                        +Math.pow(i.Diabetes_pedigree_function - ii.Diabetes_pedigree_function,2)
                        +Math.pow(i.Age - ii.Age,2);
                i.distance_map.put(distance,ii);
            }
            List<Double> keys = new ArrayList<>();
            for(Double xxx:i.distance_map.keySet()){
                keys.add(xxx);
            }
            Collections.sort(keys);
            List<Item> Nei = new ArrayList<>();
            for(int neighbor = 0;neighbor < K;neighbor++){
                Nei.add(i.distance_map.get(keys.get(neighbor)));
            }
            int cnt_true = 0;
            int cnt_false = 0;
            for(Item xx:Nei){
                if(xx.Class_variable){cnt_true ++;}
                else{cnt_false++;}
            }
            if(cnt_true > cnt_false){
                if(i.Class_variable){yeah++; }
                else{nah++;}
            }
            else{
                if(!i.Class_variable){yeah++;}
                else{nah++;}}
        }
        return yeah/(yeah+nah);
    }
        public static double NB(){
            double yeah = 0;
            double nah = 0;
            Item.no_mean_1 = Item.no_mean_1/Item.cnt_no;
            Item.no_mean_2 = Item.no_mean_2/Item.cnt_no;
            Item.no_mean_3 = Item.no_mean_3/Item.cnt_no;
            Item.no_mean_4 = Item.no_mean_4/Item.cnt_no;
            Item.no_mean_5 = Item.no_mean_5/Item.cnt_no;
            Item.no_mean_6 = Item.no_mean_6/Item.cnt_no;
            Item.no_mean_7 = Item.no_mean_7/Item.cnt_no;
            Item.no_mean_8 = Item.no_mean_8/Item.cnt_no;
            Item.yes_mean_1 = Item.yes_mean_1/Item.cnt_yes;
            Item.yes_mean_2 = Item.yes_mean_2/Item.cnt_yes;
            Item.yes_mean_3 = Item.yes_mean_3/Item.cnt_yes;
            Item.yes_mean_4 = Item.yes_mean_4/Item.cnt_yes;
            Item.yes_mean_5 = Item.yes_mean_5/Item.cnt_yes;
            Item.yes_mean_6 = Item.yes_mean_6/Item.cnt_yes;
            Item.yes_mean_7 = Item.yes_mean_7/Item.cnt_yes;
            Item.yes_mean_8 = Item.yes_mean_8/Item.cnt_yes;
            double yes_std_1 = 0;
            double yes_std_2 = 0;
            double yes_std_3 = 0;
            double yes_std_4 = 0;
            double yes_std_5 = 0;
            double yes_std_6 = 0;
            double yes_std_7 = 0;
            double yes_std_8 = 0;
            double no_std_1 = 0;
            double no_std_2 = 0;
            double no_std_3 = 0;
            double no_std_4 = 0;
            double no_std_5 = 0;
            double no_std_6 = 0;
            double no_std_7 = 0;
            double no_std_8 = 0;
            for(Item i:training){
                if(i.Class_variable){
                    yes_std_1 += Math.pow(i.Number_of_times_pregnant - Item.yes_mean_1,2);
                    yes_std_2 += Math.pow(i.Plasma_glucose_concentration_a_2_hours_in_an_oral_glucose_tolerance_test - Item.yes_mean_2,2);
                    yes_std_3 += Math.pow(i.Diastolic_blood_pressure - Item.yes_mean_3,2);
                    yes_std_4 += Math.pow(i.Triceps_skin_fold_thickness - Item.yes_mean_4,2);
                    yes_std_5 += Math.pow(i.two_Hour_serum_insulin - Item.yes_mean_5,2);
                    yes_std_6 += Math.pow(i.Body_mass_index - Item.yes_mean_6,2);
                    yes_std_7 += Math.pow(i.Diabetes_pedigree_function - Item.yes_mean_7,2);
                    yes_std_8 += Math.pow(i.Age - Item.yes_mean_8,2);
                }
                else{
                    no_std_1 += Math.pow(i.Number_of_times_pregnant - Item.no_mean_1,2);
                    no_std_2 += Math.pow(i.Plasma_glucose_concentration_a_2_hours_in_an_oral_glucose_tolerance_test - Item.no_mean_2,2);
                    no_std_3 += Math.pow(i.Diastolic_blood_pressure - Item.no_mean_3,2);
                    no_std_4 += Math.pow(i.Triceps_skin_fold_thickness - Item.no_mean_4,2);
                    no_std_5 += Math.pow(i.two_Hour_serum_insulin - Item.no_mean_5,2);
                    no_std_6 += Math.pow(i.Body_mass_index - Item.no_mean_6,2);
                    no_std_7 += Math.pow(i.Diabetes_pedigree_function - Item.no_mean_7,2);
                    no_std_8 += Math.pow(i.Age - Item.no_mean_8,2);
                }
            }
            yes_std_1 = Math.sqrt(yes_std_1/Item.cnt_yes);
            yes_std_2 = Math.sqrt(yes_std_2/Item.cnt_yes);
            yes_std_3 = Math.sqrt(yes_std_3/Item.cnt_yes);
            yes_std_4 = Math.sqrt(yes_std_4/Item.cnt_yes);
            yes_std_5 = Math.sqrt(yes_std_5/Item.cnt_yes);
            yes_std_6 = Math.sqrt(yes_std_6/Item.cnt_yes);
            yes_std_7 = Math.sqrt(yes_std_7/Item.cnt_yes);
            yes_std_8 = Math.sqrt(yes_std_8/Item.cnt_yes);
            no_std_1 = Math.sqrt(no_std_1/Item.cnt_no);
            no_std_2 = Math.sqrt(no_std_2/Item.cnt_no);
            no_std_3 = Math.sqrt(no_std_3/Item.cnt_no);
            no_std_4 = Math.sqrt(no_std_4/Item.cnt_no);
            no_std_5 = Math.sqrt(no_std_5/Item.cnt_no);
            no_std_6 = Math.sqrt(no_std_6/Item.cnt_no);
            no_std_7 = Math.sqrt(no_std_7/Item.cnt_no);
            no_std_8 = Math.sqrt(no_std_8/Item.cnt_no);
            for(Item i:testing){
                double yes_prob = PDF(i.Number_of_times_pregnant,Item.yes_mean_1,yes_std_1) *
                        PDF(i.Plasma_glucose_concentration_a_2_hours_in_an_oral_glucose_tolerance_test,Item.yes_mean_2,yes_std_2) *
                        PDF(i.Diastolic_blood_pressure,Item.yes_mean_3,yes_std_3) *
                        PDF(i.Triceps_skin_fold_thickness,Item.yes_mean_4,yes_std_4) *
                        PDF(i.two_Hour_serum_insulin,Item.yes_mean_5,yes_std_5) *
                        PDF(i.Body_mass_index,Item.yes_mean_6,yes_std_6) *
                        PDF(i.Diabetes_pedigree_function,Item.yes_mean_7,yes_std_7) *
                        PDF(i.Age,Item.yes_mean_8,yes_std_8) *
                        ((double)Item.cnt_yes/(double)training.size());
                double no_prob = PDF(i.Number_of_times_pregnant,Item.no_mean_1,no_std_1) *
                        PDF(i.Plasma_glucose_concentration_a_2_hours_in_an_oral_glucose_tolerance_test,Item.no_mean_2,no_std_2) *
                        PDF(i.Diastolic_blood_pressure,Item.no_mean_3,no_std_3) *
                        PDF(i.Triceps_skin_fold_thickness,Item.no_mean_4,no_std_4) *
                        PDF(i.two_Hour_serum_insulin,Item.no_mean_5,no_std_5) *
                        PDF(i.Body_mass_index,Item.no_mean_6,no_std_6) *
                        PDF(i.Diabetes_pedigree_function,Item.no_mean_7,no_std_7) *
                        PDF(i.Age,Item.no_mean_8,no_std_8) *
                        ((double)Item.cnt_no/(double)training.size());
                if(yes_prob > no_prob){
                    if(i.Class_variable){yeah++;
                    System.out.print("X");}
                else{nah++;System.out.print("√");}
                }
                else{
                    if(!i.Class_variable){yeah++;System.out.print("O");}
                    else{nah++;System.out.print("A");}
                }
            }
            return yeah/(nah+yeah);
        }
    public static double NB_CFS(){
        double yeah = 0;
        double nah = 0;
        Item.no_mean_2 = Item.no_mean_2/Item.cnt_no;
        Item.no_mean_5 = Item.no_mean_5/Item.cnt_no;
        Item.no_mean_6 = Item.no_mean_6/Item.cnt_no;
        Item.no_mean_7 = Item.no_mean_7/Item.cnt_no;
        Item.no_mean_8 = Item.no_mean_8/Item.cnt_no;
        Item.yes_mean_2 = Item.yes_mean_2/Item.cnt_yes;
        Item.yes_mean_5 = Item.yes_mean_5/Item.cnt_yes;
        Item.yes_mean_6 = Item.yes_mean_6/Item.cnt_yes;
        Item.yes_mean_7 = Item.yes_mean_7/Item.cnt_yes;
        Item.yes_mean_8 = Item.yes_mean_8/Item.cnt_yes;
        double yes_std_2 = 0;
        double yes_std_5 = 0;
        double yes_std_6 = 0;
        double yes_std_7 = 0;
        double yes_std_8 = 0;
        double no_std_2 = 0;
        double no_std_5 = 0;
        double no_std_6 = 0;
        double no_std_7 = 0;
        double no_std_8 = 0;
        for(Item i:training){
            if(i.Class_variable){
                yes_std_2 += Math.pow(i.Plasma_glucose_concentration_a_2_hours_in_an_oral_glucose_tolerance_test - Item.yes_mean_2,2);
                yes_std_5 += Math.pow(i.two_Hour_serum_insulin - Item.yes_mean_5,2);
                yes_std_6 += Math.pow(i.Body_mass_index - Item.yes_mean_6,2);
                yes_std_7 += Math.pow(i.Diabetes_pedigree_function - Item.yes_mean_7,2);
                yes_std_8 += Math.pow(i.Age - Item.yes_mean_8,2);
            }
            else{
                no_std_2 += Math.pow(i.Plasma_glucose_concentration_a_2_hours_in_an_oral_glucose_tolerance_test - Item.no_mean_2,2);
                no_std_5 += Math.pow(i.two_Hour_serum_insulin - Item.no_mean_5,2);
                no_std_6 += Math.pow(i.Body_mass_index - Item.no_mean_6,2);
                no_std_7 += Math.pow(i.Diabetes_pedigree_function - Item.no_mean_7,2);
                no_std_8 += Math.pow(i.Age - Item.no_mean_8,2);
            }
        }
        yes_std_2 = Math.sqrt(yes_std_2/Item.cnt_yes);
        yes_std_5 = Math.sqrt(yes_std_5/Item.cnt_yes);
        yes_std_6 = Math.sqrt(yes_std_6/Item.cnt_yes);
        yes_std_7 = Math.sqrt(yes_std_7/Item.cnt_yes);
        yes_std_8 = Math.sqrt(yes_std_8/Item.cnt_yes);
        no_std_2 = Math.sqrt(no_std_2/Item.cnt_no);
        no_std_5 = Math.sqrt(no_std_5/Item.cnt_no);
        no_std_6 = Math.sqrt(no_std_6/Item.cnt_no);
        no_std_7 = Math.sqrt(no_std_7/Item.cnt_no);
        no_std_8 = Math.sqrt(no_std_8/Item.cnt_no);
        for(Item i:testing){
            double yes_prob =
                    PDF(i.Plasma_glucose_concentration_a_2_hours_in_an_oral_glucose_tolerance_test,Item.yes_mean_2,yes_std_2) *
                    PDF(i.two_Hour_serum_insulin,Item.yes_mean_5,yes_std_5) *
                    PDF(i.Body_mass_index,Item.yes_mean_6,yes_std_6) *
                    PDF(i.Diabetes_pedigree_function,Item.yes_mean_7,yes_std_7) *
                    PDF(i.Age,Item.yes_mean_8,yes_std_8) *
                    ((double)Item.cnt_yes/(double)training.size());
            double no_prob =
                    PDF(i.Plasma_glucose_concentration_a_2_hours_in_an_oral_glucose_tolerance_test,Item.no_mean_2,no_std_2) *
                    PDF(i.two_Hour_serum_insulin,Item.no_mean_5,no_std_5) *
                    PDF(i.Body_mass_index,Item.no_mean_6,no_std_6) *
                    PDF(i.Diabetes_pedigree_function,Item.no_mean_7,no_std_7) *
                    PDF(i.Age,Item.no_mean_8,no_std_8) *
                    ((double)Item.cnt_no/(double)training.size());
            if(yes_prob > no_prob){
                if(i.Class_variable){yeah++; }
                else{nah++;}
            }
            else{
                if(!i.Class_variable){yeah++;}
                else{nah++;}
            }
        }
        return yeah/(nah+yeah);
    }
        public static void main(String[] args) throws IOException {
            double accuracy = 0;
            for(int i = 1;i<=10;i++){
                Read_File_CFS("E:\\comp3308\\ass2\\MyClassifier\\src\\pima-folds.csv",i);
                accuracy +=NB_CFS();
                System.out.println(accuracy);
                testing = new ArrayList<>();
                training = new ArrayList<>();
                Item.yes_mean_1 =0;
                Item.yes_mean_2 =0;
                Item.yes_mean_3 =0;
                Item.yes_mean_4 =0;
                Item.yes_mean_5 =0;
                Item.yes_mean_6 =0;
                Item.yes_mean_7 =0;
                Item.yes_mean_8 =0;
                Item.no_mean_1 =0;
                Item.no_mean_2 =0;
                Item.no_mean_3 =0;
                Item.no_mean_4 =0;
                Item.no_mean_5 =0;
                Item.no_mean_6 =0;
                Item.no_mean_7 =0;
                Item.no_mean_8 =0;
                Item.cnt_yes = 0;
                Item.cnt_no = 0;
            }
            System.out.println(accuracy/10);
        }
    }
