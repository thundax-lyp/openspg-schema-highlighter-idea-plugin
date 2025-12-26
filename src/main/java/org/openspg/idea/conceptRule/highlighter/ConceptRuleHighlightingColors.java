package org.openspg.idea.conceptRule.highlighter;

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;

/**
 * Highlighting text attributes for Schema Marker Language.
 */
public class ConceptRuleHighlightingColors {

    public static final TextAttributesKey ERROR =
            TextAttributesKey.createTextAttributesKey("OpenSGP_RULE_ERROR", HighlighterColors.BAD_CHARACTER);

    public static final TextAttributesKey KEYWORD =
            TextAttributesKey.createTextAttributesKey("OpenSGP_RULE_KEYWORDS", DefaultLanguageHighlighterColors.KEYWORD);

    public static final TextAttributesKey LINE_COMMENT =
            TextAttributesKey.createTextAttributesKey("OpenSGP_RULE_LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
    public static final TextAttributesKey BLOCK_COMMENT =
            TextAttributesKey.createTextAttributesKey("OpenSGP_RULE_BLOCK_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT);

    public static final TextAttributesKey NAMESPACE_KEY =
            TextAttributesKey.createTextAttributesKey("OpenSGP_RULE_NAMESPACE_KEY", DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey NAMESPACE_VALUE =
            TextAttributesKey.createTextAttributesKey("OpenSGP_RULE_NAMESPACE_VALUE", DefaultLanguageHighlighterColors.LOCAL_VARIABLE);

    public static final TextAttributesKey WRAPPER_PATTERN =
            TextAttributesKey.createTextAttributesKey("OpenSGP_RULE_WRAPPER_PATTERN", DefaultLanguageHighlighterColors.CLASS_NAME);

    public static final TextAttributesKey WRAPPER_FIELD =
            TextAttributesKey.createTextAttributesKey("OpenSGP_RULE_WRAPPER_FIELD", DefaultLanguageHighlighterColors.INSTANCE_METHOD);

    public static final TextAttributesKey NUMBER =
            TextAttributesKey.createTextAttributesKey("OpenSGP_RULE_NUMBER", DefaultLanguageHighlighterColors.NUMBER);
    public static final TextAttributesKey STRING =
            TextAttributesKey.createTextAttributesKey("OpenSGP_RULE_STRING", DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey VARIABLES =
            TextAttributesKey.createTextAttributesKey("OpenSGP_RULE_VARIABLES", DefaultLanguageHighlighterColors.LOCAL_VARIABLE);
    public static final TextAttributesKey FUNCTION =
            TextAttributesKey.createTextAttributesKey("OpenSGP_RULE_FUNCTION", DefaultLanguageHighlighterColors.FUNCTION_CALL);

    public static final TextAttributesKey BRACES =
            TextAttributesKey.createTextAttributesKey("OpenSGP_RULE_BRACES", DefaultLanguageHighlighterColors.BRACES);
    public static final TextAttributesKey BRACKETS =
            TextAttributesKey.createTextAttributesKey("OpenSGP_RULE_BRACKETS", DefaultLanguageHighlighterColors.BRACKETS);
    public static final TextAttributesKey COMMA =
            TextAttributesKey.createTextAttributesKey("OpenSGP_RULE_COMMA", DefaultLanguageHighlighterColors.COMMA);
    public static final TextAttributesKey DOT =
            TextAttributesKey.createTextAttributesKey("OpenSGP_RULE_DOT", DefaultLanguageHighlighterColors.DOT);
    public static final TextAttributesKey OPERATION_SIGN =
            TextAttributesKey.createTextAttributesKey("OpenSGP_RULE_OPERATION_SIGN", DefaultLanguageHighlighterColors.OPERATION_SIGN);
    public static final TextAttributesKey PARENTHESES =
            TextAttributesKey.createTextAttributesKey("OpenSGP_RULE_PARENTHESES", DefaultLanguageHighlighterColors.PARENTHESES);
    public static final TextAttributesKey SEMICOLON =
            TextAttributesKey.createTextAttributesKey("OpenSGP_RULE_SEMICOLON", DefaultLanguageHighlighterColors.SEMICOLON);

}
