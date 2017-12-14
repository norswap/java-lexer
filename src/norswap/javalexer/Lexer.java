package norswap.javalexer;

import norswap.javalexer.tokens.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.DOTALL;
import static java.util.regex.Pattern.compile;

/**
 * Static methods to lex Java input, regexes for all types of input elements defined by the JLS,
 * and a few extras.
 */
public final class Lexer
{
    // ---------------------------------------------------------------------------------------------
    /* Summary of unusual regex features used:

      . = any char but not a newline
      \\d = digits [0-9]
      \\s = whistepsace chars [ \t\n\x0B\f\r]
      \\p{XDigit} = hexadecimal digits [a-fA-F0-9]
      \\p{L} = unicode letters
      \\p{Nl} = unicode numbers that look like letters (e.g. roman numerals)
      \\p{Sc} = unicode currency symbols
      \\p{Pc} = unicode connector punctuation (like '_')
      \\p{Mc} = unicode spacing mark
      \\p{Mn} = unicode non-spacing mark
      \\p{Cf} = unicode format characters
      DOTALL    = make dots match newlines as well
      (?: = start of non-capturing group
      *?/+? = reluctant repetition (prefers match with least repetitions)
      *+/++ = possessive repetition (forces failure if cannot succeed with all possible repetitions)

      Note: all backlashes must be doubled when compared to a regular Java string.
      So, escaped regex characters look like '\\*' (escaping '*').
      '\\\\' is used to escape a single backslash.

     */
    // ---------------------------------------------------------------------------------------------
    // Non-Tokens Input Elements
    // ---------------------------------------------------------------------------------------------

    /**
     * Matches strings of whitespace characters: spaces, tabs, newlines, ...
     */
    public static final Pattern whitespace = compile("\\s*");

    // ---------------------------------------------------------------------------------------------

    /**
     * Matches a line comment (starting with {@code //}), including its terminating newline, if
     * available.
     */
    public static final Pattern line_comment = compile("//(?:.*)\n?");

    // ---------------------------------------------------------------------------------------------

    /**
     * Matches a block comment (starting with {@code /*}).
     */
    public static final Pattern block_comment = compile("/\\*.*?\\*/", DOTALL);

    // ---------------------------------------------------------------------------------------------

    private static final String[] keywordsa = new String[] {
        "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const",
        "continue", "default", "do", "double", "else", "enum", "extends", "final", "finally",
        "float", "for", "if", "goto", "implements", "import", "instanceof", "int", "interface",
        "long", "native", "new", "package", "private", "protected", "public", "return", "short",
        "static", "strictfp", "super", "switch", "synchronized", "this", "throw", "throws",
        "transient", "try", "void", "volatile", "while", "_" };

    // ---------------------------------------------------------------------------------------------

    /**
     * An immutable list of keywords in the Java language.
     */
    public static final List<String> keywords
        = Collections.unmodifiableList(Arrays.asList(keywordsa));

    // ---------------------------------------------------------------------------------------------

    private static final String[] restricted = new String[] {
        "open", "module", "requires", "transitive", "exports", "opens",
        "to", "uses", "provides", "with" };

    // ---------------------------------------------------------------------------------------------

    /**
     * An immutable list of restricted keywords (keywords that should sometimes be parsed as
     * identifiers, sometimes as keywords). For simplicity's sake, we always parse them as
     * identifiers -- this shouldn't be an issue in practice.
     */
    public static final List<String> restricted_keywords
        = Collections.unmodifiableList(Arrays.asList(restricted));

    // ---------------------------------------------------------------------------------------------

    private static final String idcommon = "\\p{L}\\p{Nl}\\p{Sc}\\p{Pc}";

    // ---------------------------------------------------------------------------------------------

    /**
     * Regex string that matches a java identifier start character.
     * @see Character#isJavaIdentifierStart(char)
     */
    public static final String identifier_start = "[" + idcommon + "]";

    // ---------------------------------------------------------------------------------------------

     /**
      * Regex string that matches a java identifier character (but not the start character).
      * @see Character#isJavaIdentifierPart(char)
      */
    public static final String identifier_part = "[" + idcommon
         + "\\d\\p{Mc}\\p{Mn}\\p{Cf}[\u0000-\u0008][\u000e-\u001b][\u007F-\u009F]]";

    // ---------------------------------------------------------------------------------------------

    /**
     * Matches java identifiers.
     */
    public static final Pattern identifier = compile(identifier_start + identifier_part + "*");

    // ---------------------------------------------------------------------------------------------

    private static final String[] separatorsa = new String[] {
        "(", ")", "{", "}", "[", "]", ";", ",", ".", "...", "@", "::" };

    // ---------------------------------------------------------------------------------------------

    /**
     * An immutable list of separators in the Java language.
     */
    public static final List<String> separators
        = Collections.unmodifiableList(Arrays.asList(separatorsa));

