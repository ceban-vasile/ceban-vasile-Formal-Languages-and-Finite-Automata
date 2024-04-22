package Laboratory_work_5;

import Laboratory_work_1.FiniteAutomaton;

import java.util.*;
import java.util.regex.Pattern;

public class Grammar {

    private Set<String> nonTerminals;
    private Set<String> terminals;
    private String startSymbol;
    private Map<String, List<String>> rules;

    public Grammar() {
    }

    public Grammar(FiniteAutomaton fa) {

        startSymbol = fa.getStartStateQ0();
        nonTerminals = fa.getStatesQ();
        terminals = fa.getAlphabetSigma();
        rules = new HashMap<>();

        for (FiniteAutomaton.Transition t : fa.getTransitions()) {

            String toState = t.getToState() == null ? "" : t.getToState();
            if (rules.containsKey(t.getFromState())) {
                rules.get(t.getFromState()).add(t.getWithSymbol() + toState);
            } else {
                rules.put(t.getFromState(), new ArrayList<>(List.of(t.getWithSymbol() + toState)));
            }
        }
    }

    public String generateString() {

        List<String> currentNonTerminals = new java.util.ArrayList<>(List.of(startSymbol));
        StringBuilder result = new StringBuilder();
        StringBuilder transformations = new StringBuilder();
        result.append(startSymbol);
        transformations.append(startSymbol);
        Random rand = new Random();

        while (!currentNonTerminals.isEmpty()) {

            int randIndex = rand.nextInt(currentNonTerminals.size());

            int placeToReplace = result.indexOf(currentNonTerminals.get(randIndex));

            int randIndexToReplace = rand.nextInt(rules.get(currentNonTerminals.get(randIndex)).size());
            String valueToReplace = rules.get(currentNonTerminals.get(randIndex)).get(randIndexToReplace);

            result.replace(placeToReplace, placeToReplace + 1, valueToReplace);

            transformations.append(" -> ").append(result);

            currentNonTerminals.remove(randIndex);
            for (char c : valueToReplace.toCharArray()) {
                if (nonTerminals.contains(c)) {
                    currentNonTerminals.add(String.valueOf(c));
                }
            }
        }
        System.out.println(transformations);
        return result.toString();
    }

    public String getChomskyType() {

        Set<String> states = rules.keySet();
        boolean notSecondType = false;

        // check for 0 TYPE
        for (String fromState : states) {

            char[] chars = fromState.toCharArray();

            if (fromState.length() > 1)
                notSecondType = true;

            for (char c : chars) {
                if (terminals.contains(c)) {
                    return "TYPE 0";
                }
            }
        }

        if (notSecondType)
            return "TYPE I";


        // check for III TYPE
        boolean leftHandRule = true, rightHandRule = true;

        Pattern type3RightHandRule = Pattern.compile("^[a-z][A-Z]$");
        Pattern type3LeftHandRule = Pattern.compile("^[A-Z][a-z]$");


        boolean currentValue = true;
        for (String fromState : states) {

            List<String> toState = rules.get(fromState);
            for (String s : toState) {

                String state = s.replace("FINAL", "");
                currentValue = false;
                System.out.println(s);

                // check A-> aB
                if (!Pattern.matches(type3RightHandRule.pattern(), state) && state.length() > 1) {
                    rightHandRule = false;
                    currentValue = true;
                }

                // check A-> Ba
                if (!Pattern.matches(type3LeftHandRule.pattern(), state) && state.length() > 1) {
                    leftHandRule = false;
                    currentValue = true;
                }

                // check A->a
                if (state.length() == 1) {
                    currentValue = true;
                }

                if (!currentValue) {
                    break;
                }
            }
        }

        if ((rightHandRule && !leftHandRule) || (leftHandRule && !rightHandRule))
            if (currentValue)
                return "TYPE III";



        Pattern secondTypePattern = Pattern.compile("([a-z]+)?([A-Z]+)?([a-z]+)?");
        boolean secondType = true;

        for (String fromState : states) {

            List<String> toState = rules.get(fromState);

            for (String s : toState) {
                if (!Pattern.matches(secondTypePattern.pattern(), s))
                    secondType = false;
            }
        }

        if (secondType)
            return "TYPE II";

        return "TYPE I";
    }

    public Set<String> getNonTerminals() {
        return nonTerminals;
    }

    public void setNonTerminals(Set<String> nonTerminals) {
        this.nonTerminals = nonTerminals;
    }

    public Set<String> getTerminals() {
        return terminals;
    }

    public void setTerminals(Set<String> terminals) {
        this.terminals = terminals;
    }

    public String getStartSymbol() {
        return startSymbol;
    }

    public void setStartSymbol(String startSymbol) {
        this.startSymbol = startSymbol;
    }

    public Map<String, List<String>> getRules() {
        return rules;
    }

    public void setRules(Map<String, List<String>> rules) {
        this.rules = rules;
    }
}
