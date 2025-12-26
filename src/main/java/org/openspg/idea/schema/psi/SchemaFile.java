package org.openspg.idea.schema.psi;


import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.schema.SchemaFileType;
import org.openspg.idea.schema.SchemaLanguage;

public class SchemaFile extends PsiFileBase {

    public SchemaFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, SchemaLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return SchemaFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "OpenSPG Schema File";
    }

}
