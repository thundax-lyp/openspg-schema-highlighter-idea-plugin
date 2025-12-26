package org.openspg.idea.schema.structureView.viewElement;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.schema.SchemaIcons;
import org.openspg.idea.schema.psi.SchemaBasicPropertyDeclaration;
import org.openspg.idea.schema.psi.SchemaProperty;

import javax.swing.*;

public class SchemaPropertyStructureViewElement extends AbstractSchemaStructureViewElement<SchemaProperty> {

    private static final Icon[] ICONS = {
            SchemaIcons.Nodes.EntityMeta, SchemaIcons.Nodes.PropertyMeta, SchemaIcons.Nodes.SubPropertyMeta,
    };

    public SchemaPropertyStructureViewElement(SchemaProperty element) {
        super(element);
    }

    @Override
    public String getNullableAlphaSortKey() {
        return myElement.getPropertyHead().getBasicPropertyDeclaration().getText();
    }

    @Override
    protected PresentationData createPresentation(SchemaProperty element) {
        SchemaBasicPropertyDeclaration declaration = element.getPropertyHead().getBasicPropertyDeclaration();

        String value = declaration.getValue();
        if (value != null && value.startsWith("[[") && value.endsWith("]]")) {
            value = "[[...]]";
        }

        return new PresentationData(
                declaration.getName(),
                value,
                this.getIcon(element),
                null
        );
    }

    @Override
    public TreeElement @NotNull [] getChildren() {
        if (myElement.getPropertyBody() == null) {
            return EMPTY_ARRAY;
        }

        return this.buildEntityTreeElements(myElement.getPropertyBody().getEntityList());
    }

    private Icon getIcon(SchemaProperty element) {
        int level = Math.max(0, Math.min(element.getLevel() - 1, ICONS.length - 1));
        return ICONS[level];
    }
}
