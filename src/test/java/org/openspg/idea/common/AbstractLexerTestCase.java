package org.openspg.idea.common;

import com.alibaba.fastjson.JSON;
import com.intellij.lexer.FlexAdapter;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractLexerTestCase extends BasePlatformTestCase {

    protected final FlexAdapter myLexer;

    public AbstractLexerTestCase(FlexAdapter lexer) {
        myLexer = lexer;
    }

    protected void assertTokens(String input, List<CommonToken> expectedTokens) {
        List<CommonToken> actualTokens = lexerTokens(input);

        assertEquals(
                prettier(actualTokens),
                prettier(expectedTokens)
        );
    }

    private List<CommonToken> lexerTokens(String text) {
        List<CommonToken> tokens = new ArrayList<>();

        myLexer.start(text);
        while (myLexer.getTokenType() != null) {
            IElementType type = myLexer.getTokenType();
            if (type != TokenType.WHITE_SPACE && type != TokenType.NEW_LINE_INDENT) {
                tokens.add(new CommonToken(type, myLexer.getTokenText()));
            }
            myLexer.advance();
        }

        return tokens;
    }

    private String prettier(List<CommonToken> tokens) {
        return String.join("\n", tokens
                .stream()
                .map(Object::toString)
                .toList()
        );
    }

    public static class CommonToken {
        public IElementType type;
        public String text;

        public CommonToken(IElementType type, String text) {
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
