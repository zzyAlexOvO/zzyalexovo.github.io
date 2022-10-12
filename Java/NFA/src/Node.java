import java.util.ArrayList;
import java.util.HashMap;

public class Node {
    public ArrayList<Node> sons = new ArrayList<>();
    public ArrayList<Node> withE = new ArrayList<>();
    public ArrayList<Node> withoutE = new ArrayList<>();
    public String name;
    public Node(String name){
        this.name = name;
        this.withE.add(this);
    }
    public void init(Model model){
        for(Delta i:model.delta){
            if(i.from.equals(this)){
                if(!this.sons.contains(i.to)){
                    this.sons.add(i.to);
                }
                if(i.by.length()==0){
                    if(!this.withE.contains(i.to)) {
                        this.withE.add(i.to);
                    }
                }
                else{
                    this.withoutE.add(i.to);
                }
            }
        }
    }
    public void secInit(Node origin,Model model){
        for(Delta i:model.delta){
            if(i.from.equals(this) && i.by.length()==0){
                if(!origin.withE.contains(i.to)){
                    origin.withE.add(i.to);
                    i.to.secInit(origin,model);
                }
            }
        }
    }
}
