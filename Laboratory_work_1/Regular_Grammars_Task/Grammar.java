package Laboratory_work_1.Regular_Grammars_Task;

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
            if (Character.isLetter(c)) {
                result.append(generateStringHelper(String.valueOf(c)));
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }

    public FiniteAutomaton toFiniteAutomaton() {
        // Replace placeholder values with actual values for Q, Sigma, delta, q0, and F
        Set<String> Q = new HashSet<>(Arrays.asList("q0", "q1", "q2"));
        Set<String> Sigma = new HashSet<>(Arrays.asList("a", "b", "c", "d", "e", "f"));
        Map<String, Map<String, String>> delta = new HashMap<>();
        // Populate delta with transitions
        delta.put("q0", Map.of("a", "q1", "b", "q1", "c", "q2", "d", "q2"));
        delta.put("q1", Map.of("d", "q2", "e", "q2"));
        delta.put("q2", Map.of("f", "q2", "e", "q2", "d", "q2"));
        String q0 = "q0";
        Set<String> F = new HashSet<>(Collections.singletonList("q2"));

        return new FiniteAutomaton(Q, Sigma, delta, q0, F);
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
        String currentState = q0;

        for (char symbol : inputString.toCharArray()) {
            String transition = delta.get(currentState).get(String.valueOf(symbol));
            if (transition == null) {
                return false;
            }
            currentState = transition;
        }

        return F.contains(currentState);
    }
}