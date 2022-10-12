import java.util.ArrayList;

public class Delta {
    public Node from;
    public Node to;
    public String by;
    public Delta(Node from,String by,Node to){
        this.from = from;
        this.by = by;
        this.to = to;
    }
    public void transit(Model model,ArrayList<Delta> des,Delta x){
        if(this.by.length() == 0){
            for(Delta i : model.delta){
                if(i.from.equals(this.to)){
                    i.transit(model,des,x);
                }
            }
        }
        else{
            des.add(new Delta(x.from,this.by,this.to));
        }
    }
}
