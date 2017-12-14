# java-lexer

- [Maven Dependency][jitpack]
- [Javadoc][javadoc]

[jitpack]: https://jitpack.io/#norswap/java-lexer
[javadoc]: https://jitpack.io/com/github/norswap/java-lexer/-SNAPSHOT/javadoc/

A [JLS][jls]-compliant yet simplistic lexer for the Java language. Focus is on ease of
implementation and understanding.

What is provided:

- [Classes][tokens] for the different types of input elements and tokens.

- An [unicode expander] which expands unicode escapes (e.g `\u0061`).

- A [lexer] that emits a list of input elements or tokens for a given input string.

- Upon lexing failure, choice between generating "garbage tokens" or throwing an exception.

- [Regexes][lexer] for all of Java's input elements, plus a few extras (lists of keywords, operators, etc).

- Various [utilities] dealing with escape handling and number parsing.

[tokens]: https://jitpack.io/com/github/norswap/java-lexer/-SNAPSHOT/javadoc/norswap/javalexer/tokens/package-summary.html
[unicode expander]: https://jitpack.io/com/github/norswap/java-lexer/-SNAPSHOT/javadoc/norswap/javalexer/UnicodeExpander.html
[lexer]: https://jitpack.io/com/github/norswap/java-lexer/-SNAPSHOT/javadoc/norswap/javalexer/Lexer.html
[utilities]: https://jitpack.io/com/github/norswap/java-lexer/-SNAPSHOT/javadoc/norswap/javalexer/LexUtil.html

## WARNING

This has only been tested very lightly. It's very likely bugs or outright breakages are still
lurking around. Use at your own risk!

## Installation 

If you are using Maven (or another popular JVM build tool), [see here][jitpack].

A self-contained JAR file is also available [here][jar].

[jar]: https://github.com/norswap/java-lexer/releases/tag/1.0.0

## Specification

The lexer follows the specification laid by [Chapter 3 of the Java Language Specification][jls]
for Java 9. This is largely compatible with all previous versions, with the following pitfalls:

- `_` (underscore) is a keyword since Java 9.
- Java 9 adds ten [restricted keywords] that are sometimes parsed as identifiers, sometimes as
  keywords (under some [conditions], in module declarations). For simplicity's sake, we always
  parse them as identifiers. This shouldn't be an issue in practice.
  
Looking as far back as Java 6, the following (backward-compatible) changes were made:

- `->` is a keyword since Java 8.
- `@` and `::` are separators since Java 7.
- Underscores may appear inside integer and floating-point literals since Java 7.
  
Links for easy reference:
[JLS6](https://docs.oracle.com/javase/specs/jls/se6/html/lexical.html),
[JLS7](https://docs.oracle.com/javase/specs/jls/se7/html/jls-3.html),
[JLS8](https://docs.oracle.com/javase/specs/jls/se8/html/jls-3.html),
[JLS9][jls]

[jls]: https://docs.oracle.com/javase/specs/jls/se9/html/jls-3.html
[conditions]: https://docs.oracle.com/javase/specs/jls/se9/html/jls-3.html#jls-3.9