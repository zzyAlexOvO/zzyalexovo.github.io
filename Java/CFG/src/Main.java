import java.util.List;
import java.util.Map;

/**
 * This module is the entry point to your assignment. There is some scaffolding
 * to help you get started. It will call the appropriate method and load the
 * input data. You can edit or remove as much of this code as you wish to.
 */

public class Main {

    /*
     * For each string, decide if it is in the language.
     */
    public static void membership(Parser parser) {
        CFG cfg = parser.parseCfg();
        List<String> testStrings = parser.parseTestStrings();
        for(String i:testStrings){
            CFG using = new CFG(cfg.variables,cfg.terminals,cfg.start,cfg.rules);
            String test = using.format(i);
            using.newMembership(test);
        }
        System.out.println("end");
    }

    /*
     * Give a rightmost derivation of the string.
     */
    public static void rightmostDerivation(Parser parser) {
        CFG cfg = parser.parseCfg();
        List<String> testStrings = parser.parseTestStrings();
        for(String i:testStrings){
            CFG using = new CFG(cfg.variables,cfg.terminals,cfg.start,cfg.rules);
            using.rightMost(i);
        }
        System.out.println("end");
    }

    /*
     * For each string, decide if it is in the language.
     */
    public static void ambiguous(Parser parser) {
        CFG cfg = parser.parseCfg();
        List<String> testStrings = parser.parseTestStrings();
        for(String i:testStrings){
            CFG using = new CFG(cfg.variables,cfg.terminals,cfg.start,cfg.rules);
            String test = using.format(i);
            using.ambiguityTest(test);
        }
        System.out.println("end");
    }

    /**
     * @param args
     */
    public static void main(String[] args) {

        Parser parser = new Parser();

        String command = parser.parseCommand();
        switch (command) {
            case "membership":
                membership(parser);
                break;
            case "rightmost-derivation":
                rightmostDerivation(parser);
                break;
            case "ambiguous":
                ambiguous(parser);
                break;
            default:
                System.out.println(String.format("Command %s not recognised.", command));
        }
    }
}