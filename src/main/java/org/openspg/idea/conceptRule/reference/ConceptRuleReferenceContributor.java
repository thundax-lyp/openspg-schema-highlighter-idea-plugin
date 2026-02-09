package org.openspg.idea.conceptRule.reference;

import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.*;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.conceptRule.ConceptRuleLanguage;
import org.openspg.idea.conceptRule.psi.ConceptRuleConceptType;

public class ConceptRuleReferenceContributor extends PsiReferenceContributor {

    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(
                PlatformPatterns.psiElement(ConceptRuleConceptType.class).withLanguage(ConceptRuleLanguage.INSTANCE),
                new PsiReferenceProvider() {
                    @Override
                    public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement psiElement, @NotNull ProcessingContext processingContext) {
                        if (psiElement instanceof ConceptRuleConceptType conceptType) {
                            return new PsiReference[]{conceptType.getReference()};
                        }
                        return PsiReference.EMPTY_ARRAY;
                    }
                });
    }
}
