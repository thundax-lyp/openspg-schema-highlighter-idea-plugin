package org.openspg.idea.conceptRule.structureView.viewElement;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.conceptRule.psi.ConceptRuleTheActionDeclaration;
import org.openspg.idea.schema.SchemaIcons;


public class ConceptRuleTheActionDeclarationStructureViewElement extends AbstractConceptRuleStructureViewElement<ConceptRuleTheActionDeclaration> {

    public ConceptRuleTheActionDeclarationStructureViewElement(ConceptRuleTheActionDeclaration element) {
        super(element);
    }

    @Override
    public String getNullableAlphaSortKey() {
        return myElement.getTheActionHead().getText();
    }

    @Override
    protected PresentationData createPresentation(ConceptRuleTheActionDeclaration element) {
        return new PresentationData(
                element.getTheActionHead().getText(),
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
