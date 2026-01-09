package org.openspg.idea.conceptRule.highlighter;


import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.conceptRule.lexer.ConceptRuleLexerAdapter;
import org.openspg.idea.conceptRule.psi.ConceptRuleTypes;

import java.util.HashMap;
import java.util.Map;

import static org.openspg.idea.conceptRule.psi.impl.BasicElementTypes.*;


public class ConceptRuleSyntaxHighlighter extends SyntaxHighlighterBase {

    private final Map<IElementType, TextAttributesKey> ourMap;

    public ConceptRuleSyntaxHighlighter() {
        ourMap = new HashMap<>();

        fillMap(ourMap, BASIC_KEYWORD_BIT_SET, ConceptRuleHighlightingColors.KEYWORD);
        fillMap(ourMap, BASIC_LITERAL_BIT_SET, ConceptRuleHighlightingColors.KEYWORD);
        fillMap(ourMap, BASIC_OPERATION_BIT_SET, ConceptRuleHighlightingColors.OPERATION_SIGN);

        ourMap.put(ConceptRuleTypes.INTEGER_LITERAL, ConceptRuleHighlightingColors.NUMBER);
        ourMap.put(ConceptRuleTypes.FLOAT_LITERAL, ConceptRuleHighlightingColors.NUMBER);
        ourMap.put(ConceptRuleTypes.STRING_LITERAL, ConceptRuleHighlightingColors.STRING);

        ourMap.put(ConceptRuleTypes.ESCAPED_SYMBOLIC_NAME, ConceptRuleHighlightingColors.VARIABLES);
        ourMap.put(ConceptRuleTypes.UNESCAPED_SYMBOLIC_NAME, ConceptRuleHighlightingColors.VARIABLES);

        ourMap.put(ConceptRuleTypes.LPARENTH, ConceptRuleHighlightingColors.PARENTHESES);
        ourMap.put(ConceptRuleTypes.RPARENTH, ConceptRuleHighlightingColors.PARENTHESES);

        ourMap.put(ConceptRuleTypes.LBRACE, ConceptRuleHighlightingColors.BRACES);
        ourMap.put(ConceptRuleTypes.RBRACE, ConceptRuleHighlightingColors.BRACES);

        ourMap.put(ConceptRuleTypes.LBRACKET, ConceptRuleHighlightingColors.BRACKETS);
        ourMap.put(ConceptRuleTypes.RBRACKET, ConceptRuleHighlightingColors.BRACKETS);

        ourMap.put(ConceptRuleTypes.COMMA, ConceptRuleHighlightingColors.COMMA);
        ourMap.put(ConceptRuleTypes.DOT, ConceptRuleHighlightingColors.DOT);
        ourMap.put(ConceptRuleTypes.SEMICOLON, ConceptRuleHighlightingColors.SEMICOLON);

        ourMap.put(ConceptRuleTypes.NAMESPACE_KEYWORD, ConceptRuleHighlightingColors.NAMESPACE_KEY);
        ourMap.put(ConceptRuleTypes.NAMESPACE_VARIABLE, ConceptRuleHighlightingColors.NAMESPACE_VALUE);

        ourMap.put(ConceptRuleTypes.WRAPPER_RULE_KEYWORD, ConceptRuleHighlightingColors.WRAPPER_FIELD);

        ourMap.put(ConceptRuleTypes.LINE_COMMENT, ConceptRuleHighlightingColors.LINE_COMMENT);
        ourMap.put(ConceptRuleTypes.BLOCK_COMMENT, ConceptRuleHighlightingColors.BLOCK_COMMENT);

        ourMap.put(TokenType.BAD_CHARACTER, HighlighterColors.BAD_CHARACTER);
    }


    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new ConceptRuleLexerAdapter();
    }

    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        return pack(ourMap.get(tokenType));
    }

}
