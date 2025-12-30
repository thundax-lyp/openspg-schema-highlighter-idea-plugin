package org.openspg.idea.conceptRule.annotator.processor;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.common.annotator.AnnotateProcessor;
import org.openspg.idea.conceptRule.ConceptRuleBundle;
import org.openspg.idea.conceptRule.psi.ConceptRuleConceptRuleBody;
import org.openspg.idea.conceptRule.psi.ConceptRuleConceptRuleDeclaration;
import org.openspg.idea.conceptRule.psi.ConceptRuleConceptRuleItem;

/**
 * Validates schema entity nesting depth against a configured limit.
 */
public class ConceptRuleConceptRuleBodyProcessor implements AnnotateProcessor {

    private static final int THE_GRAPH_STRUCTURE_ORDER = 0;
    private static final int THE_RULE_ORDER = 1;

    public boolean process(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof ConceptRuleConceptRuleBody ruleBody) {
            boolean theGraphExists = false;
            boolean theRuleExists = false;
            boolean theActionExists = false;
            for (int idx = 0; idx < ruleBody.getConceptRuleItemList().size(); idx++) {
                ConceptRuleConceptRuleItem item = ruleBody.getConceptRuleItemList().get(idx);
                if (item.getTheGraphStructureDeclaration() != null) {
                    if (theGraphExists) {
                        this.emit(holder, item, "ConceptRuleAnnotator.error.theGraphStructureAlreadyDefined");
                    } else {
                        theGraphExists = true;

                        if (THE_GRAPH_STRUCTURE_ORDER != idx) {
                            this.emit(holder, item, "ConceptRuleAnnotator.error.theGraphStructureInBadOrder");
                        }
                    }

                } else if (item.getTheRuleDeclaration() != null) {
                    if (theRuleExists) {
                        this.emit(holder, item, "ConceptRuleAnnotator.error.theRuleAlreadyDefined");
                    } else {
                        theRuleExists = true;

                        if (THE_RULE_ORDER != idx) {
                            this.emit(holder, item,"ConceptRuleAnnotator.error.theRuleInBadOrder");
                        }
                    }

                } else if (item.getTheActionDeclaration() != null) {
                    if (theActionExists) {
                        this.emit(holder, item, "ConceptRuleAnnotator.error.theActionAlreadyDefined");
                    }
                    theActionExists = true;
                }
            }

            if (!theGraphExists) {
                ConceptRuleConceptRuleDeclaration declaration = PsiTreeUtil.getParentOfType(ruleBody, ConceptRuleConceptRuleDeclaration.class);
                if (declaration != null) {
                    this.emit(holder, declaration, "ConceptRuleAnnotator.error.theGraphStructureRequired");
                }
            }
        }
        return true;
    }

    private void emit(AnnotationHolder holder, final PsiElement element, String messageKey) {
        holder.newAnnotation(HighlightSeverity.ERROR, element.getText())
                .range(element.getTextRange())
                .tooltip(ConceptRuleBundle.message(messageKey))
                .create();
    }
}
