package org.openspg.idea.schema.lexer;

import org.openspg.idea.common.AbstractLexerTestCase;

import java.util.*;

import static org.openspg.idea.schema.psi.SchemaTypes.*;

public class SchemaLexerTest extends AbstractLexerTestCase {

    public SchemaLexerTest() {
        super(new SchemaLexerAdapter());
    }

    /**
     * Scenario: single-line namespace declaration
     * Focus: keyword and identifier tokenization
     * Assert: token sequence and content match
     */
    public void testNamespace() {
        String text = """
                namespace LexerSample
                """;

        assertTokens(text, List.of(
                new CommonToken(NAMESPACE_KEYWORD, "namespace"),
                new CommonToken(IDENTIFIER, "LexerSample")
        ));
    }


    /**
     * Scenario: entity head with a first-level property
     * Focus: entity head tokens and indent/string tokens
     * Assert: token sequence matches indentation levels
     */
    public void testEntity() {
        String text = """
                Person(人物): EntityType
                    desc: "a great man"
                """;

        assertTokens(text, List.of(
                // Person(人物): EntityType
                new CommonToken(IDENTIFIER, "Person"),
                new CommonToken(LPARENTH, "("),
                new CommonToken(IDENTIFIER, "人物"),
                new CommonToken(RPARENTH, ")"),
                new CommonToken(COLON, ":"),
                new CommonToken(ENTITY_TYPE_KEYWORD, "EntityType"),
                // \t desc: "a great man"
                new CommonToken(INDENT, "    "),
                new CommonToken(DESC_KEYWORD, "desc"),
                new CommonToken(COLON, ":"),
                new CommonToken(STRING_LITERAL, "\"a great man\""),
                new CommonToken(DEDENT, "")
        ));
    }

    /**
     * Scenario: multi-level indentation with inherited entity
     * Focus: INDENT/DEDENT pairing and inheritance arrow
     * Assert: token sequence is complete and ordered
     */
    public void testInheritedEntity() {
        String text = """
                Person(人物): EntityType
                    properties:
                        birth(生日): Text
                
                Artist(艺术家) -> Person:
                    desc: "a great man"
                """;

        assertTokens(text, List.of(
                // Person(人物): EntityType
                new CommonToken(IDENTIFIER, "Person"), new CommonToken(LPARENTH, "("),
                new CommonToken(IDENTIFIER, "人物"),
                new CommonToken(RPARENTH, ")"),
                new CommonToken(COLON, ":"),
                new CommonToken(ENTITY_TYPE_KEYWORD, "EntityType"),
                // \t properties:
                new CommonToken(INDENT, "    "),
                new CommonToken(PROPERTIES_KEYWORD, "properties"),
                new CommonToken(COLON, ":"),
                // \t\t birth(生日): Text
                new CommonToken(INDENT, "        "),
                new CommonToken(IDENTIFIER, "birth"),
                new CommonToken(LPARENTH, "("),
                new CommonToken(IDENTIFIER, "生日"),
                new CommonToken(RPARENTH, ")"),
                new CommonToken(COLON, ":"),
                new CommonToken(TEXT_KEYWORD, "Text"),
                new CommonToken(DEDENT, ""),
                new CommonToken(DEDENT, ""),
                // Artist(艺术家) -> Person:
                new CommonToken(IDENTIFIER, "Artist"),
                new CommonToken(LPARENTH, "("),
                new CommonToken(IDENTIFIER, "艺术家"),
                new CommonToken(RPARENTH, ")"),
                new CommonToken(RIGHT_ARROW, "->"),
                new CommonToken(IDENTIFIER, "Person"),
                new CommonToken(COLON, ":"),
                // \t desc: "a great man"
                new CommonToken(INDENT, "    "),
                new CommonToken(DESC_KEYWORD, "desc"),
                new CommonToken(COLON, ":"),
                new CommonToken(STRING_LITERAL, "\"a great man\""),
                new CommonToken(DEDENT, "")
        ));
    }

    /**
     * Scenario: mixed indentation styles at same logical level
     * Focus: tab+space should be aligned with five spaces
     * Assert: no extra INDENT is emitted between sibling properties
     */
    public void testMixedIndentAtSameLevel() {
        String text = """
                Person(人物): EntityType
                \t desc: "v1"
                     index: text
                """;

        assertTokens(text, List.of(
                new CommonToken(IDENTIFIER, "Person"),
                new CommonToken(LPARENTH, "("),
                new CommonToken(IDENTIFIER, "人物"),
                new CommonToken(RPARENTH, ")"),
                new CommonToken(COLON, ":"),
                new CommonToken(ENTITY_TYPE_KEYWORD, "EntityType"),

                new CommonToken(INDENT, "\t "),
                new CommonToken(DESC_KEYWORD, "desc"),
                new CommonToken(COLON, ":"),
                new CommonToken(STRING_LITERAL, "\"v1\""),

                new CommonToken(INDEX_KEYWORD, "index"),
                new CommonToken(COLON, ":"),
                new CommonToken(TEXT_KEYWORD, "text"),
                new CommonToken(DEDENT, "")
        ));
    }

}
