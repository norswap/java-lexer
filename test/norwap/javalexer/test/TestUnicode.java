package norwap.javalexer.test;

import org.testng.annotations.Test;

import static norswap.javalexer.UnicodeExpander.expand;
import static org.testng.Assert.assertEquals;

public final class TestUnicode
{
    @Test public void test_unicode_expansion()
    {
        assertEquals(expand(""), "");
        assertEquals(expand("a"), "a");
        assertEquals(expand("abc"), "abc");
        assertEquals(expand("\\u0061"), "a");
        assertEquals(expand("\\uuu0061"), "a");
        assertEquals(expand("\\\\\\u0061"), "\\\\a");
        assertEquals(expand("\\\\u0061"), "\\\\u0061");
        assertEquals(expand("\\u061"), "\\u061");
        assertEquals(expand("\\u0061\\u0062\\u0063"), "abc");
        assertEquals(expand("\\u0061\\u0062\\u0063def"), "abcdef");
        assertEquals(expand("def\\u0061\\u0062\\u0063"), "defabc");
        assertEquals(expand("def\\u0061\\u0062\\u0063ghi"), "defabcghi");
        assertEquals(expand("\\u0061\\u0062\\u0063def\\u0067\\u0068\\u0069"), "abcdefghi");
    }
}
