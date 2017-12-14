package norswap.javalexer.tokens;

import norswap.javalexer.LexUtil;

/**
 * A single-quoted character literal.
 */
public final class CharLiteral extends Literal
{
    // ---------------------------------------------------------------------------------------------

    /**
     * The string appearing within the single quotes.
     */
    public final String string;

    // ---------------------------------------------------------------------------------------------

    private Character c;

    // ---------------------------------------------------------------------------------------------

    /**
     * @param string the string appearing within the single quotes.
     */
    public CharLiteral (String string) {
        this.string = string;
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * The character value corresponding to the content of the character literal,
     * performing escape translation if necessary.
     */
    public char character()
    {
        if (c != null)
            return c;
        if (string.length() == 1)
            return c = string.charAt(0);
        else
            return c = LexUtil.unescape_char_content(string);
    }

    // ---------------------------------------------------------------------------------------------

    @Override public int hashCode() {
        return 31 * (1777 + c);
    }

    // ---------------------------------------------------------------------------------------------

    @Override public boolean equals (Object other) {
        return other instanceof CharLiteral
            && string.equals(((CharLiteral) other).string);
    }

    // ---------------------------------------------------------------------------------------------

    @Override public String toString() {
        return "Char(" + string + ")";
    }

    // ---------------------------------------------------------------------------------------------
}
