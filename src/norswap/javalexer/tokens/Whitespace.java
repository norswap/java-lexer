package norswap.javalexer.tokens;

/**
 * Whitespace: spaces, tabs, newlines...
 */
public final class Whitespace extends InputElement
{
    // ---------------------------------------------------------------------------------------------

    /**
     * The matched whitespace sequence.
     */
    public final String value;

    // ---------------------------------------------------------------------------------------------

    public Whitespace (String value) {
        this.value = value;
    }

    // ---------------------------------------------------------------------------------------------

    @Override public int hashCode() {
        return 1777 + value.hashCode();
    }

    // ---------------------------------------------------------------------------------------------

    @Override public boolean equals (Object other) {
        return other instanceof Whitespace
            && value.equals(((Whitespace) other).value);
    }

    // ---------------------------------------------------------------------------------------------

    @Override public String toString() {
        return "Whitespace";
    }

    // ---------------------------------------------------------------------------------------------
}
