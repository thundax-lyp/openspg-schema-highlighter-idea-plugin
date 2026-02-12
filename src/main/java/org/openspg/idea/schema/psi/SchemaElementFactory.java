package org.openspg.idea.schema.psi;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.util.PsiTreeUtil;
import org.openspg.idea.schema.SchemaFileType;

public class SchemaElementFactory {

    public static SchemaFile createFile(Project project, String text) {
        String fileName = "dummy.schema";
        return (SchemaFile) PsiFileFactory.getInstance(project).createFileFromText(
                fileName,
                SchemaFileType.INSTANCE,
                text
        );
    }

    public static SchemaStructureNameDeclaration createStructureNameDeclaration(Project project, String name) {
        String sourceCode = "namespace Dummy\n" + name + "(\"a\"): EntityType\n";
        SchemaFile file = createFile(project, sourceCode);
        return PsiTreeUtil.findChildOfType(file, SchemaStructureNameDeclaration.class);
    }

}
