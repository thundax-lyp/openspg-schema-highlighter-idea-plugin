package org.openspg.idea.conceptRule.psi;


import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.conceptRule.ConceptRuleFileType;
import org.openspg.idea.conceptRule.ConceptRuleLanguage;

public class ConceptRuleFile extends PsiFileBase {

    public ConceptRuleFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, ConceptRuleLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return ConceptRuleFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "OpenSPG Concept Rule File";
    }

}
