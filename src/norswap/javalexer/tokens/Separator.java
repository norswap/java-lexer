package norswap.javalexer.tokens;

/**
 * A Java separator.
 */
public final class Separator extends Token
{
    // ---------------------------------------------------------------------------------------------

    /**
     * The string value of the separator (e.g. ",").
     */
    public final String value;

    // ---------------------------------------------------------------------------------------------

    public Separator (String value) {
        this.value = value;
    }

    // ---------------------------------------------------------------------------------------------

    @Override public int hashCode() {
        return 1777 + value.hashCode();
    }

    // ---------------------------------------------------------------------------------------------

    @Override public boolean equals (Object other) {
        return other instanceof Separator
            && value.equals(((Separator) other).value);
    }

    // ---------------------------------------------------------------------------------------------

    @Override public String toString() {
        return "Sep<" + value + ">";
    }

    // ---------------------------------------------------------------------------------------------
}
