package org.openspg.idea.schema.action;

import com.intellij.codeInsight.intention.impl.BaseIntentionAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.schema.SchemaBundle;

public class SchemaCreateTypeQuickFix extends BaseIntentionAction {

    private final String typeName;

    public SchemaCreateTypeQuickFix(String typeName) {
        this.typeName = typeName;
    }

    @NotNull
    @Override
    public String getText() {
        return SchemaBundle.message("SchemaQuickFix.create.type", typeName);
    }

    @NotNull
    @Override
    public String getFamilyName() {
        return SchemaBundle.message("SchemaQuickFix.family");
    }

    @Override
    public boolean isAvailable(@NotNull Project project, com.intellij.openapi.editor.Editor editor, PsiFile file) {
        return file != null && file.isWritable();
    }

    @Override
    public void invoke(@NotNull Project project, com.intellij.openapi.editor.Editor editor, PsiFile file) {
        Document document = PsiDocumentManager.getInstance(project).getDocument(file);
        if (document == null) {
            return;
        }
        String name = sanitize(typeName);
        String snippet = "\n" + name + "(\"\") : EntityType\n";
        WriteCommandAction.runWriteCommandAction(project, () -> {
            document.insertString(document.getTextLength(), snippet);
            PsiDocumentManager.getInstance(project).commitDocument(document);
        });
    }

    private static String sanitize(String raw) {
        if (raw == null || raw.isBlank()) {
            return "NewEntity";
        }
        return raw.trim().replaceAll("\\s+", "_");
    }
}
