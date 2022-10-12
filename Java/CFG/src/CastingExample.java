import java.util.List;
import java.util.Map;

public class CastingExample {
    /*
     * just a bit of example code to demonstrate casting the objects
     * don't actually use this...
     */
    /*
    public static void main(String[] args) {

        Parser parser = new Parser();
        String command = parser.parseCommand();
        CFG cfg = parser.parseCfg();

        @SuppressWarnings("unchecked")
        List<String> variables = (List<String>)cfg.get("variables");
        System.out.println(variables);

        @SuppressWarnings("unchecked")
        List<String> terminals = (List<String>)cfg.get("terminals");
        System.out.println(terminals);

        @SuppressWarnings("unchecked")
        String start = (String)cfg.get("start");
        System.out.println(start);

        @SuppressWarnings("unchecked")
        List<List<Object>> rules = (List<List<Object>>)cfg.get("rules");
        for(List<Object> rule : rules) {
            @SuppressWarnings("unchecked")
            String v = (String)rule.get(0);
            @SuppressWarnings("unchecked")
            List<String> alpha = (List<String>)rule.get(1);
            System.out.print(v);
            System.out.print(" -> ");
            System.out.println(alpha);
        }
    }*/
}