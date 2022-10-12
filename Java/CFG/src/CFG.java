import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class CFG {
    public ArrayList<String> variables;
    public ArrayList<String> terminals;
    public String start;
    public ArrayList<String> waitList = new ArrayList<>();
    public ArrayList<Rule> rules;
    public ArrayList<String> expanded = new ArrayList<>();
    public CFG(ArrayList<String> variables,ArrayList<String> terminals,String start,ArrayList<Rule> rules){
        this.variables = variables;
        this.terminals = terminals;
        this.start = start;
        this.rules = rules;
        this.waitList.add(start);
    }
    public String format(String test) {
        StringBuilder builder = new StringBuilder();
        for (char i : test.toCharArray()) {
            ArrayList<String> found = Rule.reverseSearch(Character.toString(i), rules);
            builder.append(found.get(0));
        }
        return builder.toString();
    }
    public void newMembership(String test){
        ArrayList<String> expanded = new ArrayList<>();
        ArrayList<Node> found = new ArrayList<>();
        Node a  = new Node(test);
        Rule.reverseRightMost(this.start,expanded,found,a,0,rules);
        if(found.size()==0){
            System.out.println(0);
        }
        else{
            System.out.println(1);
        }
    }
    public void membership(String test){
        ArrayList<String> list = new ArrayList<>();
        for(String node:waitList){
            if(node.equals(test)){
                System.out.println("1");
                return;
            }
            expanded.add(node);
            ArrayList<String> expand = Rule.findAllMatching(node,rules);
            for(String i:expand){
                if(!expanded.contains(i)&&
                        i.length()<=test.length()&&
                        !list.contains(i)){
                    list.add(i);
                }
            }
        }
        this.waitList = list;
        if(waitList.size()==0){
            System.out.println(0);
            return;
        }
        else{
            membership(test);
        }
    }
    public void ambiguityTest(String test){
        ArrayList<String> expanded = new ArrayList<>();
        ArrayList<Node> found = new ArrayList<>();
        Node a  = new Node(test);
        Rule.reverseRightMost(this.start,expanded,found,a,0,rules);
        boolean kk = true;
        if(found.size()>1){
            System.out.println(1);
        }
        else if(found.isEmpty()){
            System.out.println(0);
        }
        else{
            Node i = found.get(0);
            while(!i.prev.isEmpty()){
                if(i.prev.size()>1){
                    boolean same = false;
                    String name = i.prev.get(0).name;
                    for(int z = 1;z<i.prev.size();z++){
                        if(i.prev.get(z).name.equals(name)){
                            continue;
                        }
                        else{
                            same = true;
                            break;
                        }
                    }
                    if(same){
                        System.out.println(1);
                        kk = false;
                    }
                }
                else{
                    i = i.prev.get(0);
                }
            }
            if(kk){
                System.out.println(0);
            }
        }
    }
    public void rightMost(String test){
        Node start = new Node(this.start);
        ArrayList<String> results = new ArrayList<>();
        ArrayList<Node> found =  new ArrayList<>();
        Rule.deriveRight(results,found,start,test,0,rules);
        Node find = found.get(0);
        ArrayList<String> list = new ArrayList<>();
        list.add(find.name);
        while(!find.prev.isEmpty()){
            list.add(find.prev.get(0).name);
            find = find.prev.get(0);
        }
        Collections.reverse(list);
        for(String i:list){
            System.out.println(i);
        }
    }
}
