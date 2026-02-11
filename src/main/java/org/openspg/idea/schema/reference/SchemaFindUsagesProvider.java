package org.openspg.idea.schema.reference;

import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.openspg.idea.schema.psi.*;

public class SchemaFindUsagesProvider implements FindUsagesProvider {

    @Override
    public WordsScanner getWordsScanner() {
        return null;
    }

    @Override
    public boolean canFindUsagesFor(@NotNull PsiElement element) {
        return element instanceof SchemaStructureNameDeclaration;
    }

    @Nullable
    @Override
    public String getHelpId(@NotNull PsiElement element) {
        return null;
    }

    @NotNull
    @Override
    public String getType(@NotNull PsiElement element) {
        return "Schema";
    }

    @NotNull
    @Override
    public String getDescriptiveName(@NotNull PsiElement element) {
        SchemaBasicStructureDeclaration declaration = PsiTreeUtil.getParentOfType(element, SchemaBasicStructureDeclaration.class);
        if (declaration != null) {
            return declaration.getText();
        }
        return "";
    }

    @NotNull
    @Override
    public String getNodeText(@NotNull PsiElement element, boolean useFullName) {
        SchemaBasicStructureDeclaration declaration = PsiTreeUtil.getParentOfType(element, SchemaBasicStructureDeclaration.class);
        if (declaration != null) {
            return declaration.getText();
        }
        return "";
    }
}
