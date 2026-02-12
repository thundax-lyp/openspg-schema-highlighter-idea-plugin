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

public class SchemaRenameEntityQuickFix extends BaseIntentionAction {

    private final TextRange range;
    private final String newName;

    public SchemaRenameEntityQuickFix(TextRange range, String newName) {
        this.range = range;
        this.newName = newName;
    }

    @NotNull
    @Override
    public String getText() {
        return SchemaBundle.message("SchemaQuickFix.rename.entity", newName);
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
            document.replaceString(range.getStartOffset(), range.getEndOffset(), newName);
            PsiDocumentManager.getInstance(project).commitDocument(document);
        });
    }
}
