import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Model {
    public HashMap<String,Node> states = new HashMap<>();
    public ArrayList<String> alphabet = new ArrayList<>();
    public Node start;
    public ArrayList<Node> finals = new ArrayList<>();
    public ArrayList<Delta> delta = new ArrayList<>();

    public Node find_next(Node from,String pattern){
        for(Delta i:delta){
            if(i.from.equals(from)&&i.by.equals(pattern)){
                return i.to;
            }
        }
        System.out.println("?");
        return null;
    }

    public void find_all(HashMap<String,Node> newStates,ArrayList<String> newDelta,Node current){
        if(newStates.containsValue(current)){
            return;
        }
        else{
            newStates.put(current.name,current);
        }
        ArrayList<Delta> mine = new ArrayList<>();
        HashMap<String,ArrayList<Node>> nodes= new HashMap<>();
        for(String i:alphabet){
            nodes.put(i,new ArrayList<>());
        }
        for(Delta i:delta){
            if(i.from.equals(current)){
                mine.add(i);
            }
        }
        for(Delta i:mine){
            nodes.get(i.by).add(i.to);
        }
        for(String i:alphabet){
            if(nodes.get(i).size() ==1){
                newDelta.add(current.name+","+i+","+nodes.get(i).get(0).name);
                find_all(newStates,newDelta,nodes.get(i).get(0));
            }
            else if(nodes.get(i).size()==0){
                newDelta.add(current.name+","+i+","+"s");
                if(!newStates.containsKey("s")){
                    find_all(newStates,newDelta,new Node("s"));
                }
            }
            else{
                StringBuilder builder = new StringBuilder();
                for(Node node:nodes.get(i)){
                    builder.append(node.name);
                }
                newDelta.add(current.name+","+i+","+builder.toString());
                if(!newStates.containsKey(builder.toString())){
                    Node a  = new Node(builder.toString());
                    find_all(newStates,newDelta,a);
                }
            }
        }
    }

    public void output(){
        for(int i = 0;i<this.states.size();i++){
            if(i==this.states.size()-1){
                System.out.println((this.states.keySet()).toArray()[i]);
            }
            else{
                System.out.print((this.states.keySet()).toArray()[i]+",");
            }
        }
        for(int i = 0;i<this.alphabet.size();i++){
            if(i==this.alphabet.size()-1){
                System.out.println(this.alphabet.get(i));
            }
            else{
                System.out.print(this.alphabet.get(i)+",");
            }
        }
        System.out.println(this.start.name);
        for(int i = 0;i<this.finals.size();i++){
            if(i==this.finals.size()-1){
                System.out.println(this.finals.get(i).name);
            }
            else{
                System.out.print(this.finals.get(i).name+",");
            }
        }
        for(Delta i:this.delta){
            System.out.println(i.from.name+","+i.by+","+i.to.name);
        }
        System.out.println("end");
    }
}
