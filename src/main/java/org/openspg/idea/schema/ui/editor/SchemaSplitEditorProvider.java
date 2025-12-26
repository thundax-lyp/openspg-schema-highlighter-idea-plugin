package org.openspg.idea.schema.ui.editor;

import com.intellij.openapi.editor.event.CaretEvent;
import com.intellij.openapi.editor.event.CaretListener;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorPolicy;
import com.intellij.openapi.fileEditor.FileEditorProvider;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.fileEditor.impl.text.TextEditorProvider;
import com.intellij.openapi.fileTypes.FileTypeRegistry;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.schema.SchemaFileType;
import org.openspg.idea.schema.psi.SchemaEntity;

import java.util.Objects;

public class SchemaSplitEditorProvider implements FileEditorProvider, DumbAware {

    public SchemaSplitEditorProvider() {

    }

    @Override
    public boolean accept(@NotNull Project project, @NotNull VirtualFile file) {
        return isSchemaFileType(project, file);
    }

    // @Override
    public boolean acceptRequiresReadAction() {
        return false;
    }

    @Override
    public @NotNull FileEditor createEditor(@NotNull Project project, @NotNull VirtualFile file) {
        TextEditor editor = (TextEditor) TextEditorProvider.getInstance().createEditor(project, file);
        SchemaPreviewEditor previewEditor = (SchemaPreviewEditor) SchemaPreviewEditorProvider.getInstance().createEditor(project, file);

        editor.getEditor().getCaretModel().addCaretListener(new CaretListener() {
            public void caretPositionChanged(@NotNull CaretEvent event) {
                int offset = Objects.requireNonNull(event.getCaret()).getOffset();
                PsiFile psiFile = PsiManager.getInstance(project).findFile(file);
                if (psiFile == null) {
                    return;
                }

                PsiElement element = psiFile.findElementAt(offset);
                if (element == null) {
                    return;
                }

                SchemaEntity entity = PsiTreeUtil.getParentOfType(element, SchemaEntity.class);
                if (entity == null) {
                    return;
                }

                String entityName = entity.getEntityHead().getBasicStructureDeclaration().getStructureNameDeclaration().getText();
                if (entityName != null && !entityName.isBlank()) {
                    previewEditor.activateEntity(entityName);
                }
            }
        });

        return new SchemaSplitEditor(editor, previewEditor);
    }

    @Override
    public @NotNull String getEditorTypeId() {
        return "openspg-schema-split-editor";
    }

    @Override
    public @NotNull FileEditorPolicy getPolicy() {
        return FileEditorPolicy.HIDE_DEFAULT_EDITOR;
    }

    public static boolean isSchemaFileType(@NotNull Project project, @NotNull VirtualFile file) {
        if (file.isDirectory() || !file.exists()) {
            return false;
        }

        if (project.isDisposed()) {
            return false;
        }
        return FileTypeRegistry.getInstance().isFileOfType(file, SchemaFileType.INSTANCE);
    }

}
