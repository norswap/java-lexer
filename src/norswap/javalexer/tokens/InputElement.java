package norswap.javalexer.tokens;

/**
 * Parent class for all input elements which are either tokens ({@link Token}) or non-token
 * input elements ({@link Whitespace} and {@link Comment}).
 * <p>
 * Each input element has fields ({@link #start} and {@link #end}) to record its position in the
 * input, but those are not part of the identity of the input element (for {@link #equals}, etc).
 */
public abstract class InputElement
{
    // ---------------------------------------------------------------------------------------------

    /**
     * Input position at which the input element begins (inclusive).
     */
    public int start;

    // ---------------------------------------------------------------------------------------------

    /**
     * Input position at which the input element ends (exclusive).
     */
    public int end;

    // ---------------------------------------------------------------------------------------------

    /**
     * Length of the input element.
     */
    public int length() {
        return end - start;
    }

    // ---------------------------------------------------------------------------------------------

    InputElement() {}

    // ---------------------------------------------------------------------------------------------

    /**
     * True only if this input element is equal to {@code other} according to {@link #equals}, AND
     * their input positions are the same.
     */
    public boolean equals_with_position (Object other)
    {
        return this.equals(other)
            && start == ((InputElement) other).start
            && end   == ((InputElement) other).end;
    }

    // ---------------------------------------------------------------------------------------------
}
