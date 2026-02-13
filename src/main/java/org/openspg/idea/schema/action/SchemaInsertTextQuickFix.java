package org.openspg.idea.schema.action;

import com.intellij.codeInsight.intention.impl.BaseIntentionAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.schema.SchemaBundle;

public class SchemaInsertTextQuickFix extends BaseIntentionAction {

    private final int offset;
    private final String text;
    private final String label;

    public SchemaInsertTextQuickFix(int offset, String text, String label) {
        this.offset = offset;
        this.text = text;
        this.label = label;
    }

    @NotNull
    @Override
    public String getText() {
        return label;
    }

    @NotNull
    @Override
    public String getFamilyName() {
        return SchemaBundle.message("SchemaQuickFix.family");
    }

    @Override
    public boolean isAvailable(@NotNull Project project, com.intellij.openapi.editor.Editor editor, PsiFile file) {
        return file != null && file.isWritable() && offset >= 0;
    }

    @Override
    public void invoke(@NotNull Project project, com.intellij.openapi.editor.Editor editor, PsiFile file) {
        Document document = PsiDocumentManager.getInstance(project).getDocument(file);
        if (document == null) {
            return;
        }
        int safeOffset = Math.min(offset, document.getTextLength());
        WriteCommandAction.runWriteCommandAction(project, () -> {
            document.insertString(safeOffset, text);
            PsiDocumentManager.getInstance(project).commitDocument(document);
        });
    }
}
