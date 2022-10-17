package app.model;


public class ThresholdListener {
    private static int threshold;
    public static void setThreshold(int num){
        threshold = num;
    }
    public boolean analyse(int comics){
        return comics > threshold;
    }
}
