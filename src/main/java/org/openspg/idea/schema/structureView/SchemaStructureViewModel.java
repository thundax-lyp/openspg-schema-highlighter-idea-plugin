package org.openspg.idea.schema.structureView;

import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.StructureViewModelBase;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.openspg.idea.schema.psi.SchemaEntityHead;
import org.openspg.idea.schema.structureView.viewElement.SchemaFileStructureViewElement;

public class SchemaStructureViewModel extends StructureViewModelBase implements StructureViewModel.ElementInfoProvider {

    public SchemaStructureViewModel(@Nullable Editor editor, PsiFile psiFile) {
        super(psiFile, editor, new SchemaFileStructureViewElement(psiFile));
    }

    @NotNull
    public Sorter @NotNull [] getSorters() {
        return new Sorter[]{Sorter.ALPHA_SORTER};
    }


    @Override
    public boolean isAlwaysShowsPlus(StructureViewTreeElement element) {
        return false;
    }

    @Override
    public boolean isAlwaysLeaf(StructureViewTreeElement element) {
        return false;
    }

    @Override
    protected Class<?> @NotNull [] getSuitableClasses() {
        return new Class[]{
                SchemaEntityHead.class,
        };
    }

}
