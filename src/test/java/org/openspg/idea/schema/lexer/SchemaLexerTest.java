package org.openspg.idea.schema.lexer;

import com.alibaba.fastjson.JSON;
import com.intellij.lexer.Lexer;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;

import java.io.File;
import java.util.*;

import static org.openspg.idea.schema.psi.SchemaTypes.*;

public class SchemaLexerTest extends BasePlatformTestCase {

    @Override
    protected String getTestDataPath() {
        return new File("src/test/resources/testFixture").getAbsolutePath();
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

        assertEquals(
                "Lexer: Namespace",
                prettier(List.of(
                        new SchemaToken(NAMESPACE_KEYWORD, "namespace"),
                        new SchemaToken(IDENTIFIER, "LexerSample")
                )),
                prettier(lexerTokens(text))
        );
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

        assertEquals(
                "Lexer: Entity",
                prettier(List.of(
                        // Person(人物): EntityType
                        new SchemaToken(IDENTIFIER, "Person"),
                        new SchemaToken(LPARENTH, "("),
                        new SchemaToken(IDENTIFIER, "人物"),
                        new SchemaToken(RPARENTH, ")"),
                        new SchemaToken(COLON, ":"),
                        new SchemaToken(ENTITY_TYPE_KEYWORD, "EntityType"),
                        // \t desc: "a great man"
                        new SchemaToken(INDENT, "    "),
                        new SchemaToken(DESC_KEYWORD, "desc"),
                        new SchemaToken(COLON, ":"),
                        new SchemaToken(STRING_LITERAL, "\"a great man\""),
                        new SchemaToken(DEDENT, "")
                )),
                prettier(lexerTokens(text))
        );
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

        assertEquals(
                "Lexer: Inherited entity",
                prettier(List.of(
                        // Person(人物): EntityType
                        new SchemaToken(IDENTIFIER, "Person"), new SchemaToken(LPARENTH, "("),
                        new SchemaToken(IDENTIFIER, "人物"),
                        new SchemaToken(RPARENTH, ")"),
                        new SchemaToken(COLON, ":"),
                        new SchemaToken(ENTITY_TYPE_KEYWORD, "EntityType"),
                        // \t properties:
                        new SchemaToken(INDENT, "    "),
                        new SchemaToken(PROPERTIES_KEYWORD, "properties"),
                        new SchemaToken(COLON, ":"),
                        // \t\t birth(生日): Text
                        new SchemaToken(INDENT, "        "),
                        new SchemaToken(IDENTIFIER, "birth"),
                        new SchemaToken(LPARENTH, "("),
                        new SchemaToken(IDENTIFIER, "生日"),
                        new SchemaToken(RPARENTH, ")"),
                        new SchemaToken(COLON, ":"),
                        new SchemaToken(TEXT_KEYWORD, "Text"),
                        new SchemaToken(DEDENT, ""),
                        new SchemaToken(DEDENT, ""),
                        // Artist(艺术家) -> Person:
                        new SchemaToken(IDENTIFIER, "Artist"),
                        new SchemaToken(LPARENTH, "("),
                        new SchemaToken(IDENTIFIER, "艺术家"),
                        new SchemaToken(RPARENTH, ")"),
                        new SchemaToken(RIGHT_ARROW, "->"),
                        new SchemaToken(IDENTIFIER, "Person"),
                        new SchemaToken(COLON, ":"),
                        // \t desc: "a great man"
                        new SchemaToken(INDENT, "    "),
                        new SchemaToken(DESC_KEYWORD, "desc"),
                        new SchemaToken(COLON, ":"),
                        new SchemaToken(STRING_LITERAL, "\"a great man\""),
                        new SchemaToken(DEDENT, "")
                )),
                prettier(lexerTokens(text))
        );
    }

    private static List<SchemaToken> lexerTokens(String text) {
        List<SchemaToken> tokens = new ArrayList<>();

        Lexer lexer = new SchemaLexerAdapter();
        lexer.start(text);
        while (lexer.getTokenType() != null) {
            IElementType type = lexer.getTokenType();
            if (type != TokenType.WHITE_SPACE && type != TokenType.NEW_LINE_INDENT) {
                tokens.add(new SchemaToken(type, lexer.getTokenText()));
            }
            lexer.advance();
        }

        return tokens;
    }

    private String prettier(List<SchemaToken> tokens) {
        return String.join("\n", tokens
                .stream()
                .map(Object::toString)
                .toList()
        );
    }

    public static class SchemaToken {
        public IElementType type;
        public String text;

        public SchemaToken(IElementType type, String text) {
            this.type = type;
            this.text = text;
        }

        public String toString() {
            Map<String, String> map = new LinkedHashMap<>();
            map.put("type", this.type.toString());
            map.put("text", this.text);
            return JSON.toJSONString(map);
        }
    }

}
