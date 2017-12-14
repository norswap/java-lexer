package norswap.javalexer;

/**
 * Utilities dealing with lexical specificities of Java (escape handling, number parsing).
 */
public final class LexUtil
{
    // ---------------------------------------------------------------------------------------------

    /**
     * Returns the character escaped by a backslash followed by the character held in {@code c},
     * or 0 if no such escapes exist (octal and unicode escapes are not considered).
     */
    private static char escape_for_char (char c)
    {
        switch (c) {
            case 'b':  return '\b';
            case 't':  return '\t';
            case 'n':  return '\n';
            case 'f':  return '\f';
            case 'r':  return '\r';
            case '"':  return '"';
            case '\'': return '\'';
            case '\\': return '\\';
        }

        return 0;
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * Does {@code c} represent an octal digit?
     */
    public static boolean is_octal (char c) {
        return '0' <= c && c <= '7';
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * Does {@code c} represent an hexadecimal digit?
     */
    public static boolean is_hex (char c) {
        return '0' <= c && c <= '9' || 'a' <= c && c <= 'f' || 'A' <= c && c <= 'F';
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * Returns the hex value of the digit {@code c}, or -1 if {@code c} is not an hex digit.
     */
    public static int hex_value (char c)
    {
        if ('0' <= c && c <= '9')
            return c - '0';
        if ('a' <= c && c <= 'f')
            return 10 + c - 'a';
        if ('A' <= c && c <= 'F')
            return 10 + c - 'A';
        else
            return -1;
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * Returns the length of the octal digit string necessary to encode character {@code c}, or -1
     * if {@code c >= 256}.
     */
    private static int octal_length (char c) {
        return c >= 256 ? -1
             : c >= 64  ?  3
             : c >= 8   ?  2
                        :  1;
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * Assuming {@code string} starts with a octal digit, returns the char represented by the
     * biggest octal escape digit string (representing a char < 256) that appears as a prefix of the
     * string.
     */
    private static char bite_octal (CharSequence string)
    {
        int len = string.length();
        int total;

        assert len > 0; {
            char c = string.charAt(0);
            if (!is_octal(c)) throw new IllegalArgumentException("invalid octal escape");
            total = c - '0';
        }

        if (len > 1) {
            char c = string.charAt(1);
            if (!is_octal(c)) return (char) total;
            total = total * 8 + c - '0';
        }

        if (len > 2 && string.charAt(0) <= '3') {
            char c = string.charAt(2);
            if (!is_octal(c)) return (char) total;
            total = total * 8 + c - '0';
        }

        return (char) total;
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * Assuming {@code string} starts with a 'u', returns a composite int value:
     * <ul>
     *   <li>Lowest 16 bits: char represented by the biggest unicode escape
     *       string that appears as a prefix of the string.</li>
     *   <li>Highest 16 bits: length of the unicode escape string.</li>
     * </ul>
     */
    private static int bite_unicode (CharSequence string)
    {
        int len = string.length();
        int i = 0;

        while (i < len && string.charAt(i) == 'u') ++i;

        int limit = i + 4;
        int total = 0;

        if (len < limit)
            throw new IllegalArgumentException(
                "illegal unicode escape sequence: not enough digits");

        while (i < limit) {
            int value = hex_value(string.charAt(i++));
            if (value < 0)
                throw new IllegalArgumentException(
                    "illegal unicode escape sequence: not enough digits");
            total = total * 16 + value;
        }

        return limit << 16 + total;
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * Given a string appearing between single quotes (not included!) in a character literal,
     * returns the corresponding character. Might throw an exception if the literal is illegal.
     * <p>
     * Is also able to process unicode escapes (which are normally processed earlier).
     */
    public static char unescape_char_content (String string)
    {
        int len = string.length();

        if (len == 0)
            throw new IllegalArgumentException("input length is 0");

        // single character
        if (len == 1)
            return string.charAt(0);

        // escape check
        if (string.charAt(0) != '\\')
            throw new IllegalArgumentException("not a single character or escape");

        // regular escape
        if (len == 2) {
            char c = escape_for_char(string.charAt(1));
            if (c != 0) return c;
        }

        // octal escape
        if (is_octal(string.charAt(1)))
        {
            char c = bite_octal(string.substring(1));

            if (octal_length(c) < len - 1)
                throw new IllegalArgumentException("illegal octal escape: trailing garbage");

            return c;
        }

        // unicode escape
        if (string.charAt(1) == 'u') {
            int x = bite_unicode(string.substring(1));

            if (x >> 16 != len - 1)
                throw new IllegalArgumentException("illegal hex escape: trailing garbage");

            return (char) (x & 0xFFFF);
        }

        throw new IllegalArgumentException("illegal escape");
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * Given a string appearing between double quotes (not included!) in a string literal, returns
     * the corresponding string. Might throw an exception if the literal is illegal.
     * <p>
     * Is also able to process unicode escapes (which are normally processed earlier).
     */
    public static String unescape_string_content (String string)
    {
        int len = string.length();
        StringBuilder b = new StringBuilder();
        char[] chars = string.toCharArray();

        int i = 0;
        while (i < chars.length)
        {
            if (chars[i] == '\\')
            {
                char c = chars[++i];

                // standard escape
                char d = escape_for_char(c);
                if (d != 0) {
                    b.append(d);
                    ++i;
                    continue;
                }

                // octal escape
                if (is_octal(c)) {
                    d = bite_octal(string.subSequence(i, len));
                    b.append(d);
                    i += octal_length(d);
                    continue;
                }

                // unicode escape
                if (c == 'u') {
                    int x = bite_unicode(string.subSequence(i, len));
                    b.append(x & 0xFFFF);
                    i += x >> 16;
                    continue;
                }
            }

            b.append(chars[i++]);
        }

        return b.toString();
    }

    // ---------------------------------------------------------------------------------------------

    private static long java_parse_integer (boolean is_long, String string, int radix)
    {
        return is_long
            ? Long.parseLong(string, radix)
            : Integer.parseInt(string, radix);
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * Given a an integer literal as may appear in Java source code, returns the corresponding
     * integer. An exception may be thrown if the literal is invalid (for instance, if it represents
     * a number that is too large).
     */
    public static long parse_int (String string)
    {
        string = string.replace("_", "");
        int lasti = string.length() - 1;
        char last = string.charAt(lasti);
        boolean is_long = false;

        if (last == 'l' || last == 'L') {
            string = string.substring(0, lasti);
            is_long = true;
        }

        int len = string.length();

        if (len == 0)
            throw new NumberFormatException();

        if (len == 1 || string.charAt(0) != '0')
            java_parse_integer(is_long, string, 10);

        switch (string.charAt(1)) {
            case 'x':
            case 'X':
                return java_parse_integer(is_long, string.substring(2), 16);
            case 'b':
            case 'B':
                return java_parse_integer(is_long, string.substring(2), 2);
            default:
                return java_parse_integer(is_long, string, 8);
        }
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * Given a floating number literal as may appear in Java source code, returns the corresponding
     * number. An exception may be thrown if the literal is invalid (for instance, if it represents
     * a number that is too large).
     */
    public static double parse_float (String string)
    {
        int lasti = string.length() - 1;
        char last = string.charAt(lasti);
        boolean is_float = last == 'f' || last == 'F';
        String str = string.replace("_", "");
        return is_float
            ? Float.parseFloat(str)
            : Double.parseDouble(str);
    }

    // ---------------------------------------------------------------------------------------------
}
