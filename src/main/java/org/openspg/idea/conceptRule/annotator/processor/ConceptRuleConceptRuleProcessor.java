package org.openspg.idea.conceptRule.annotator.processor;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.common.AnnotateProcessor;
import org.openspg.idea.conceptRule.ConceptRuleBundle;
import org.openspg.idea.conceptRule.psi.*;

import java.util.List;
import java.util.Objects;

/**
 * Validates schema entity nesting depth against a configured limit.
 */
public class ConceptRuleConceptRuleProcessor implements AnnotateProcessor {

    private static final int THE_GRAPH_STRUCTURE_ORDER = 0;

    public boolean process(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof ConceptRuleFile file) {
            handleConceptRuleFile(file, holder);

        } else if (element instanceof ConceptRuleRuleWrapperRuleDeclaration ruleWrapperRuleDeclaration) {
            handleConceptRuleDeclaration(ruleWrapperRuleDeclaration, holder);

        } else if (element instanceof ConceptRuleConceptRuleDeclaration conceptRuleDeclaration) {
            handleConceptRuleDeclaration(conceptRuleDeclaration, holder);
        }
        return true;
    }

    private void handleConceptRuleFile(ConceptRuleFile file, @NotNull AnnotationHolder holder) {
        detectRedeclaredConceptRule(
                PsiTreeUtil.getChildrenOfTypeAsList(file, ConceptRuleConceptRuleDeclaration.class),
                holder
        );
    }

    private void handleConceptRuleDeclaration(ConceptRuleRuleWrapperRuleDeclaration declaration, @NotNull AnnotationHolder holder) {
        detectRedeclaredConceptRule(
                declaration.getRuleWrapperRuleBody().getConceptRuleDeclarationList(),
                holder
        );
    }

    private void handleConceptRuleDeclaration(ConceptRuleConceptRuleDeclaration declaration, @NotNull AnnotationHolder holder) {
        long theGraphStructureCount = declaration
                .getConceptRuleBody()
                .getConceptRuleItemList()
                .stream()
                .map(ConceptRuleConceptRuleItem::getTheGraphStructureDeclaration)
                .filter(Objects::nonNull)
                .count();
        if (theGraphStructureCount == 0) {
            this.emitError(holder, declaration, "ConceptRuleAnnotator.error.theGraphStructureRequired");
        }

        boolean theGraphExists = false;
        boolean theRuleExists = false;
        boolean theActionExists = false;

        List<ConceptRuleConceptRuleItem> items = declaration.getConceptRuleBody().getConceptRuleItemList();
        for (int idx = 0; idx < items.size(); idx++) {
            ConceptRuleConceptRuleItem item = items.get(idx);
            if (item.getTheGraphStructureDeclaration() != null) {
                if (theGraphExists) {
                    this.emitError(holder, item, "ConceptRuleAnnotator.error.theGraphStructureAlreadyDefined");
                } else {
                    theGraphExists = true;

                    if (THE_GRAPH_STRUCTURE_ORDER != idx) {
                        this.emitError(holder, item, "ConceptRuleAnnotator.error.theGraphStructureInBadOrder");
                    }
                }

            } else if (item.getTheRuleDeclaration() != null) {
                if (theRuleExists) {
                    this.emitError(holder, item, "ConceptRuleAnnotator.error.theRuleAlreadyDefined");
                } else {
                    theRuleExists = true;
                    if (idx > 0 && items.get(idx - 1).getTheGraphStructureDeclaration() == null) {
                        this.emitError(holder, item, "ConceptRuleAnnotator.error.theRuleInBadOrder");
                    }
                }

            } else if (item.getTheActionDeclaration() != null) {
                if (theActionExists) {
                    this.emitError(holder, item, "ConceptRuleAnnotator.error.theActionAlreadyDefined");
                }
                theActionExists = true;
            }
        }
    }

    private void detectRedeclaredConceptRule(List<ConceptRuleConceptRuleDeclaration> conceptRules, AnnotationHolder holder) {
        List<String> conceptRuleNames = conceptRules
                .stream()
                .map(x -> x.getConceptRuleHead().getText().replaceAll("\\s", ""))
                .toList();

        for (int idx = 0; idx < conceptRules.size(); idx++) {
            String conceptRuleName = conceptRuleNames.get(idx);
            long existCount = conceptRuleNames.stream().filter(x -> x.equals(conceptRuleName)).count();
            if (existCount > 1) {
                this.emitError(holder, conceptRules.get(idx).getConceptRuleHead(), "ConceptRuleAnnotator.error.redeclaredBlockScopedConceptRule");
            }
        }
    }

    private void emitError(AnnotationHolder holder, final PsiElement element, String messageKey, Object... messageParams) {
        holder.newAnnotation(HighlightSeverity.ERROR, element.getText())
                .range(element.getTextRange())
                .tooltip(ConceptRuleBundle.message(messageKey, messageParams))
                .create();
    }

}
