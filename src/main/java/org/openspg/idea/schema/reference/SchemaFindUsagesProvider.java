package org.openspg.idea.schema.reference;

import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.openspg.idea.schema.psi.*;

import java.util.List;

public class SchemaFindUsagesProvider implements FindUsagesProvider {

    @Override
    public WordsScanner getWordsScanner() {
        return null;
    }

    @Override
    public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
        if (psiElement instanceof SchemaStructureNameDeclaration) {
            List<SchemaEntity> entities = PsiTreeUtil.collectParents(psiElement, SchemaEntity.class, false, x -> false);
            return entities.size() == 1;
        }
        return false;
    }

    @Nullable
    @Override
    public String getHelpId(@NotNull PsiElement psiElement) {
        return null;
    }

    @NotNull
    @Override
    public String getType(@NotNull PsiElement element) {
        if (element instanceof SchemaStructureNameDeclaration) {
            return "Entity";
        }
        return "";
    }

    @NotNull
    @Override
    public String getDescriptiveName(@NotNull PsiElement element) {
        if (element instanceof SchemaStructureNameDeclaration declaration) {
            String name = declaration.getName();
            if (name != null) {
                return name;
            }
        }
        return "";
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
