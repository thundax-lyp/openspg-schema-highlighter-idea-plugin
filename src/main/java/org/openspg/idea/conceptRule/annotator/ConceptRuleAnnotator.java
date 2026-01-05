package org.openspg.idea.conceptRule.annotator;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.common.annotator.AnnotateProcessor;
import org.openspg.idea.conceptRule.annotator.processor.ConceptRuleConceptRuleProcessor;
import org.openspg.idea.conceptRule.annotator.processor.ConceptRuleHighlightingProcessor;

import java.util.HashSet;
import java.util.Set;

final class ConceptRuleAnnotator implements Annotator {

    private final Set<AnnotateProcessor> myProcessors;

    public ConceptRuleAnnotator() {
        myProcessors = new HashSet<>();
        myProcessors.add(new ConceptRuleHighlightingProcessor());
        myProcessors.add(new ConceptRuleConceptRuleProcessor());
    }

    @Override
    public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {
        for (AnnotateProcessor processor : myProcessors) {
            if (!processor.process(element, holder)) {
                return;
            }
        }
    }
}
