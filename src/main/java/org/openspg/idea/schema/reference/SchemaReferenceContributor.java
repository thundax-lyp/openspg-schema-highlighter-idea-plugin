package org.openspg.idea.schema.reference;

import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceRegistrar;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.schema.SchemaLanguage;
import org.openspg.idea.schema.psi.SchemaStructureName;
import org.openspg.idea.schema.psi.SchemaVariableStructureType;

public class SchemaReferenceContributor extends PsiReferenceContributor {

    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(
                PlatformPatterns.psiElement(SchemaStructureName.class).withLanguage(SchemaLanguage.INSTANCE),
                new PsiReferenceProvider() {
                    @Override
                    public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement psiElement, @NotNull ProcessingContext processingContext) {
                        if (psiElement instanceof SchemaStructureName && psiElement.getParent() instanceof SchemaVariableStructureType variable) {
                            return new PsiReference[]{variable.getReference()};
                        }
                        return PsiReference.EMPTY_ARRAY;
                    }
                });
    }
}
