package org.openspg.idea.schema.lexer;

import com.alibaba.fastjson.JSON;
import com.intellij.lexer.Lexer;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.openspg.idea.schema.psi.SchemaTypes.*;

public class SchemaLexerTest extends BasePlatformTestCase {

    @Override
    protected String getTestDataPath() {
        return new File("src/test/resources/testFixture").getAbsolutePath();
    }

    @Test
    public void testNamespace() {
        String text = """
                namespace LexerSample
                """;

        assertLinesMatch(
                prettier(List.of(
                        new SchemaToken(NAMESPACE_KEYWORD, "namespace"),
                        new SchemaToken(IDENTIFIER, "LexerSample")
                )),
                prettier(lexerTokens(text)),
                "Lexer: Namespace");
    }


    @Test
    public void testEntity() {
        String text = """
                Person(人物): EntityType
                    desc: "a great man"
                """;

        assertLinesMatch(
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
                prettier(lexerTokens(text)),
                "Lexer: Entity");
    }

    @Test
    public void testInheritedEntity() {
        String text = """
                Person(人物): EntityType
                    properties:
                        birth(生日): Text

                Artist(艺术家) -> Person:
                    desc: "a great man"
                """;

        assertLinesMatch(
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
                prettier(lexerTokens(text)),
                "Lexer: Inherited entity");
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

    private List<String> prettier(List<SchemaToken> tokens) {
        return tokens
                .stream()
                .map(Object::toString)
                .toList();
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
