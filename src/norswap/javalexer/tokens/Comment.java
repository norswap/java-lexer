package norswap.javalexer.tokens;

/**
 * A comment, either from the line or block variant.
 */
public final class Comment extends InputElement
{
    // ---------------------------------------------------------------------------------------------

    /**
     * The type of comment: either a line or block comment.
     */
    public enum Type {
        /** A line comment starting with //. */
        LINE,
        /** A block comment delimited with {@code / * * /}. */
        BLOCK
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * The full comment, including delimiters, and the terminating newline (if present) for line
     * comments.
     */
    public final String value;

    // ---------------------------------------------------------------------------------------------

    /**
     * The {@link Type} of comment.
     */
    public final Type type;

    // ---------------------------------------------------------------------------------------------

    /**
     * @param value see {@link #value}
     * @param type {@code LINE} or {@code BLOCK}, see {@link Type}
     */
    public Comment (String value, Type type) {
        this.value = value;
        this.type = type;
    }

    // ---------------------------------------------------------------------------------------------

    @Override public int hashCode() {
        return 1777 + value.hashCode();
    }

    // ---------------------------------------------------------------------------------------------

    @Override public boolean equals (Object other) {
        return other instanceof Comment
            && value.equals(((Comment) other).value);
    }

    // ---------------------------------------------------------------------------------------------

    @Override public String toString() {
        return type == Type.LINE ? "LineComment" : "BlockComment";
    }

    // ---------------------------------------------------------------------------------------------
}
