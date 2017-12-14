package norswap.javalexer.tokens;

/**
 * A Java expression operator.
 */
public final class Operator extends Token
{
    // ---------------------------------------------------------------------------------------------

    /**
     * The string representation of the operator (e.g. "+").
     */
    public final String name;

    // ---------------------------------------------------------------------------------------------

    public Operator (String name) {
        this.name = name;
    }

    // ---------------------------------------------------------------------------------------------

    @Override public int hashCode() {
        return 1777 + name.hashCode();
    }

    // ---------------------------------------------------------------------------------------------

    @Override public boolean equals (Object other) {
        return other instanceof Operator
            && name.equals(((Operator) other).name);
    }

    // ---------------------------------------------------------------------------------------------

    @Override public String toString() {
        return "Op(" + name + ")";
    }

    // ---------------------------------------------------------------------------------------------
}
