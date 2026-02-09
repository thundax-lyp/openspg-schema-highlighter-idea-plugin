package org.openspg.idea.conceptRule.reference;

import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.openspg.idea.conceptRule.psi.ConceptRuleConceptType;
import org.openspg.idea.schema.psi.SchemaBasicStructureDeclaration;
import org.openspg.idea.schema.psi.SchemaStructureNameDeclaration;

public class ConceptRuleFindUsagesProvider implements FindUsagesProvider {

    @Override
    public WordsScanner getWordsScanner() {
        return null;
    }

    @Override
    public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
        return psiElement instanceof ConceptRuleConceptType;
    }

    @Nullable
    @Override
    public String getHelpId(@NotNull PsiElement psiElement) {
        return null;
    }

    @NotNull
    @Override
    public String getType(@NotNull PsiElement element) {
        return "";
    }

    @NotNull
    @Override
    public String getDescriptiveName(@NotNull PsiElement element) {
        return element.getText();
    }

    @NotNull
    @Override
    public String getNodeText(@NotNull PsiElement element, boolean useFullName) {
        System.out.println("getNodeText: " + element + " " + useFullName);
        if (element instanceof SchemaStructureNameDeclaration) {
            PsiElement declaration = PsiTreeUtil.findFirstParent(element, x -> x instanceof SchemaBasicStructureDeclaration);
            if (declaration != null) {
                return declaration.getText();
            }
        }
        return "";
    }
}
