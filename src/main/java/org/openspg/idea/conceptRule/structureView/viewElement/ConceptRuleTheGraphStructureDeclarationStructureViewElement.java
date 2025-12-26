package org.openspg.idea.conceptRule.structureView.viewElement;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.conceptRule.psi.ConceptRuleTheGraphStructureDeclaration;
import org.openspg.idea.schema.SchemaIcons;


public class ConceptRuleTheGraphStructureDeclarationStructureViewElement extends AbstractConceptRuleStructureViewElement<ConceptRuleTheGraphStructureDeclaration> {

    public ConceptRuleTheGraphStructureDeclarationStructureViewElement(ConceptRuleTheGraphStructureDeclaration element) {
        super(element);
    }

    @Override
    public String getNullableAlphaSortKey() {
        return myElement.getTheGraphStructureHead().getText();
    }

    @Override
    protected PresentationData createPresentation(ConceptRuleTheGraphStructureDeclaration element) {
        return new PresentationData(
                element.getTheGraphStructureHead().getText(),
                null,
                SchemaIcons.Nodes.Entity,
                null
        );
    }

    @Override
    public TreeElement @NotNull [] getChildren() {
        return TreeElement.EMPTY_ARRAY;
    }

}
