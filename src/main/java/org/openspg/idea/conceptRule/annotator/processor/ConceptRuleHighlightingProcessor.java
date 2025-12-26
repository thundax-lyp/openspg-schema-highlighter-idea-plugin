package org.openspg.idea.conceptRule.annotator.processor;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.common.annotator.AnnotateProcessor;
import org.openspg.idea.conceptRule.ConceptRuleBundle;
import org.openspg.idea.conceptRule.highlighter.ConceptRuleHighlightingColors;
import org.openspg.idea.conceptRule.psi.*;

import java.util.List;

public class ConceptRuleHighlightingProcessor implements AnnotateProcessor {

    @Override
    public boolean process(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {
        TextAttributesKey textAttributesKey = null;

        if (element instanceof ConceptRuleLabelName
                && PsiTreeUtil.getParentOfType(element, ConceptRuleRuleWrapperDeclaration.class) != null) {
            textAttributesKey = ConceptRuleHighlightingColors.WRAPPER_PATTERN;

        } else if (element instanceof ConceptRuleFunctionName) {
            textAttributesKey = ConceptRuleHighlightingColors.FUNCTION;
        }

        if (textAttributesKey != null) {
            holder.newAnnotation(HighlightSeverity.TEXT_ATTRIBUTES, element.getText())
                    .range(element.getTextRange())
                    .textAttributes(textAttributesKey)
                    .create();
        }

        if (element instanceof ConceptRuleLabelPropertyList) {
            return this.checkLabelPropertyList((ConceptRuleLabelPropertyList) element, holder);
        }

        return true;
    }

    private boolean checkLabelPropertyList(@NotNull final ConceptRuleLabelPropertyList labelPropertyList, @NotNull AnnotationHolder holder) {
        List<ConceptRuleLabelProperty> labelProperties = labelPropertyList.getLabelPropertyList();
        for (int i = 1; i < labelProperties.size(); i++) {
            ConceptRuleLabelProperty prevElement = labelProperties.get(i - 1);
            ConceptRuleLabelProperty currElement = labelProperties.get(i);
            // label-name should be before property-expression
            if (currElement.getLabelName() != null && prevElement.getPropertyExpression() != null) {
                holder.newAnnotation(HighlightSeverity.ERROR, currElement.getText())
                        .range(currElement.getTextRange())
                        .tooltip(ConceptRuleBundle.message("ConceptRuleAnnotator.error.badLabelPropertyExpressionOrder"))
                        .create();
                return false;
            }
        }
        return true;
    }

}
