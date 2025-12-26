package org.openspg.idea.schema.highlighter;

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;

/**
 * Highlighting text attributes for Schema Marker Language.
 */
public class SchemaHighlightingColors {

    public static final TextAttributesKey KEYWORD =
            TextAttributesKey.createTextAttributesKey("OpenSGP_SCHEMA_KEYWORDS", DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey COMMENT =
            TextAttributesKey.createTextAttributesKey("OpenSGP_SCHEMA_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
    public static final TextAttributesKey ERROR =
            TextAttributesKey.createTextAttributesKey("OpenSGP_SCHEMA_ERROR", HighlighterColors.BAD_CHARACTER);

    public static final TextAttributesKey OPERATION_SIGN =
            TextAttributesKey.createTextAttributesKey("OpenSGP_SCHEMA_OPERATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN);

    public static final TextAttributesKey ENTITY_NAME =
            TextAttributesKey.createTextAttributesKey("OpenSGP_SCHEMA_ENTITY_NAME", DefaultLanguageHighlighterColors.CLASS_NAME);
    public static final TextAttributesKey ENTITY_ALIAS =
            TextAttributesKey.createTextAttributesKey("OpenSGP_SCHEMA_ENTITY_ALIAS", DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey ENTITY_REFERENCE =
            TextAttributesKey.createTextAttributesKey("OpenSGP_SCHEMA_ENTITY_REFERENCE", DefaultLanguageHighlighterColors.CLASS_REFERENCE);

}
