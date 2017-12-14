package norswap.javalexer.tokens;

import norswap.javalexer.LexUtil;

/**
 * A literal integer value.
 */
public final class IntLiteral extends Literal
{
    // ---------------------------------------------------------------------------------------------

    /**
     * The type of literal: decimal, octal, hexadecimal or binary.
     */
    public enum Type {
        /** Marks a decimal integer literal. */
        DECIMAL,
        /** Marks an octal integer literal. */
        OCTAL,
        /** Marks an hexadecimal literal. */
        HEXADECIMAL,
        /** Marks a binary integer literal. */
        BINARY
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * The string representation of the integer value. Might not actually represent
     * a valid integer value (use {@link #is_valid()}.
     */
    public final String string;

    // ---------------------------------------------------------------------------------------------

    /**
     * The type of integer literal.
     */
    public final Type type;

    // ---------------------------------------------------------------------------------------------

    private Long value;

    // ---------------------------------------------------------------------------------------------

    /**
     * @param string see {@link #string}
     * @param type see {@link Type}
     */
    public IntLiteral (String string, Type type) {
        this.string = string;
        this.type = type;
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * The integer value that the literal represents. Will throw an exception if the
     * literal doesn't represent a valid value (use {@link #is_valid()}.
     */
    public long value()
    {
        return value != null
            ? value
            : (value = LexUtil.parse_int(string));
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * Indicates whether the integer value has been marked as a {@code long} with the {@code l} or
     * {@code L} suffixes. Otherwise, it is an {@code int} by default.
     */
    public boolean is_long()
    {
        char c = string.charAt(string.length() - 1);
        return c == 'l' || c == 'L';
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * Indicates whether the literal represents a valid integer value.
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
        return other instanceof IntLiteral
            && string.equals(((IntLiteral) other).string);
    }

    // ---------------------------------------------------------------------------------------------

    @Override public String toString() {
        return "Int(" + string + ")";
    }

    // ---------------------------------------------------------------------------------------------
}
