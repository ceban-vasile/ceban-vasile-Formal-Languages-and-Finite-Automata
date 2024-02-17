package Laboratory_work_1.Regular_Grammars_Task;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Set<String> VN = new HashSet<>(Arrays.asList("S", "R", "L"));
        Set<String> VT = new HashSet<>(Arrays.asList("a", "b", "c", "d", "e", "f"));

        Map<String, List<String>> P = new HashMap<>();
        P.put("S", Arrays.asList("aS", "bS", "cR", "dL"));
        P.put("R", Arrays.asList("dL", "e"));
        P.put("L", Arrays.asList("fL", "eL", "d"));

        String startSymbol = "S";

        Grammar grammar = new Grammar(VN, VT, P, startSymbol);

        for (int i = 0; i < 5; i++) {
            String generatedString = grammar.generateString();
            System.out.println("Generated String " + (i + 1) + ": " + generatedString);
        }

        FiniteAutomaton finiteAutomaton = grammar.toFiniteAutomaton();
    }
}
