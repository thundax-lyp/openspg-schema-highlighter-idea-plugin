package org.openspg.idea.schema.structureView.viewElement;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.openspg.idea.schema.psi.SchemaEntity;
import org.openspg.idea.schema.psi.SchemaProperty;

import java.util.ArrayList;
import java.util.List;

abstract class AbstractSchemaStructureViewElement<T extends PsiElement> implements StructureViewTreeElement, SortableTreeElement {

    protected final T myElement;

    public AbstractSchemaStructureViewElement(T element) {
        this.myElement = element;
    }

    @Override
    public Object getValue() {
        return myElement;
    }

    @Override
    public void navigate(boolean requestFocus) {
        if (myElement instanceof NavigatablePsiElement) {
            ((NavigatablePsiElement) myElement).navigate(requestFocus);
        }
    }

    @Override
    public boolean canNavigate() {
        if (myElement instanceof NavigatablePsiElement) {
            return ((NavigatablePsiElement) myElement).canNavigate();
        }
        return false;
    }

    @Override
    public boolean canNavigateToSource() {
        if (myElement instanceof NavigatablePsiElement) {
            return ((NavigatablePsiElement) myElement).canNavigateToSource();
        }
        return false;
    }

    @NotNull
    @Override
    public String getAlphaSortKey() {
        String key = this.getNullableAlphaSortKey();
        return key == null ? "" : key;
    }

    @NotNull
    @Override
    public ItemPresentation getPresentation() {
        if (myElement instanceof NavigatablePsiElement) {
            ItemPresentation presentation = ((NavigatablePsiElement) myElement).getPresentation();
            if (presentation == null) {
                presentation = createPresentation(myElement);
            }
            return presentation;
        }
        return createPresentation(myElement);
    }

    protected PresentationData createPresentation(T element) {
        return new PresentationData();
    }

    @Nullable
    protected String getNullableAlphaSortKey() {
        return null;
    }

    protected TreeElement @NotNull [] buildEntityTreeElements(List<SchemaEntity> elements) {
        List<TreeElement> treeElements = new ArrayList<>(elements.size());

        for (SchemaEntity element : elements) {
            treeElements.add(new SchemaEntityStructureViewElement(element));
        }

        return treeElements.toArray(new TreeElement[0]);
    }

    protected TreeElement @NotNull [] buildPropertyTreeElements(List<SchemaProperty> elements) {
        List<TreeElement> treeElements = new ArrayList<>(elements.size());

        for (SchemaProperty element : elements) {
            treeElements.add(new SchemaPropertyStructureViewElement(element));
        }

        return treeElements.toArray(new TreeElement[0]);
    }
}
