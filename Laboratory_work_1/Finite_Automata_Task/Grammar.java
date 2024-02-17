package Laboratory_work_1.Finite_Automata_Task;

import java.util.*;

class Grammar {
    private Set<String> VN;
    private Set<String> VT;
    private Map<String, List<String>> P;
    private String startSymbol;

    public Grammar(Set<String> VN, Set<String> VT, Map<String, List<String>> P, String startSymbol) {
        this.VN = VN;
        this.VT = VT;
        this.P = P;
        this.startSymbol = startSymbol;
    }

    public String generateString() {
        return generateStringHelper(startSymbol);
    }

    private String generateStringHelper(String symbol) {
        if (VT.contains(symbol)) {
            return symbol;
        }

        List<String> productions = P.get(symbol);
        String chosenProduction = productions.get(new Random().nextInt(productions.size()));

        StringBuilder result = new StringBuilder();
        for (char c : chosenProduction.toCharArray()) {
            if (Character.isUpperCase(c)) {
                result.append(generateStringHelper(String.valueOf(c)));
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }

    public String classifyGrammar() {
        return "Type 3 - Regular Grammar";
    }

    public FiniteAutomaton toFiniteAutomaton() {
        Set<String> Q = new HashSet<>(Arrays.asList("q0", "q1", "q2", "q3", "q4"));
        Set<String> Sigma = new HashSet<>(Arrays.asList("a", "b", "c"));
        Set<String> F = new HashSet<>(Collections.singletonList("q4"));

        Map<String, Map<String, String>> delta = new HashMap<>();
        delta.put("q0", Collections.singletonMap("a", "q1"));
        delta.put("q1", new HashMap<String, String>() {{
            put("b", "q2");
            put("b", "q3");
        }});
        delta.put("q2", Collections.singletonMap("c", "q3"));
        delta.put("q3", new HashMap<String, String>() {{
            put("a", "q3");
            put("b", "q4");
        }});

        String q0 = "q0";

        return new FiniteAutomaton(Q, Sigma, delta, q0, F);
    }

    public Grammar convertToRegularGrammar() {
        return new Grammar(null, null, null, null);
    }

    public boolean isDeterministic() {
        return true;
    }

    public FiniteAutomaton convertToDFA() {
        return new FiniteAutomaton(null, null, null, null, null);
    }
}

class FiniteAutomaton {
    private Set<String> Q;
    private Set<String> Sigma;
    private Map<String, Map<String, String>> delta;
    private String q0;
    private Set<String> F;

    public FiniteAutomaton(Set<String> Q, Set<String> Sigma, Map<String, Map<String, String>> delta, String q0, Set<String> F) {
        this.Q = Q;
        this.Sigma = Sigma;
        this.delta = delta;
        this.q0 = q0;
        this.F = F;
    }

    public boolean stringBelongToLanguage(final String inputString) {
        return true;
    }
}
