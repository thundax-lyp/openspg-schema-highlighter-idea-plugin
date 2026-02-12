package org.openspg.idea.schema.action;

import com.intellij.codeInsight.intention.impl.BaseIntentionAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.schema.SchemaBundle;

public class SchemaRemoveDuplicateNamespaceQuickFix extends BaseIntentionAction {

    private final TextRange range;

    public SchemaRemoveDuplicateNamespaceQuickFix(TextRange range) {
        this.range = range;
    }

    @NotNull
    @Override
    public String getText() {
        return SchemaBundle.message("SchemaQuickFix.remove.duplicate.namespace");
    }

    @NotNull
    @Override
    public String getFamilyName() {
        return SchemaBundle.message("SchemaQuickFix.family");
    }

    @Override
    public boolean isAvailable(@NotNull Project project, com.intellij.openapi.editor.Editor editor, PsiFile file) {
        return file != null && file.isWritable() && range != null;
    }

    @Override
    public void invoke(@NotNull Project project, com.intellij.openapi.editor.Editor editor, PsiFile file) {
        Document document = PsiDocumentManager.getInstance(project).getDocument(file);
        if (document == null) {
            return;
        }
        WriteCommandAction.runWriteCommandAction(project, () -> {
            int start = range.getStartOffset();
            int end = Math.min(range.getEndOffset() + 1, document.getTextLength());
            document.deleteString(start, end);
            PsiDocumentManager.getInstance(project).commitDocument(document);
        });
    }
}
