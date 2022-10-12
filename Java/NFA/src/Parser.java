import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * This class is a very simple parser to help you read the input files. You
 * don't need to edit this file, but you can if you want. You can even delete
 * it, if you'd prefer to write your own parsing functions.
 */

public class Parser {

    Scanner scanner;

    public Parser() {
        scanner = new Scanner(System.in);
    }

    /**
     * @return the next line from the stream
     */
    public String readLine() {
        return scanner.nextLine().replaceAll("\\s+", "");
    }

    /**
     * @return a list of lines, up to but excluding the next "end"
     */
    public List<String> readSection() {
        ArrayList<String> lines = new ArrayList<String>();
        while (true) {
            String line = scanner.nextLine();
            if (line.equals("end")) {
                break;
            }
            lines.add(line.replaceAll("\\s+", ""));
        }
        return lines;
    }

    /**
     * @return the next line from the stream
     */
    public String parseCommand() {
        return readLine();
    }

    /**
     * Read from the stream, return a dictionary representing the nfa/dfa.
     *
     * key 'state' gives the set of states (as a List<String>)
     * key 'alphabet' gives the set of symbols (as a List<String>)
     * key 'start' gives the label of the start state (as a String)
     * key 'final' gives the set of final states (as a List<String>)
     * key 'delta' gives a list of (s, c, t) tuples (as a List<List<String>>)
     *
     * You will need to cast the values back to the correct types.
     *
     * This is not a very efficient representation of a FA, you will want to use
     * this data to construct something more useful.
     *
     * @return
     */
    public Model parseFa() {
        Model fa = new Model();
        List<String> states = Arrays.asList(readLine().split(","));
        for(String i:states){
            fa.states.put(i,new Node(i));
        }
        fa.alphabet = new ArrayList<>(Arrays.asList(readLine().split(",")));
        fa.start = fa.states.get(readLine());
        for(String i:Arrays.asList(readLine().split(","))){
            fa.finals.add(fa.states.get(i));
        }
        for (String line : readSection()) {
            List<String> temp = Arrays.asList(line.split(","));
            fa.delta.add(new Delta(fa.states.get(temp.get(0)),temp.get(1),fa.states.get(temp.get(2))));
        }
        return fa;
    }

    /**
     * of states reachable from the key, using 0 or more epsilon transitions.
     */
    public void parseClosures(Model model) {
        for (String line : readSection()) {
            String[] closure = line.split(":");
            String key = closure[0];
            for(String i:Arrays.asList(closure[1].split(","))){
                if(!model.states.get(key).sons.contains(model.states.get(i))){
                    model.states.get(key).sons.add(model.states.get(i));
                }
            }
        }
    }

    /**
     * @return a list of strings (to be tested)
     */
    public List<String> parseTestStrings() {
        return readSection();
    }
}