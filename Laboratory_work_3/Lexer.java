package Laboratory_work_3;

import java.util.ArrayList;
import java.util.List;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    private static final List<TokenPattern> TOKEN_PATTERNS = new ArrayList<>();

    static {
        TOKEN_PATTERNS.add(new TokenPattern("DECIMAL", Pattern.compile("\\d+\\.\\d+")));
        TOKEN_PATTERNS.add(new TokenPattern("INTEGER", Pattern.compile("\\d+")));
        TOKEN_PATTERNS.add(new TokenPattern("STRING", Pattern.compile("\"([^\"]+)\"")));
        TOKEN_PATTERNS.add(new TokenPattern("COMMENT", Pattern.compile("//.*")));
        TOKEN_PATTERNS.add(new TokenPattern("OPERATOR", Pattern.compile("[\\+\\-\\*/=<>!]=?|&&|\\|\\|")));
        TOKEN_PATTERNS.add(new TokenPattern("IDENTIFIER", Pattern.compile("[a-zA-Z_]\\w*")));
        TOKEN_PATTERNS.add(new TokenPattern("KEYWORD", Pattern.compile("if|else|for|while")));
        TOKEN_PATTERNS.add(new TokenPattern("BRACKET", Pattern.compile("[\\{\\[\\(\\)\\}\\]]")));
        TOKEN_PATTERNS.add(new TokenPattern("PUNCTUATION", Pattern.compile("[\\;\\,\\.]")));
        TOKEN_PATTERNS.add(new TokenPattern("WHITESPACE", Pattern.compile("\\s+")));
        TOKEN_PATTERNS.add(new TokenPattern("UNKNOWN", Pattern.compile(".")));
    }

    private final String codeText;
    private final List<Token> tokens;

    public Lexer(String codeText) {
        this.codeText = codeText;
        this.tokens = new ArrayList<>();
    }

    public List<Token> tokenize() {
        String remainingText = codeText;

        while (!remainingText.isEmpty()) {
            boolean tokenMatched = false;

            for (TokenPattern pattern : TOKEN_PATTERNS) {
                Matcher matcher = pattern.getPattern().matcher(remainingText);

                if (matcher.find() && matcher.start() == 0) {
                    String value = matcher.group(0);

                    if (!pattern.getType().equals("WHITESPACE") && !pattern.getType().equals("COMMENT")) {
                        tokens.add(new Token(pattern.getType(), value));
                    }

                    remainingText = remainingText.substring(value.length());
                    tokenMatched = true;
                    break;
                }
            }

            if (!tokenMatched) {
                throw new RuntimeException("TokenizerError: Unknown token");
            }
        }

        return tokens;
    }
}
