package org.openspg.idea.schema.structureView.viewElement;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.schema.SchemaIcons;
import org.openspg.idea.schema.psi.SchemaBasicStructureDeclaration;
import org.openspg.idea.schema.psi.SchemaEntity;

import javax.swing.*;

public class SchemaEntityStructureViewElement extends AbstractSchemaStructureViewElement<SchemaEntity> {

    private static final Icon[] ICONS = {
            SchemaIcons.Nodes.Entity, SchemaIcons.Nodes.Property, SchemaIcons.Nodes.SubProperty,
    };

    public SchemaEntityStructureViewElement(SchemaEntity element) {
        super(element);
    }

    @Override
    public String getNullableAlphaSortKey() {
        return myElement.getEntityHead().getBasicStructureDeclaration().getStructureNameDeclaration().getText();
    }

    @Override
    protected PresentationData createPresentation(SchemaEntity element) {
        SchemaBasicStructureDeclaration declaration = element.getEntityHead().getBasicStructureDeclaration();
        return new PresentationData(
                declaration.getStructureNameDeclaration().getText(),
                declaration.getStructureAliasDeclaration().getText(),
                this.getIcon(element),
                null
        );
    }

    @Override
    public TreeElement @NotNull [] getChildren() {
        if (myElement.getEntityBody() == null) {
            return EMPTY_ARRAY;
        }

        return this.buildPropertyTreeElements(myElement.getEntityBody().getPropertyList());
    }

    private Icon getIcon(SchemaEntity element) {
        int level = Math.max(0, Math.min(element.getLevel(), ICONS.length - 1));
        return ICONS[level];
    }
}
