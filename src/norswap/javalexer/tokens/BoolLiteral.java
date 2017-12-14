package norswap.javalexer.tokens;

/**
 * A boolean literal: {@code true} or {@code false}.
 */
public final class BoolLiteral extends Literal
{
    // ---------------------------------------------------------------------------------------------

    public final boolean value;

    // ---------------------------------------------------------------------------------------------

    public BoolLiteral (boolean value) {
        this.value = value;
    }

    // ---------------------------------------------------------------------------------------------

    @Override public int hashCode() {
        return 1777 + (value ? 0 : 1);
    }

    // ---------------------------------------------------------------------------------------------

    @Override public boolean equals (Object other) {
        return other instanceof BoolLiteral
            && value == ((BoolLiteral) other).value;
    }

    // ---------------------------------------------------------------------------------------------

    @Override public String toString() {
        return value ? "True" : "False";
    }

    // ---------------------------------------------------------------------------------------------
}