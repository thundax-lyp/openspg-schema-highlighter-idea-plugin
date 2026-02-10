package org.openspg.idea.conceptRule.reference;

import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.*;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.conceptRule.ConceptRuleLanguage;
import org.openspg.idea.conceptRule.psi.ConceptRuleSchemaName;

public class ConceptRuleReferenceContributor extends PsiReferenceContributor {

    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(
                PlatformPatterns.psiElement(ConceptRuleSchemaName.class).withLanguage(ConceptRuleLanguage.INSTANCE),
                new PsiReferenceProvider() {
                    @Override
                    public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement psiElement, @NotNull ProcessingContext processingContext) {
                        if (psiElement instanceof ConceptRuleSchemaName schemaName) {
                            return new PsiReference[]{schemaName.getReference()};
                        }
                        return PsiReference.EMPTY_ARRAY;
                    }
                });
    }
}
