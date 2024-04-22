package Laboratory_work_5;

import java.util.*;

public class CNFConverter extends Grammar {

    public CNFConverter(Grammar grammar) {
        super();
        this.setNonTerminals(grammar.getNonTerminals());
        this.setTerminals(grammar.getTerminals());
        this.setStartSymbol(grammar.getStartSymbol());
        this.setRules(grammar.getRules());
        removeEpsilonProductions();
        removeUnitProductions();
        eliminateInaccessibleStates();
        eliminateNonProductiveRules();
        convertToChomskyNormalForm();
    }

    public void convertToChomskyNormalForm() {
        Map<String, String> terminalNonTerminalsMap = new HashMap<>();
        Map<String, String> newNonTerminalsMap = new HashMap<>();
        Map<String, String> nonTerminalMapping = new HashMap<>();
        int newNonTerminalCounter = 0;

        Map<String, List<String>> updatedRules = new HashMap<>(getRules());

        for (Map.Entry<String, List<String>> entry : updatedRules.entrySet()) {
            String fromState = entry.getKey();
            List<String> toStates = entry.getValue();
            List<String> newToStates = new ArrayList<>();
            for (String production : toStates) {
                if (production.length() > 2) {
                    String newNonTerminal = nonTerminalMapping.get(production);
                    if (newNonTerminal == null) {
                        newNonTerminal = generateNewNonTerminal(newNonTerminalsMap, newNonTerminalCounter++, production);
                        newNonTerminalsMap.put(newNonTerminal, production.substring(1));
                        nonTerminalMapping.put(production, newNonTerminal);
                    }
                    newToStates.add(production.charAt(0) + newNonTerminal);
                } else {
                    newToStates.add(production);
                }
            }
            updatedRules.put(fromState, newToStates);
        }

        for (Map.Entry<String, String> entry : newNonTerminalsMap.entrySet()) {
            updatedRules.put(entry.getKey(), List.of(entry.getValue()));
        }

        for (Map.Entry<String, List<String>> entry : updatedRules.entrySet()) {
            String fromState = entry.getKey();
            List<String> toStates = entry.getValue();
            List<String> newToStates = new ArrayList<>();
            for (String production : toStates) {
                if (production.length() == 2 && Character.isLowerCase(production.charAt(0)) && Character.isUpperCase(production.charAt(1))) {
                    String terminal = production.substring(0, 1);
                    String nonTerminal = terminalNonTerminalsMap.getOrDefault(terminal, null);
                    if (nonTerminal == null) {
                        nonTerminal = generateNewNonTerminal(newNonTerminalsMap, newNonTerminalCounter++, terminal);
                        terminalNonTerminalsMap.put(terminal, nonTerminal);
                    }
                    newToStates.add(nonTerminal + production.charAt(1));
                } else {
                    newToStates.add(production);
                }
            }
            updatedRules.put(fromState, newToStates);
        }

        for (Map.Entry<String, String> entry : newNonTerminalsMap.entrySet()) {
            String nonTerminal = entry.getKey();
            String terminal = entry.getValue();
            updatedRules.put(nonTerminal, List.of(terminal));
        }

        setRules(updatedRules);
    }

    private String generateNewNonTerminal(Map<String, String> newNonTerminals, int counter, String symbols) {
        String newNonTerminal = "X" + counter;
        newNonTerminals.put(newNonTerminal, symbols);
        return newNonTerminal;
    }

