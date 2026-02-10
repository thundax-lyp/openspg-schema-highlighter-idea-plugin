package org.openspg.idea.conceptRule.reference;

import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.openspg.idea.schema.psi.SchemaBasicStructureDeclaration;
import org.openspg.idea.schema.psi.SchemaStructureNameDeclaration;
import org.openspg.idea.schema.psi.SchemaStructureRealName;

public class ConceptRuleFindUsagesProvider implements FindUsagesProvider {

    @Override
    public WordsScanner getWordsScanner() {
        return null;
    }

    @Override
    public boolean canFindUsagesFor(@NotNull PsiElement element) {
        return element instanceof SchemaStructureNameDeclaration
                || element instanceof SchemaStructureRealName;
    }

    @Nullable
    @Override
    public String getHelpId(@NotNull PsiElement element) {
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
        if (element instanceof SchemaStructureNameDeclaration) {
            PsiElement declaration = PsiTreeUtil.findFirstParent(element, x -> x instanceof SchemaBasicStructureDeclaration);
            if (declaration != null) {
                return "***" + declaration.getText() + "***";
            }
        }
        return "";
    }
}
