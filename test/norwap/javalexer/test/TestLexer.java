package norwap.javalexer.test;

import norswap.javalexer.Lexer;
import norswap.javalexer.tokens.*;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;
import static org.testng.Assert.assertEquals;

public final class TestLexer
{
    // ---------------------------------------------------------------------------------------------

    private void test_lex (String input, InputElement... expected)
    {
        assertEquals(Lexer.lex(input), Arrays.asList(expected));
    }

    // ---------------------------------------------------------------------------------------------

    @Test public void test_lexer_single()
    {
        test_lex("hello",
            new Identifier("hello"));
        test_lex("αρετη",
            new Identifier("αρετη"));
        test_lex("class",
            new Keyword("class"));
        test_lex("int",
            new Keyword("int"));
        test_lex("true",
            new BoolLiteral(true));
        test_lex("false",
            new BoolLiteral(false));
        test_lex("null",
            new NullLiteral());
        test_lex("//hello",
            new Comment("//hello", Comment.Type.LINE));
        test_lex("/** hello /*  // hi */",
            new Comment("/** hello /*  // hi */", Comment.Type.BLOCK));
        test_lex("+=",
            new Operator("+="));
        test_lex("+",
            new Operator("+"));
        test_lex(",",
            new Separator(","));
        test_lex("::",
            new Separator("::"));
        test_lex("'a'",
            new CharLiteral("a"));
        test_lex("'\\n'",
            new CharLiteral("\\n"));
        test_lex("'\\011'",
            new CharLiteral("\\011"));
        test_lex("\"hello\"",
            new StringLiteral("hello"));
        test_lex("\" \\n\\011 \"",
            new StringLiteral(" \\n\\011 "));
        test_lex("1337",
            new IntLiteral("1337", IntLiteral.Type.DECIMAL));
        test_lex("034",
            new IntLiteral("034", IntLiteral.Type.OCTAL));
        test_lex("0b1111",
            new IntLiteral("0b1111", IntLiteral.Type.BINARY));
        test_lex("0xDEADB33F",
            new IntLiteral("0xDEADB33F", IntLiteral.Type.HEXADECIMAL));
        test_lex("13__37",
            new IntLiteral("13__37", IntLiteral.Type.DECIMAL));
        test_lex("0xDEAD__B33F",
            new IntLiteral("0xDEAD__B33F", IntLiteral.Type.HEXADECIMAL));
        test_lex("1337L",
            new IntLiteral("1337L", IntLiteral.Type.DECIMAL));
        test_lex("13.37",
            new FloatLiteral("13.37", FloatLiteral.Type.DECIMAL));
        test_lex("13.37F",
            new FloatLiteral("13.37F", FloatLiteral.Type.DECIMAL));
        test_lex("13.",
            new FloatLiteral("13.", FloatLiteral.Type.DECIMAL));
        test_lex("13e2",
            new FloatLiteral("13e2", FloatLiteral.Type.DECIMAL));
        test_lex("13D",
            new FloatLiteral("13D", FloatLiteral.Type.DECIMAL));
        test_lex("0x13p37",
            new FloatLiteral("0x13p37", FloatLiteral.Type.HEXADECIMAL));
        test_lex("1_3.3__7e-42",
            new FloatLiteral("1_3.3__7e-42", FloatLiteral.Type.DECIMAL));
    }

    // NOTE(norswap)
    //
    // This needs more extensive testing, namely for combinations of token.
    //
    // Random generation testing should probably be used: generate a random token lists,
    // splice it to text, lex it and verify the resulting lists matches the source list.
    //
    // However, this is not the most productive use of my time right now.

    // ---------------------------------------------------------------------------------------------

    /**
     * Lightweight validation that the code works for more than single tokens.
     */
    public static void main (String[] args) throws IOException
    {
        String content = new String(readAllBytes(get("src/norswap/javalexer/Lexer.java")));
        List<InputElement> result = Lexer.lex(content);
        System.out.println(result);
    }

    // ---------------------------------------------------------------------------------------------
}
