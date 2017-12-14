package norswap.javalexer;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Contains a method that expands unicode escapes in a string, as per JLS 3.3.
 */
public final class UnicodeExpander
{
    // ---------------------------------------------------------------------------------------------

    private static final Pattern escape = Pattern.compile("(\\\\*)(\\\\u+(\\p{XDigit}{4}))");

    // ---------------------------------------------------------------------------------------------

    /**
     * Expands the unicode escapes in a source string, as per JLS 3.3.
     */
    public static String expand (String string)
    {
        Matcher matcher = escape.matcher(string);
        ArrayList<int[]> replacements = new ArrayList<>();

        while (matcher.find())
        {
            // Total number of backslashes is even, don't replace this escape.
            if ((matcher.end(1) - matcher.start(1)) % 2 == 1)
                continue;

            String escape = matcher.group(3);
            int total = 0;
            for (int i = 0; i < 4; ++i)
                total = total * 16 + LexUtil.hex_value(escape.charAt(i));

            replacements.add(new int[]{ matcher.start(2), matcher.end(2), total });
        }

        if (replacements.size() == 0)
            return string;

        int i = 0;
        StringBuilder b = new StringBuilder();

        for (int[] r: replacements) {
            b.append(string, i, r[0]);
            b.append((char) r[2]);
            i = r[1];
        }

        b.append(string, i, string.length());
        return b.toString();
    }

    // ---------------------------------------------------------------------------------------------
}
