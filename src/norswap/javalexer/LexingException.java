package norswap.javalexer;

/**
 * Indicates the failure of recognizing an input element while lexing an input string.
 */
public final class LexingException extends RuntimeException
{
    // ---------------------------------------------------------------------------------------------

    /**
     * The position at which the failure occured.
     */
    public final int position;

    // ---------------------------------------------------------------------------------------------

    public LexingException (int position) {
        this.position = position;
    }

    // ---------------------------------------------------------------------------------------------

    @Override public int hashCode () {
        return position;
    }

    // ---------------------------------------------------------------------------------------------

    @Override public boolean equals (Object other) {
        return other instanceof LexingException && position == ((LexingException) other).position;
    }

    // ---------------------------------------------------------------------------------------------

    @Override public String toString() {
        return "LexingException(" + position + ")";
    }

    // ---------------------------------------------------------------------------------------------
}
