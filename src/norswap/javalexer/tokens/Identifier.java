package norswap.javalexer.tokens;

/**
 * An identifier (name).
 */
public final class Identifier extends Token
{
    // ---------------------------------------------------------------------------------------------

    /**
     * The string value of the identifier.
     */
    public final String value;

    // ---------------------------------------------------------------------------------------------

    public Identifier (String value) {
        this.value = value;
    }

    // ---------------------------------------------------------------------------------------------

    @Override public int hashCode() {
        return 1777 + value.hashCode();
    }

    // ---------------------------------------------------------------------------------------------

    @Override public boolean equals (Object other) {
        return other instanceof Identifier
            && value.equals(((Identifier) other).value);
    }

    // ---------------------------------------------------------------------------------------------

    @Override public String toString() {
        return "ID(" + value + ")";
    }

    // ---------------------------------------------------------------------------------------------
}
