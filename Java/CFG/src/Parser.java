import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
        return scanner.nextLine().trim();
    }

    /**
     * @return a list of lines, up to but excluding the next "end"
     */
    public List<String> readSection() {
        ArrayList<String> lines = new ArrayList<String>();
        while (true) {
            String line = scanner.nextLine().trim();
            if (line.equals("end")) {
                break;
            }
            lines.add(line);
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
     * key 'variables' gives the set of variables (as a List<String>)
     * key 'terminals' gives the set of terminals (as a List<String>)
     * key 'start' gives the label of the start variable (as a String)
     * key 'rules' gives a list of (V, production) tuples, where production is a
     *    list (as a List<Object>, where element 0 is a String and element 1 is
     *    a List<String>)
     *
     * You will need to cast the values back to the correct types.
     *
     * You will want use this data to construct more suitable data structures.
     */
    //Override
    public CFG parseCfg() {
        ArrayList<String> variables = new ArrayList<>(Arrays.asList(readLine().split(",")));
        ArrayList<String> terminals = new ArrayList<>(Arrays.asList(readLine().split(",")));
        String start = readLine();
        ArrayList<Rule> rules = new ArrayList<>();
        for (String line : readSection()) {
            String variable = line.split("->")[0].trim();
            String alpha = line.split("->")[1].trim();
            String to = alpha.replaceAll(" ","");
            if(to.equals("epsilon")){
                to = "";
            }
            Rule rule = new Rule(variable,to);
            rules.add(rule);
        }
        CFG cfg = new CFG(variables,terminals,start,rules);
        return cfg;
    }

    /**
     * @return a list of strings (to be tested)
     */
    public List<String> parseTestStrings() {
        return readSection();
    }

    /**
     * @return a single string (to be tested)
     */
    public String parseTestString() {
        return readSection().get(0);
    }
}