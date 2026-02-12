package org.openspg.idea.schema.action;

import com.intellij.codeInsight.intention.impl.BaseIntentionAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.schema.SchemaBundle;

public class SchemaAddNamespaceQuickFix extends BaseIntentionAction {

    @NotNull
    @Override
    public String getText() {
        return SchemaBundle.message("SchemaQuickFix.add.namespace");
    }

    @NotNull
    @Override
    public String getFamilyName() {
        return SchemaBundle.message("SchemaQuickFix.family");
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
        return file != null && file.isWritable();
    }

    @Override
    public void invoke(@NotNull Project project, Editor editor, PsiFile file) {
        Document document = PsiDocumentManager.getInstance(project).getDocument(file);
        if (document == null) {
            return;
        }
        String namespace = guessNamespace(file);
        String prefix = "namespace " + namespace + "\n";
        WriteCommandAction.runWriteCommandAction(project, () -> {
            document.insertString(0, prefix);
            PsiDocumentManager.getInstance(project).commitDocument(document);
        });
    }

    private static String guessNamespace(PsiFile file) {
        VirtualFile virtualFile = file.getVirtualFile();
        if (virtualFile == null) {
            return "Default";
        }
        String name = virtualFile.getNameWithoutExtension();
        if (name.isBlank()) {
            return "Default";
        }
        return name.replaceAll("\\s+", "_");
    }
}
