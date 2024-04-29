package Laboratory_work_6;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        String input = "sqrt(25) + (10 - 3) * powtwo(2)";
        List<Token> tokens = Lexer.tokenize(input);
        System.out.println("Tokens:");
        tokens.forEach(System.out::println);

        Parser parser = new Parser(tokens);
        Expr expression = parser.parse();
        double result = expression.evaluate();
        System.out.println("Result: " + result);
    }
}
