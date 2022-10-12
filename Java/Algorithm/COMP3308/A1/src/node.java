import java.util.ArrayList;
import java.util.List;

public class node {
    public String value;
    public node father;
    public int cant;
    public List<node> kids = new ArrayList<>();
    public int s1;
    public int s2;
    public int s3;
    public int cost;
    public static List<node> expanded_list = new ArrayList<>();
    public static List<node> await_list = new ArrayList<>();
    public static List<String> forbid = new ArrayList<>();

    public node(String value, node father){
        this.value = value;
        this.father = father;
        this.cant = Math.abs(Integer.parseInt(this.value) - Integer.parseInt(this.father.value));
        this.s1 = Character.getNumericValue((this.value).charAt(0));
        this.s2 = Character.getNumericValue((this.value).charAt(1));
        this.s3 = Character.getNumericValue((this.value).charAt(2));
    }
    public node(String value){
        this.value = value;
        this.cant = 0;
        this.s1 = Character.getNumericValue((this.value).charAt(0));
        this.s2 = Character.getNumericValue((this.value).charAt(1));
        this.s3 = Character.getNumericValue((this.value).charAt(2));
    }
    public static void getKids(node N){
        if(N.kids.isEmpty()){


        String result1 = "";
        String result2 = "";
        String result3= "";
        String result4= "";
        String result5= "";
        String result6= "";

        if(N.s1 != 0){result1 = String.format("%03d",Integer.parseInt(Integer.toString((N.s1 - 1)) + Integer.toString(N.s2)  + Integer.toString(N.s3)));}
        if(N.s1 != 9){result2 = String.format("%03d",Integer.parseInt(Integer.toString((N.s1 + 1)) + Integer.toString(N.s2)  + Integer.toString(N.s3)));}
        if(N.s2 != 0){result3 = String.format("%03d",Integer.parseInt(Integer.toString(N.s1) + Integer.toString((N.s2 - 1)) + Integer.toString(N.s3)));}
        if(N.s2 != 9){result4 = String.format("%03d",Integer.parseInt(Integer.toString(N.s1) + Integer.toString((N.s2 + 1)) + Integer.toString(N.s3)));}
        if(N.s3 != 0){result5 = String.format("%03d",Integer.parseInt(Integer.toString(N.s1) + Integer.toString(N.s2) + Integer.toString((N.s3-1))));}
        if(N.s3 != 9){result6 = String.format("%03d",Integer.parseInt(Integer.toString(N.s1) + Integer.toString(N.s2) + Integer.toString((N.s3+1))));}

        if(N.cant == 0){
            if(N.s1 == 9) {
                node x = new node(result1,N);
                N.kids.add(x);
            }
            else if(N.s1 ==  0){
                node x = new node(result2,N);
                N.kids.add(x);
            }
            else {
                node x = new node(result1,N);
                node xx = new node(result2,N);
                N.kids.add(x);
                N.kids.add(xx);
            }
            if(N.s2 == 9) {
                node x = new node(result3,N);
                N.kids.add(x);
            }
            else if(N.s2 ==  0){
                node x = new node(result4,N);
                N.kids.add(x);
            }
            else {
                node x = new node(result3,N);
                node xx = new node(result4,N);
                N.kids.add(x);
                N.kids.add(xx);
            }
            if(N.s3 == 9) {
                node x = new node(result5,N);
                N.kids.add(x);
            }
            else if(N.s3 ==  0){
                node x = new node(result6,N);
                N.kids.add(x);
            }
            else {
                node x = new node(result5,N);
                node xx = new node(result6,N);
                N.kids.add(x);
                N.kids.add(xx);
            }
        }
        else if(N.cant == 100){
            if(N.s2 == 9) {
                node x = new node(result3,N);
                N.kids.add(x);
            }
            else if(N.s2 == 0){
                node x = new node(result4,N);
                N.kids.add(x);
            }
            else {
                node x = new node(result3,N);
                node xx = new node(result4,N);
                N.kids.add(x);
                N.kids.add(xx);
            }
            if(N.s3 == 9) {
                node x = new node(result5,N);
                N.kids.add(x);
            }
            else if(N.s3 ==  0){
                node x = new node(result6,N);
                N.kids.add(x);
            }
            else {
                node x = new node(result5,N);
                node xx = new node(result6,N);
                N.kids.add(x);
                N.kids.add(xx);
            }
        }
        else if(N.cant == 10){
            if(N.s1 == 9) {
                node x = new node(result1,N);
                N.kids.add(x);
            }
            else if(N.s1 ==  0){
                node x = new node(result2,N);
                N.kids.add(x);
            }
            else {
                node x = new node(result1,N);
                node xx = new node(result2,N);
                N.kids.add(x);
                N.kids.add(xx);
            }
            if(N.s3 == 9) {
                node x = new node(result5,N);
                N.kids.add(x);
            }
            else if(N.s3 ==  0){
                node x = new node(result6,N);
                N.kids.add(x);
            }
            else {
                node x = new node(result5,N);
                node xx = new node(result6,N);
                N.kids.add(x);
                N.kids.add(xx);
            }
        }
        else if(N.cant == 1){
            if(N.s1 == 9) {
                node x = new node(result1,N);
                N.kids.add(x);
            }
            else if(N.s1 ==  0){
                node x = new node(result2,N);
                N.kids.add(x);
            }
            else {
                node x = new node(result1,N);
                node xx = new node(result2,N);
                N.kids.add(x);
                N.kids.add(xx);
            }
            if(N.s2 == 9) {
                node x = new node(result3,N);
                N.kids.add(x);
            }
            else if(N.s2 ==  0){
                node x = new node(result4,N);
                N.kids.add(x);
            }
            else {
                node x = new node(result3,N);
                node xx = new node(result4,N);
                N.kids.add(x);
                N.kids.add(xx);
            }
            }
        }
    }
}