    public void eliminateNonProductiveRules() {
        Set<String> productiveStates = new HashSet<>();
        for (Map.Entry<String, List<String>> entry : getRules().entrySet()) {
            String fromState = entry.getKey();
            List<String> toStates = entry.getValue();
            for (String production : toStates) {
                boolean valid = true;
                for (char state : production.toCharArray()) {
                    if (Character.isUpperCase(state) && !productiveStates.contains(state + "")) {
                        valid = false;
                        break;
                    }
                }
                if (valid) {
                    productiveStates.add(fromState);
                    break;
                }
            }
        }

        Map<String, List<String>> updatedRules = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : getRules().entrySet()) {
            String fromState = entry.getKey();
            if (productiveStates.contains(fromState)) {
                List<String> toStates = entry.getValue();
                List<String> updatedToStates = new ArrayList<>();
                for (String production : toStates) {
                    boolean valid = true;
                    for (char state : production.toCharArray()) {
                        if (Character.isUpperCase(state) && !productiveStates.contains(state + "")) {
                            valid = false;
                            break;
                        }
                    }
                    if (valid) {
                        updatedToStates.add(production);
                    }
                }
                updatedRules.put(fromState, updatedToStates);
            }
        }
        setRules(updatedRules);
    }

    public void eliminateInaccessibleStates() {
        Set<String> accessibleStates = new HashSet<>();
        accessibleStates.add(getStartSymbol());
        boolean changed;
        do {
            changed = false;
            for (Map.Entry<String, List<String>> entry : getRules().entrySet()) {
                String fromState = entry.getKey();
                List<String> toStates = entry.getValue();
                if (accessibleStates.contains(fromState)) {
                    for (String production : toStates) {
                        for (char state : production.toCharArray()) {
                            if (Character.isUpperCase(state) && !accessibleStates.contains(state + "")) {
                                accessibleStates.add(state + "");
                                changed = true;
                            }
                        }
                    }
                }
            }
        } while (changed);

        Map<String, List<String>> updatedRules = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : getRules().entrySet()) {
            String fromState = entry.getKey();
            if (accessibleStates.contains(fromState)) {
                List<String> toStates = entry.getValue();
                List<String> updatedToStates = new ArrayList<>();
                for (String production : toStates) {
                    boolean valid = true;
                    for (char state : production.toCharArray()) {
                        if (Character.isUpperCase(state) && !accessibleStates.contains(state + "")) {
                            valid = false;
                            break;
                        }
                    }
                    if (valid) {
                        updatedToStates.add(production);
                    }
                }
                updatedRules.put(fromState, updatedToStates);
            }
        }
        setRules(updatedRules);
    }

    public void removeUnitProductions() {
        Map<String, List<String>> updatedRules = new HashMap<>(getRules());
        boolean changed;
        do {
            changed = false;
            for (Map.Entry<String, List<String>> entry : updatedRules.entrySet()) {
                String fromState = entry.getKey();
                List<String> toStates = entry.getValue();
                List<String> newToStates = new ArrayList<>();
                for (String production : toStates) {
                    if (production.length() == 1 && Character.isUpperCase(production.charAt(0))) {
                        List<String> unitProductions = updatedRules.get(production);
                        if (unitProductions != null) {
                            newToStates.addAll(unitProductions);
                            changed = true;
                        }
                    } else {
                        newToStates.add(production);
                    }
                }
                updatedRules.put(fromState, newToStates);
            }

            for (Map.Entry<String, List<String>> entry : updatedRules.entrySet()) {
                String fromState = entry.getKey();
                List<String> toStates = entry.getValue();
                List<String> newToStates = new ArrayList<>();
                for (String production : toStates) {
                    if (!(production.length() == 1 && Character.isUpperCase(production.charAt(0)))) {
                        newToStates.add(production);
                    }
                }
                updatedRules.put(fromState, newToStates);
            }
        } while (changed);
        setRules(updatedRules);
    }

    public void removeEpsilonProductions() {
        Map<String, List<String>> updatedRules = new HashMap<>(getRules());

        Set<String> epsilonStates = new HashSet<>();
        for (Map.Entry<String, List<String>> entry : updatedRules.entrySet()) {
            if (entry.getValue().contains("")) {
                epsilonStates.add(entry.getKey());
            }
        }

        for (String state : epsilonStates) {
            List<String> productions = updatedRules.get(state);
            List<String> updatedProductions = new ArrayList<>(productions);
            updatedProductions.remove("");
            updatedRules.put(state, updatedProductions);
        }

        for (String stateWithEpsilon : epsilonStates) {
            for (Map.Entry<String, List<String>> entry : updatedRules.entrySet()) {
                String fromState = entry.getKey();
                List<String> toStates = entry.getValue();
                List<String> newToStates = new ArrayList<>();
                for (String production : toStates) {
                    if (production.contains(stateWithEpsilon)) {
                        List<String> replicatedProductions = replicateProduction(production, stateWithEpsilon);
                        newToStates.addAll(replicatedProductions);
                    } else {
                        newToStates.add(production);
                    }
                }
                updatedRules.put(fromState, newToStates);
            }
        }

        for (Map.Entry<String, List<String>> entry : updatedRules.entrySet()) {
            String fromState = entry.getKey();
            List<String> toStates = entry.getValue();
            Set<String> uniqueToStates = new HashSet<>();
            for (String production : toStates) {
                generateCombinations("", production, 0, uniqueToStates);
            }
            updatedRules.put(fromState, new ArrayList<>(uniqueToStates));
        }

        setRules(updatedRules);
    }

    private void generateCombinations(String prefix, String remaining, int index, Set<String> combinations) {
        if (index == remaining.length()) {
            combinations.add(prefix);
            return;
        }
        char currentChar = remaining.charAt(index);
        if (currentChar == 'ε') {
            generateCombinations(prefix, remaining, index + 1, combinations);
            generateCombinations(prefix + currentChar, remaining, index + 1, combinations);
        } else {
            generateCombinations(prefix + currentChar, remaining, index + 1, combinations);
        }
    }

    private List<String> replicateProduction(String production, String stateWithEpsilon) {
        List<String> replicatedProductions = new ArrayList<>();
        int numInstances = (int) production.chars().filter(ch -> ch == stateWithEpsilon.charAt(0)).count();
        for (int i = 0; i < Math.pow(2, numInstances); i++) {
            StringBuilder sb = new StringBuilder(production);
            for (int j = 0; j < numInstances; j++) {
                if ((i & (1 << j)) != 0) {
                    int index = sb.indexOf(stateWithEpsilon);
                    sb.replace(index, index + 1, "");
                }
            }
            replicatedProductions.add(sb.toString());
        }
        return replicatedProductions;
    }

    public void displayCNF() {
        Map<String, List<String>> rules = getRules();
        for (Map.Entry<String, List<String>> entry : rules.entrySet()) {
            String fromState = entry.getKey();
            List<String> toStates = entry.getValue();
            for (String production : toStates) {
                System.out.println(fromState + " -> " + production);
            }
        }
    }
}
