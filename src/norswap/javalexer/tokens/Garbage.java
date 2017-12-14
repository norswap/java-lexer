package norswap.javalexer.tokens;

/**
 * A garbage token represents a string of characters that could not be used to form any other
 * tokens. This is not an official Java token, but generating these tokens while lexing instead
 * of signaling an error makes the lexer error-tolerant.
 */
public class Garbage extends Token
{
    // ---------------------------------------------------------------------------------------------

    /**
     * The garbage string.
     */
    public final String value;

    // ---------------------------------------------------------------------------------------------

    public Garbage (String value) {
        this.value = value;
    }

    // ---------------------------------------------------------------------------------------------

    @Override public int hashCode() {
        return 1777 + value.hashCode();
    }

    // ---------------------------------------------------------------------------------------------

    @Override public boolean equals (Object other) {
        return other instanceof Garbage
            && value.equals(((Garbage) other).value);
    }

    // ---------------------------------------------------------------------------------------------

    @Override public String toString() {
        return "Garbage";
    }

    // ---------------------------------------------------------------------------------------------
}
