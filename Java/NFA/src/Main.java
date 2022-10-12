import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This module is the entry point to your assignment. There is some scaffolding
 * to help you get started. It will call the appropriate method (task_1, 2 etc.)
 * and load the input data. You can edit or remove as much of this code as you
 * wish to.
 */

public class Main {

    /*
     * For each state of the NFA, compute the Epsilon closure and output it as a
     * line of the form s:a,b,c where s is the state, and {a,b,c} is E(s)
     */
    public static void task1(Parser parser) {
        Model nfa = parser.parseFa();
        for(Node i:nfa.states.values()){
            i.secInit(i,nfa);
            System.out.print(i.name+":");
            for(int a = 0;a<i.withE.size();a++){
                if(a == i.withE.size()-1){
                    System.out.println(i.withE.get(a).name);
                }
                else{
                    System.out.print(i.withE.get(a).name+",");
                }
            }
        }
        System.out.print("end");
    }

    /*
     * Construct and output an equivalent Epsilon free NFA. The state names should
     * not change.
     */
    public static void task2(Parser parser) {
        Model nfa = parser.parseFa();
        for(Node i:nfa.states.values()){
            i.secInit(i,nfa);
        }
        parser.parseClosures(nfa);
        ArrayList<Delta> des = new ArrayList<>();
        ArrayList<Node> newFinals = new ArrayList<>(nfa.finals);
        for(Node i:nfa.states.values()){
            if(!nfa.finals.contains(i)){
                for(Node ii:i.withE){
                    if(nfa.finals.contains(ii)){
                        newFinals.add(i);
                    }
                }
            }
        }
        for(Delta i:nfa.delta){
            i.transit(nfa,des,i);
        }

        nfa.finals = newFinals;
        nfa.delta = des;
        nfa.output();
    }

    /*
     * Construct and output an equivalent DFA. The input is guaranteed to be an
     * Epsilon Free NFA.
     */
    public static void task3(Parser parser) {
        Model efnfa = parser.parseFa();
        Model dfa = new Model();
        dfa.alphabet = new ArrayList<>(efnfa.alphabet);
        dfa.start = efnfa.start;
        dfa.delta = efnfa.delta;
        ArrayList<String> newDelta = new ArrayList<>();
        dfa.find_all(dfa.states,newDelta,dfa.start);
        dfa.delta = new ArrayList<>();
        for(String i:newDelta){
            String[] a = i.split(",");
            dfa.delta.add(new Delta(dfa.states.get(a[0]),a[1],dfa.states.get(a[2])));
        }
        ArrayList<Node> newFinals = new ArrayList<>(efnfa.finals);
        for(Node i:efnfa.finals){
            if(!dfa.states.containsValue(i)){
                newFinals.remove(i);
            }
        }
        dfa.finals = newFinals;
        dfa.output();


    }

    /*
     * For each string, output 1 if the DFA accepts it, 0 otherwise. The input is
     * guaranteed to be a DFA.
     */
    public static void task4(Parser parser) {
        Model dfa = parser.parseFa();
        List<String> testStrings = parser.parseTestStrings();
        for(String i:testStrings){
            Node current = dfa.start;
            for(int cnt = 0;cnt<i.length();cnt++){
                String pattern = Character.toString(i.charAt(cnt));
                current=dfa.find_next(current,pattern);
            }
            if(dfa.finals.contains(current)){
                System.out.println(1);
            }
            else{
                System.out.println(0);
            }
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {

        Parser parser = new Parser();

        String command = parser.parseCommand();
        switch (command) {
            case "epsilon-closure":
                task1(parser);
                break;
            case "nfa-to-efnfa":
                task2(parser);
                break;
            case "efnfa-to-dfa":
                task3(parser);
                break;
            case "compute-dfa":
                task4(parser);
                break;
            default:
                System.out.println(String.format("Command %s not recognised.", command));
        }
    }
}
