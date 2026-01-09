package org.openspg.idea.conceptRule.structureView;

import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.StructureViewModelBase;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.openspg.idea.conceptRule.psi.ConceptRuleRuleWrapperDeclaration;
import org.openspg.idea.conceptRule.structureView.viewElement.ConceptRuleFileStructureViewElement;

public class ConceptRuleStructureViewModel extends StructureViewModelBase implements
        StructureViewModel.ElementInfoProvider {

    public ConceptRuleStructureViewModel(@Nullable Editor editor, PsiFile psiFile) {
        super(psiFile, editor, new ConceptRuleFileStructureViewElement(psiFile));
    }

    @NotNull
    public Sorter[] getSorters() {
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
    protected Class<?>[] getSuitableClasses() {
        return new Class[]{
                ConceptRuleRuleWrapperDeclaration.class,
        };
    }

}
