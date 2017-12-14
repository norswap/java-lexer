package norswap.javalexer.tokens;

import norswap.javalexer.LexUtil;

/**
 * A double-quoted string literal.
 */
public class StringLiteral extends Literal
{
    // ---------------------------------------------------------------------------------------------

    /**
     * The string appearing within the double quotes.
     */
    public final String string;

    // ---------------------------------------------------------------------------------------------

    public StringLiteral (String string) {
        this.string = string;
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * The string value corresponding to the content of the string literal,
     * performing escape translation if necessary.
     */
    public String value() {
        return LexUtil.unescape_string_content(string);
    }

    // ---------------------------------------------------------------------------------------------

    @Override public int hashCode() {
        return 1777 + string.hashCode();
    }

    // ---------------------------------------------------------------------------------------------

    @Override public boolean equals (Object other) {
        return other instanceof StringLiteral
            && string.equals(((StringLiteral) other).string);
    }

    // ---------------------------------------------------------------------------------------------

    @Override public String toString() {
        return "String(" + string + ")";
    }

    // ---------------------------------------------------------------------------------------------
}
