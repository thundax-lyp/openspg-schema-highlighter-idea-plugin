package org.openspg.idea.schema.structureView.viewElement;

import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.schema.psi.SchemaEntity;
import org.openspg.idea.schema.psi.SchemaRootEntity;

import java.util.List;

public class SchemaFileStructureViewElement extends AbstractSchemaStructureViewElement<PsiFile> {

    public SchemaFileStructureViewElement(PsiFile element) {
        super(element);
    }

    @NotNull
    @Override
    public String getAlphaSortKey() {
        return myElement.getName();
    }

    @Override
    public TreeElement[] getChildren() {
        List<SchemaEntity> elements = PsiTreeUtil.getChildrenOfTypeAsList(myElement, SchemaRootEntity.class)
                .stream()
                .map(SchemaRootEntity::getEntity)
                .toList();

        return this.buildEntityTreeElements(elements);
    }

}