    // ---------------------------------------------------------------------------------------------

    private static final String[] operatorsa = new String[] {
        "=", ">", "<", "!", "~", "?", ":", "->", "==", "<=", ">=", "!=", "&&", "||", "++", "--", "+", "-",
        "*", "/", "&", "|", "^", "%", "<<", ">>", ">>>", "+=", "-=", "*=", "/=", "&=", "|=", "^=",
        "%=", "<<=", ">>=", ">>>=" };

    // ---------------------------------------------------------------------------------------------

    /**
     * An immutable list of operators in the Java language.
     */
    public static final List<String> operators =
        Collections.unmodifiableList(Arrays.asList(operatorsa));

    // ---------------------------------------------------------------------------------------------
    // Integer Literals

    private static final String digits = "(?:\\d+(?:_*\\d++)*)";
    private static final String hexd   = "(?:\\p{XDigit}++(?:_*\\p{XDigit}++)*)";

    /** Matches decimal integer literals. */
    public static final Pattern dec_int_lit = compile("0|[1-9]+(?:_*\\d++)*[lL]?");

    /** Matches hexadecimal integer literals. */
    public static final Pattern hex_int_lit = compile("0[xX]" + hexd + "[lL]?");

    /** Matches octal integer literals. */
    public static final Pattern oct_int_lit = compile("0(?:_*[0-7]++)+[lL]?");

    /** Matches binary integer literals. */
    public static final Pattern bin_int_lit = compile("0[bB](?:_*[01]++)+");

    // ---------------------------------------------------------------------------------------------
    // Floating-Point Literals

    private static final String exp_part
        = "(?:[eE][+-]?" + digits + ")";

    private static final String hex_significand
        = "0[xX](?:" + hexd + "(?:\\." + hexd + "?)?" + "|\\." + hexd + ")";

    /**
     * Matches decimal floating-point literals.
     */
    public static final Pattern fp_lit
        = compile(digits + "\\." + digits + "?" + exp_part + "?[fFdD]?"
                + "|\\." + digits + "(?:[eE][+-]?" + digits + ")?[fFdD]?"
                + "|" + digits + exp_part + "[fFdD]?"
                + "|" + digits + exp_part + "?[fFdD]");

    /**
     * Matches hexadecimal floating-point literals.
     */
    public static final Pattern hex_fp_lit
        = compile(hex_significand + "[pP][+-]?" + digits + "[fFdD]?");

    // ---------------------------------------------------------------------------------------------
    // Other Literals

    private static final String escape = "(?"
        + ":\\\\[btnfr\"'\\\\]"
        + "|\\\\[0-7][0-7]?"
        + "|\\\\[0-3][0-7][0-7])";

    /** Matches a character literal. */
    public static final Pattern char_lit = compile("'(?:[^\n\r'\\\\]|" + escape + ")'");

    /** Matches a string literal. */
    public static final Pattern string_lit = compile("\"(?:[^\n\r\"\\\\]|" + escape + ")*\"");

    /** Matches a boolean literal. */
    public static final Pattern bool_lit = compile("true|false");

    /** Matches the null literal. */
    public static final Pattern null_lit = compile("null");

    // ---------------------------------------------------------------------------------------------

    /**
     * Input Element Factory: pairs a pattern with a function that instantiates the corrsponding
     * input element when matched.
     */
    private final static class IEFactory
    {
        final Pattern pattern;
        final Function<Matcher, InputElement> f;

        IEFactory (Pattern pattern, Function<Matcher, InputElement> f) {
            this.pattern = pattern;
            this.f = f;
        }
    }

    // ---------------------------------------------------------------------------------------------

    private static IEFactory factory (Pattern pattern, Function<String, InputElement> f)
    {
        return new IEFactory(pattern, m -> {
            InputElement ie = f.apply(m.group());
            ie.start = m.start();
            ie.end   = m.end();
            return ie;
        });
    }

    // ---------------------------------------------------------------------------------------------

    private static IEFactory strip_quotes (Pattern pattern, Function<String, InputElement> f)
    {
        return new IEFactory(pattern, m -> {
            String match = m.group();
            InputElement ie = f.apply(match.substring(1, match.length() - 1));
            ie.start = m.start();
            ie.end   = m.end();
            return ie;
        });
    }

    // ---------------------------------------------------------------------------------------------

