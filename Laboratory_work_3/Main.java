package Laboratory_work_3;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        String codeText = """
            // This is a single-line comment
            int x = 10;    // Another comment
            float y = 3.14; 
            if (x > y) {
                System.out.println("x is greater"); // This won't be tokenized
            } 
            """;

        Lexer lexer = new Lexer(codeText);
        List<Token> tokens = lexer.tokenize();

        for (Token token : tokens) {
            System.out.println(token);
        }
    }
}
