import java.util.ArrayList;

public class Rule {
    public String from;
    public String to;
    public Rule(String from,String to){
        this.from = from;
        this.to = to;
    }
    public static ArrayList<String> findMatching(String node,ArrayList<Rule> rules){
        ArrayList<String> result = new ArrayList<>();
        for(Rule r : rules){
            if(r.from.equals(node)){
                result.add(r.to);
            }
        }
        return result;
    }
    public static ArrayList<String> findAllMatching(String node,ArrayList<Rule> rules){
        if(node.length() == 1){
            return findMatching(node,rules);
        }
        else{
            ArrayList<String> result = new ArrayList<>();
            for (int i = 0; i < node.toCharArray().length;i++){
                ArrayList<String> merge = findMatching(Character.toString(node.charAt(i)),rules);
                char[] charArray = node.toCharArray();
                for(int a = 0;a<merge.size();a++){
                    StringBuilder builder = new StringBuilder();
                    int cnt = 0;
                    for(char ss:charArray){
                        if(cnt == i){
                            builder.append(merge.get(a));
                        }
                        else {
                            builder.append(ss);
                        }
                        cnt++;
                    }
                    result.add(builder.toString());
                }
            }
            return result;
        }
    }
    public static void deriveRight(ArrayList<String> expanded,ArrayList<Node> found,Node node,String test,int i,ArrayList<Rule> rules){
        if (node.name.equals(test)){
            found.add(node);
            return;
        }else if(i<0) {
            return;
        }
        else if(node.name.length()>test.length()){
            return;
        }
        ArrayList<String> match = findMatching(Character.toString(node.name.charAt(i)),rules);
        if(match.isEmpty()){
            i--;
            deriveRight(expanded,found,node,test,i,rules);
        }
        else{
            String left = node.name.substring(0,i);
            String right = node.name.substring(i+1);
            for(String a:match) {
                String name = left + a + right;
                if(!expanded.contains(name)) {
                    Node newNode = new Node(name);
                    node.next.add(newNode);
                    newNode.prev.add(node);
                    expanded.add(name);
                    deriveRight(expanded, found, newNode,test,newNode.name.length()-1, rules);
                }
            }
        }
        return;


    }
    public static ArrayList<String> reverseSearch(String node, ArrayList<Rule> rules){
        ArrayList<String> result = new ArrayList<>();
        for(Rule r : rules){
            if(r.to.equals(node)){
                result.add(r.from);
            }
        }
        return result;
    }
    public static void reverseRightMost(String start,ArrayList<String> expanded,ArrayList<Node> found,Node node,int i,ArrayList<Rule> rules){
        if (node.name.equals(start)){
            found.add(node);
            return;
        }else if(node.name.length()-2-i<0) {
            return;
        }
        String sub;
        if(i==0){
            sub = node.name.substring(node.name.length()-2);
        }
        else{
            sub = node.name.substring(node.name.length()-2-i,node.name.length()-i);
        }
        ArrayList<String> match = reverseSearch(sub,rules);
        if(match.isEmpty()){
        }
        else{
            String left = node.name.substring(0,node.name.length()-2-i);
            String right = node.name.substring(node.name.length()-i);
            for(String a:match) {
                String name = left + a + right;
                    Node newNode = new Node(name);
                    node.next.add(newNode);
                    newNode.prev.add(node);
                    expanded.add(name);
                    reverseRightMost(start,expanded, found, newNode, 0, rules);
            }
        }
        i++;
        reverseRightMost(start,expanded,found,node,i,rules);
        return;
    }

    public static void main(String[] args){
        ArrayList<Rule> rules = new ArrayList<>();
        rules.add(new Rule("T","AB"));
        rules.add(new Rule("T","CD"));
        rules.add(new Rule("S","CD"));
        rules.add(new Rule("S","SS"));
        rules.add(new Rule("S","AB"));
        rules.add(new Rule("T","SS"));
        rules.add(new Rule("A","a"));
        rules.add(new Rule("B","b"));
        ArrayList<String> results = new ArrayList<>();
        Node T = new Node("ABAB");
        ArrayList<Node> found =  new ArrayList<>();
        reverseRightMost("T",results,found,T,0,rules);
        Node find = found.get(0);

    }
}
