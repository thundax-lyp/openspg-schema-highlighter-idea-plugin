package org.openspg.idea.conceptRule.highlighter;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.openspg.idea.conceptRule.ConceptRuleBundle;
import org.openspg.idea.conceptRule.demo.ConceptRuleDemo;
import org.openspg.idea.schema.SchemaIcons;

import javax.swing.*;
import java.util.Map;

import static org.openspg.idea.conceptRule.highlighter.ConceptRuleHighlightingColors.*;

final class ConceptRuleColorSettingsPage implements ColorSettingsPage {

    private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[]{
            new AttributesDescriptor(
                    ConceptRuleBundle.message("ConceptRuleColorSettings.keywords"), KEYWORD),
            new AttributesDescriptor(
                    ConceptRuleBundle.message("ConceptRuleColorSettings.comments.block.comment"), BLOCK_COMMENT),
            new AttributesDescriptor(
                    ConceptRuleBundle.message("ConceptRuleColorSettings.comments.line.comment"), LINE_COMMENT),
            new AttributesDescriptor(
                    ConceptRuleBundle.message("ConceptRuleColorSettings.error"), ERROR),

            new AttributesDescriptor(
                    ConceptRuleBundle.message("ConceptRuleColorSettings.namespace.key"), NAMESPACE_KEY),
            new AttributesDescriptor(
                    ConceptRuleBundle.message("ConceptRuleColorSettings.namespace.value"), NAMESPACE_VALUE),

            new AttributesDescriptor(
                    ConceptRuleBundle.message("ConceptRuleColorSettings.conceptRule.pattern"), WRAPPER_PATTERN),
            new AttributesDescriptor(
                    ConceptRuleBundle.message("ConceptRuleColorSettings.conceptRule.category"), WRAPPER_FIELD),

            new AttributesDescriptor(
                    ConceptRuleBundle.message("ConceptRuleColorSettings.number"), NUMBER),
            new AttributesDescriptor(
                    ConceptRuleBundle.message("ConceptRuleColorSettings.string"), STRING),
            new AttributesDescriptor(
                    ConceptRuleBundle.message("ConceptRuleColorSettings.variables"), VARIABLES),
            new AttributesDescriptor(
                    ConceptRuleBundle.message("ConceptRuleColorSettings.function"), FUNCTION),

            new AttributesDescriptor(
                    ConceptRuleBundle.message("ConceptRuleColorSettings.bracesAndOperators.braces"), BRACES),
            new AttributesDescriptor(
                    ConceptRuleBundle.message("ConceptRuleColorSettings.bracesAndOperators.brackets"), BRACKETS),
            new AttributesDescriptor(
                    ConceptRuleBundle.message("ConceptRuleColorSettings.bracesAndOperators.comma"), COMMA),
            new AttributesDescriptor(
                    ConceptRuleBundle.message("ConceptRuleColorSettings.bracesAndOperators.dot"), DOT),
            new AttributesDescriptor(
                    ConceptRuleBundle.message("ConceptRuleColorSettings.bracesAndOperators.operator.sign"), OPERATION_SIGN),
            new AttributesDescriptor(
                    ConceptRuleBundle.message("ConceptRuleColorSettings.bracesAndOperators.parentheses"), PARENTHESES),
            new AttributesDescriptor(
                    ConceptRuleBundle.message("ConceptRuleColorSettings.bracesAndOperators.semicolon"), SEMICOLON),
    };

    @Override
    public Icon getIcon() {
        return SchemaIcons.FILE;
    }

    @NotNull
    @Override
    public SyntaxHighlighter getHighlighter() {
        return new ConceptRuleSyntaxHighlighter();
    }

    @NotNull
    @Override
    public String getDemoText() {
        return ConceptRuleDemo.getHighlighterText();
    }

    @Nullable
    @Override
    public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        return null;
    }

    @Override
    public AttributesDescriptor @NotNull [] getAttributeDescriptors() {
        return DESCRIPTORS;
    }

    @Override
    public ColorDescriptor @NotNull [] getColorDescriptors() {
        return ColorDescriptor.EMPTY_ARRAY;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "OpenSPG Concept Rule";
    }

}
