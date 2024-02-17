package Laboratory_work_1.LexerScanner_Task;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    private final List<Token> tokens;

    public Lexer(String input) {
        this.tokens = new ArrayList<>();
        tokenize(input);
    }

    public List<Token> getTokens() {
        return tokens;
    }

    private void tokenize(String input) {
        String regex = "\\s*(\\+|\\-|\\*|\\/|\\(|\\)|\\d+|\\w+)\\s*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            String tokenValue = matcher.group(1);
            TokenType tokenType = determineTokenType(tokenValue);
            tokens.add(new Token(tokenType, tokenValue));
        }
    }

    private TokenType determineTokenType(String tokenValue) {
        if (tokenValue.matches("\\d+")) {
            return TokenType.NUMBER;
        } else if (tokenValue.matches("\\w+")) {
            return TokenType.IDENTIFIER;
        } else {
            switch (tokenValue) {
                case "+":
                    return TokenType.PLUS;
                case "-":
                    return TokenType.MINUS;
                case "*":
                    return TokenType.MULTIPLY;
                case "/":
                    return TokenType.DIVIDE;
                case "(":
                    return TokenType.OPEN_PAREN;
                case ")":
                    return TokenType.CLOSE_PAREN;
                default:
                    return TokenType.UNKNOWN;
            }
        }
    }

    public static void main(String[] args) {
        String input = "3 + 4 * (5 - 2)";
        Lexer lexer = new Lexer(input);
        List<Token> tokens = lexer.getTokens();

        for (Token token : tokens) {
            System.out.println(token);
        }
    }
}
