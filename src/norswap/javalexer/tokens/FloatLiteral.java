package norswap.javalexer.tokens;

import norswap.javalexer.LexUtil;

/**
 * A literal floating-point value.
 */
public final class FloatLiteral extends Literal
{
    // ---------------------------------------------------------------------------------------------

    /**
     * The type of literal: decimal or hexadecimal.
     */
    public enum Type {
        /** Marks a decimal floating-point literal. */
        DECIMAL,
        /** Marks an hexadecimal floating-point literal. */
        HEXADECIMAL
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * The string representation of the floating-point value. Might not actually represent
     * a valid floating-point value (use {@link #is_valid()}.
     */
    public final String string;

    // ---------------------------------------------------------------------------------------------

    /**
     * The type of floating-point literal: decimal or hexadecimal.
     */
    public final Type type;

    // ---------------------------------------------------------------------------------------------

    private Double value;

    // ---------------------------------------------------------------------------------------------

    /**
     * @param string see {@link #string}
     * @param type {@code DECIMAL} or {@code HEXADECIMAL}, see {@link Type}
     */
    public FloatLiteral (String string, Type type) {
        this.string = string;
        this.type = type;
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * The floating-point value that the literal represents. Will throw an exception if the
     * literal doesn't represent a valid value (use {@link #is_valid()}.
     */
    public double value()
    {
        return value != null
            ? value
            : (value = LexUtil.parse_float(string));
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * Indicates whether the floating-point value has been marked as a {@code float} with the {@code
     * f} or {@code F} suffixes. Otherwise, it is a {@code double} by default.
     */
    public boolean is_float()
    {
        char c = string.charAt(string.length() - 1);
        return c == 'f' || c == 'F';
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * Indicates whether the literal represents a valid floating-point value.
     */
    public boolean is_valid()
    {
        try {
            value();
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    // ---------------------------------------------------------------------------------------------

    @Override public int hashCode() {
        return 1777 + string.hashCode();
    }

    // ---------------------------------------------------------------------------------------------

    @Override public boolean equals (Object other) {
        return other instanceof FloatLiteral
            && string.equals(((FloatLiteral) other).string);
    }

    // ---------------------------------------------------------------------------------------------

    @Override public String toString() {
        return "Float(" + string + ")";
    }

    // ---------------------------------------------------------------------------------------------
}
