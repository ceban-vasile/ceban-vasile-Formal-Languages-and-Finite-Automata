package Laboratory_work_1.Finite_Automata_Task;

import Laboratory_work_1.Finite_Automata_Task.FiniteAutomaton;
import Laboratory_work_1.Finite_Automata_Task.Grammar;

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

        // a. Classify the grammar based on Chomsky hierarchy
        String grammarClassification = grammar.classifyGrammar();
        System.out.println("Grammar Classification: " + grammarClassification);

        // b. Convert Finite Automaton to Regular Grammar
        Grammar regularGrammar = grammar.convertToRegularGrammar();

        // c. Check if the Finite Automaton is deterministic
        boolean isDeterministic = grammar.isDeterministic();
        System.out.println("Is Deterministic: " + isDeterministic);

        // d. Convert NDFA to DFA
        FiniteAutomaton dfa = grammar.convertToDFA();
    }
}