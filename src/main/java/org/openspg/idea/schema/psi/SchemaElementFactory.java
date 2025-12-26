package org.openspg.idea.schema.psi;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.tree.IElementType;
import org.openspg.idea.schema.SchemaFileType;

public class SchemaElementFactory {

    public static SchemaFile createFile(Project project, String text) {
        String fileName = "dummy.schema";
        return (SchemaFile) PsiFileFactory.getInstance(project).createFileFromText(
                fileName,
                SchemaFileType.INSTANCE,
                text);
    }

    public static IElementType createEntityName(Project project, String name) {
        //String sourceCode = "namespace DUMMY\n"
        //        + "\n"
        //        + name + "(文本块):EntityType\n"
        //        + "\n";
        //final SchemaFile file = createFile(project, sourceCode);
        //file.findChildByClass(SchemaEntityInfo.class);
        //return (SchemaEntityInfo) file.getFirstChild();
        throw new IllegalArgumentException("Not implemented yet");
    }

}
