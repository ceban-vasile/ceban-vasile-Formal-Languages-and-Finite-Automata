package Laboratory_work_3;

import java.util.regex.Pattern;

public class TokenPattern {
    private final String type;
    private final Pattern pattern;

    public TokenPattern(String type, Pattern pattern) {
        this.type = type;
        this.pattern = pattern;
    }

    public String getType() {
        return type;
    }

    public Pattern getPattern() {
        return pattern;
    }
}
