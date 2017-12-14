package norswap.javalexer.tokens;

/**
 * A reserved Java keyword.
 */
public final class Keyword extends Token
{
    // ---------------------------------------------------------------------------------------------

    /**
     * The string representation of the keyword (e.g. "class").
     */
    public final String name;

    // ---------------------------------------------------------------------------------------------

    public Keyword (String name) {
        this.name = name;
    }

    // ---------------------------------------------------------------------------------------------

    @Override public int hashCode() {
        return 1777 + name.hashCode();
    }

    // ---------------------------------------------------------------------------------------------

    @Override public boolean equals (Object other) {
        return other instanceof Keyword
            && name.equals(((Keyword) other).name);
    }

    // ---------------------------------------------------------------------------------------------

    @Override public String toString() {
        return "Keyword(" + name + ")";
    }

    // ---------------------------------------------------------------------------------------------
}
