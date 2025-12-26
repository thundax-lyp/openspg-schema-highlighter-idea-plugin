package org.openspg.idea.schema.action;


import com.intellij.codeInsight.intention.impl.BaseIntentionAction;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.schema.SchemaFileType;

import java.util.Collection;

// TODO
class SchemaCreatePropertyQuickFix extends BaseIntentionAction {

    private final String key;

    SchemaCreatePropertyQuickFix(String key) {
        this.key = key;
    }

    @NotNull
    @Override
    public String getText() {
        return "Create property '" + key + "'";
    }

    @NotNull
    @Override
    public String getFamilyName() {
        return "Create property";
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
        return true;
    }

    @Override
    public void invoke(@NotNull final Project project, final Editor editor, PsiFile file) throws
            IncorrectOperationException {
        ApplicationManager.getApplication().invokeLater(() -> {
            Collection<VirtualFile> virtualFiles =
                    FileTypeIndex.getFiles(SchemaFileType.INSTANCE, GlobalSearchScope.allScope(project));
            if (virtualFiles.size() == 1) {
                createProperty(project, virtualFiles.iterator().next());
            } else {
                final FileChooserDescriptor descriptor =
                        FileChooserDescriptorFactory.createSingleFileDescriptor(SchemaFileType.INSTANCE);
                descriptor.setRoots(ProjectUtil.guessProjectDir(project));
                final VirtualFile file1 = FileChooser.chooseFile(descriptor, project, null);
                if (file1 != null) {
                    createProperty(project, file1);
                }
            }
        });
    }

    private void createProperty(final Project project, final VirtualFile file) {
        //WriteCommandAction.writeCommandAction(project).run(() -> {
        //    SchemaFile simpleFile = (SchemaFile) PsiManager.getInstance(project).findFile(file);
        //    assert simpleFile != null;
        //    ASTNode lastChildNode = simpleFile.getNode().getLastChildNode();
        //    // TODO: Add another check for CRLF
        //    if (lastChildNode != null/* && !lastChildNode.getElementType().equals(SimpleTypes.CRLF)*/) {
        //        simpleFile.getNode().addChild(SchemaElementFactory.createCRLF(project).getNode());
        //    }
        //    // IMPORTANT: change spaces to escaped spaces or the new node will only have the first word for the key
        //    SchemaEntity property = SchemaElementFactory.createProperty(project, key.replaceAll(" ", "\\\\ "), "");
        //    simpleFile.getNode().addChild(property.getNode());
        //    ((Navigatable) property.getLastChild().getNavigationElement()).navigate(true);
        //    Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        //    assert editor != null;
        //    editor.getCaretModel().moveCaretRelatively(2, 0, false, false, false);
        //});
    }

}
