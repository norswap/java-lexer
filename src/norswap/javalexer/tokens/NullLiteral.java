package norswap.javalexer.tokens;

/**
 * The {@code null} literal.
 */
public final class NullLiteral extends Literal
{
    // ---------------------------------------------------------------------------------------------

    @Override public int hashCode() {
        return 31 * 1777;
    }

    // ---------------------------------------------------------------------------------------------

    @Override public boolean equals (Object other) {
        return other instanceof NullLiteral;
    }

    // ---------------------------------------------------------------------------------------------

    @Override public String toString() {
        return "Null";
    }

    // ---------------------------------------------------------------------------------------------

}