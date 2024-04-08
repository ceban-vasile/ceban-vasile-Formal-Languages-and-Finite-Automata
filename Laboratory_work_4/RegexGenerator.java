package Laboratory_work_4;

import java.util.*;

public class RegexGenerator {

    public static void displayResult() {

        String firstRegex = "M?N^2(O|P)^3Q*R+";
        String secondRegex = "(X|Y|Z)^38+(9|0)^2";
        String thirdRegex = "(H|i)(J|K)L*N?";

        System.out.println("First regular expression: {M?N^2(O|P)^3Q*R+}");
        for (int i = 0; i < 5; i++) {
            generateRegex(firstRegex);
            System.out.println();
        }

        System.out.println("Second regular expression: {(X|Y|Z)^38+(9|0)^2}");
        for (int i = 0; i < 5; i++) {
            generateRegex(secondRegex);
            System.out.println();
        }

        System.out.println("Third regular expression: {(H|i)(J|K)L*N?}");
        for (int i = 0; i < 5; i++) {
            generateRegex(thirdRegex);
            System.out.println();
        }

    }

    public static void generateRegex(String regexPattern) {

        StringBuilder resultBuilder = new StringBuilder();
        Random random = new Random();
        int startPosition, sequenceNumber = 1;

        for (int i = 0; i < regexPattern.length(); i++) {

            StringBuilder currentSequenceBuilder = new StringBuilder();
            startPosition = i;
            char currentChar = regexPattern.charAt(i);
            Set<Character> charSet = new HashSet<>();

            if (Character.isLetter(currentChar) || Character.isDigit(currentChar)) {
                if (regexPattern.length() > i + 1 && regexPattern.charAt(i + 1) == '^') {
                    int power = Integer.parseInt(regexPattern.charAt(i + 2) + "");
                    currentSequenceBuilder.append(String.valueOf(currentChar).repeat(Math.max(0, power)));
                    i += 2;
                } else if (regexPattern.length() > i + 1 && regexPattern.charAt(i + 1) == '*') {
                    currentSequenceBuilder.append(String.valueOf(currentChar).repeat(random.nextInt(5)));
                    i++;
                } else if (regexPattern.length() > i + 1 && regexPattern.charAt(i + 1) == '+') {
                    currentSequenceBuilder.append(String.valueOf(currentChar).repeat(random.nextInt(1, 5)));
                    i++;
                } else if (regexPattern.length() > i + 1 && regexPattern.charAt(i + 1) == '?') {
                    if (random.nextBoolean()) {
                        currentSequenceBuilder.append(currentChar);
                    }
                    i++;
                }
            }

            if (currentChar == '(') {
                char nextChar = regexPattern.charAt(i + 1);
                while (nextChar != ')') {
                    if (nextChar != '|') {
                        charSet.add(nextChar);
                    }
                    nextChar = regexPattern.charAt(i + 1);
                    i++;
                }

                if (regexPattern.length() > i + 1 && regexPattern.charAt(i + 1) == '^') {
                    int power = Integer.parseInt(regexPattern.charAt(i + 2) + "");

                    for (int j = 0; j < power; j++) {
                        currentSequenceBuilder.append(charSet.stream().toList().get(random.nextInt(charSet.size())));
                    }
                    i += 2;
                } else {
                    currentSequenceBuilder.append(charSet.stream().toList().get(random.nextInt(charSet.size())));
                }
            }

            System.out.println((sequenceNumber++) + ") " + regexPattern.substring(startPosition, i + 1) + " -> " + currentSequenceBuilder.toString());
            resultBuilder.append(currentSequenceBuilder);
        }

        System.out.println("Regular expression>> " + resultBuilder);
    }
}
