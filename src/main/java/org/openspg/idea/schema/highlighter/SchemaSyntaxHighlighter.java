package org.openspg.idea.schema.highlighter;


import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.schema.lexer.SchemaLexerAdapter;
import org.openspg.idea.schema.psi.SchemaTypes;

import java.util.HashMap;
import java.util.Map;

import static org.openspg.idea.schema.psi.impl.BasicElementTypes.BASIC_KEYWORD_BIT_SET;
import static org.openspg.idea.schema.psi.impl.BasicElementTypes.BASIC_OPERATION_BIT_SET;


public class SchemaSyntaxHighlighter extends SyntaxHighlighterBase {

    private final Map<IElementType, TextAttributesKey> ourMap;

    public SchemaSyntaxHighlighter() {
        ourMap = new HashMap<>();

        fillMap(ourMap, BASIC_KEYWORD_BIT_SET, SchemaHighlightingColors.KEYWORD);
        fillMap(ourMap, BASIC_OPERATION_BIT_SET, SchemaHighlightingColors.OPERATION_SIGN);

        ourMap.put(SchemaTypes.LINE_COMMENT, SchemaHighlightingColors.COMMENT);

        ourMap.put(TokenType.BAD_CHARACTER, HighlighterColors.BAD_CHARACTER);
    }

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new SchemaLexerAdapter();
    }

    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        return pack(ourMap.get(tokenType));
    }

}