    private static final IEFactory[] factories;
    static {
        ArrayList<IEFactory> list = new ArrayList<>();

        list.add(factory(whitespace,    Whitespace::new));
        list.add(factory(line_comment,  it -> new Comment(it, Comment.Type.LINE)));
        list.add(factory(block_comment, it -> new Comment(it, Comment.Type.BLOCK)));

        list.add(factory(dec_int_lit,   it -> new IntLiteral(it, IntLiteral.Type.DECIMAL)));
        list.add(factory(hex_int_lit,   it -> new IntLiteral(it, IntLiteral.Type.HEXADECIMAL)));
        list.add(factory(oct_int_lit,   it -> new IntLiteral(it, IntLiteral.Type.OCTAL)));
        list.add(factory(bin_int_lit,   it -> new IntLiteral(it, IntLiteral.Type.BINARY)));

        list.add(factory(fp_lit,        it -> new FloatLiteral(it, FloatLiteral.Type.DECIMAL)));
        list.add(factory(hex_fp_lit,    it -> new FloatLiteral(it, FloatLiteral.Type.HEXADECIMAL)));

        list.add(strip_quotes(char_lit,     CharLiteral::new));
        list.add(strip_quotes(string_lit,   StringLiteral::new));
        list.add(factory(bool_lit,          it -> new BoolLiteral(it.equals("true"))));
        list.add(factory(null_lit,          it -> new NullLiteral()));

        for (String kw: keywordsa)
            list.add(factory(compile(kw), Keyword::new));

        // After keywords!
        list.add(factory(identifier,    Identifier::new));

        for (String sep: separatorsa)
            list.add(factory(compile(Pattern.quote(sep)), it -> new Separator(sep)));

        for (String op: operatorsa)
            list.add(factory(compile(Pattern.quote(op)), Operator::new));

        factories = list.toArray(new IEFactory[0]);
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * Returns a list of input elements obtained by lexing {@code string},
     * according to JLS chapter 3.
     *
     * @param tokens_only if true, the returned list will only include tokens, not other input
     *                    elements.
     *
     * @param tolerant if true, the function may emit {@link Garbage} tokens whenever it is unable
     *                 to match any valid input element; otherwise a {@link LexingException}
     *                 is thrown.
     */
    private static List<InputElement> lex (String string, boolean tokens_only, boolean tolerant)
    {
        int i   = 0;
        int len = string.length();
        int top = -1;
        int max = 0;

        String garbage = "";
        ArrayList<InputElement> out = new ArrayList<>();
        Matcher[] matchers = new Matcher[factories.length];

        for (int j = 0; j < factories.length; ++j)
            matchers[j] = factories[j].pattern.matcher(string);

        while (i < len)
        {
            for (int j = 0; j < factories.length; ++j)
            {
                IEFactory factory = factories[j];
                Matcher   matcher = matchers[j];

                matcher.region(i, len);

                if (matcher.lookingAt()) {
                    int span = matcher.end() - matcher.start();
                    if (span > max) {
                        max = span;
                        top = j;
                    }
                }
            }

            if (top < 0) {
                max = 1;
                if (tolerant)
                    garbage += string.charAt(i);
                else
                    throw new LexingException(i);
            } else {
                if (garbage.length() > 0) {
                    out.add(new Garbage(garbage));
                    garbage = "";
                }
                InputElement ie = factories[top].f.apply(matchers[top]);
                if (!tokens_only || ie instanceof Token)
                    out.add(ie);
            }

            i += max;
            top = -1;
            max = 0;
        }

        if (garbage.length() > 0)
            out.add(new Garbage(garbage));

        return out;
    }

    // ---------------------------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    private static <T> T cast (Object item) {
        return (T) item;
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * Returns a list of tokens (but not other input elements such as whitespace) obtained by lexing
     * {@code string}, according to JLS chapter 3.
     * <p>
     * The function may emit {@link Garbage} tokens whenever it is unable to match any valid input
     * element.
     */
    public static List<Token> tokenize (String string) {
        return cast(lex(string, true, true));
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * Returns a list of input elements obtained by lexing {@code string},
     * according to JLS chapter 3.
     * <p>
     * The function may emit {@link Garbage} tokens whenever it is unable to match any valid input
     * element.
     */
    public static List<InputElement> lex (String string) {
        return lex(string, false, true);
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * Returns a list of tokens (but not other input elements such as whitespace) obtained by lexing
     * {@code string}, according to JLS chapter 3.
     *
     * @param tolerant if true, the function may emit {@link Garbage} tokens whenever it is unable
     *                 to match any valid input element; otherwise a {@link LexingException}
     *                 is thrown.
     */
    public static List<Token> tokenize (String string, boolean tolerant) {
        return cast(lex(string, true, tolerant));
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * Returns a list of input elements obtained by lexing {@code string},
     * according to JLS chapter 3.
     *
     * @param tolerant if true, the function may emit {@link Garbage} tokens whenever it is unable
     *                 to match any valid input element; otherwise a {@link LexingException}
     *                 is thrown.
     */
    public static List<InputElement> lex (String string, boolean tolerant) {
        return lex(string, false, tolerant);
    }

    // ---------------------------------------------------------------------------------------------
}
