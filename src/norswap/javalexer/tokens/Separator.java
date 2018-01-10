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
    public final String name;

    // ---------------------------------------------------------------------------------------------

    public Separator (String name) {
        this.name = name;
    }

    // ---------------------------------------------------------------------------------------------

    @Override public int hashCode() {
        return 1777 + name.hashCode();
    }

    // ---------------------------------------------------------------------------------------------

    @Override public boolean equals (Object other) {
        return other instanceof Separator
            && name.equals(((Separator) other).name);
    }

    // ---------------------------------------------------------------------------------------------

    @Override public String toString() {
        return "Sep<" + name + ">";
    }

    // ---------------------------------------------------------------------------------------------
}
