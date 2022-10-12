import java.util.ArrayList;
import java.util.HashMap;

public class Node {
    public String name;
    public ArrayList<Node> prev;
    public ArrayList<Node> next;
    public static HashMap<String,Node> map = new HashMap<>();
    public Node(String name){
        this.name = name;
        next = new ArrayList<>();
        prev = new ArrayList<>();
        map.put(name,this);
    }
}
