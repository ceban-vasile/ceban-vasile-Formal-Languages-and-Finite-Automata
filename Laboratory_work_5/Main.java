package Laboratory_work_5;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Grammar grammar = new Grammar();
        Set<String> characters = new HashSet<>(List.of("S", "A", "B", "C", "D"));
        grammar.setNonTerminals(characters);
        characters = new HashSet<>(List.of("a", "b"));
        grammar.setTerminals(characters);
        grammar.setStartSymbol("S");
        HashMap<String, List<String>> rules = new HashMap<>();
        rules.put("S", List.of("aB", "bA"));
        rules.put("A", List.of("B", "b", "aD", "AS", "bAAB", ""));
        rules.put("B", List.of("b", "bS"));
        rules.put("C", List.of("AB"));
        rules.put("D", List.of("BB"));
        grammar.setRules(rules);

        CNFConverter cnfconverter = new CNFConverter(grammar);

        // Display the CNF grammar
        System.out.println("Chomsky Normal Form Grammar:");
        cnfconverter.displayCNF();
    }
}
