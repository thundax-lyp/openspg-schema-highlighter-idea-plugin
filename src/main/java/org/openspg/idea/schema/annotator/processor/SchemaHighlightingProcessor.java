package org.openspg.idea.schema.annotator.processor;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.common.annotator.AnnotateProcessor;
import org.openspg.idea.schema.highlighter.SchemaHighlightingColors;
import org.openspg.idea.schema.psi.*;

/**
 * Adds text-attribute annotations for key schema PSI elements.
 */
public class SchemaHighlightingProcessor implements AnnotateProcessor {

    /**
     * Resolves highlight styles for the element and emits annotations when matched.
     */
    public boolean process(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {
        TextAttributesKey textAttributesKey = null;

        if (element instanceof SchemaStructureSemanticName) {
            textAttributesKey = SchemaHighlightingColors.KEYWORD;

        } else if (element instanceof SchemaStructureRealName) {
            textAttributesKey = SchemaHighlightingColors.ENTITY_NAME;

        } else if (element instanceof SchemaStructureAliasDeclaration) {
            textAttributesKey = SchemaHighlightingColors.ENTITY_ALIAS;

        } else if (element instanceof SchemaStructureName
                && PsiTreeUtil.getParentOfType(element, SchemaStructureTypeDeclaration.class) != null) {
            textAttributesKey = SchemaHighlightingColors.ENTITY_REFERENCE;
        }

        if (textAttributesKey != null) {
            holder.newAnnotation(HighlightSeverity.TEXT_ATTRIBUTES, element.getText())
                    .range(element.getTextRange())
                    .textAttributes(textAttributesKey)
                    .create();
        }

        return true;
    }

}
