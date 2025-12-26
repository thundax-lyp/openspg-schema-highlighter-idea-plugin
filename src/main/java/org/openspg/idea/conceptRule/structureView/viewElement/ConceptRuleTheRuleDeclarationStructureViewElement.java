package org.openspg.idea.conceptRule.structureView.viewElement;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.conceptRule.psi.ConceptRuleTheRuleDeclaration;
import org.openspg.idea.schema.SchemaIcons;


public class ConceptRuleTheRuleDeclarationStructureViewElement extends AbstractConceptRuleStructureViewElement<ConceptRuleTheRuleDeclaration> {

    public ConceptRuleTheRuleDeclarationStructureViewElement(ConceptRuleTheRuleDeclaration element) {
        super(element);
    }

    @Override
    public String getNullableAlphaSortKey() {
        return myElement.getTheRuleHead().getText();
    }

    @Override
    protected PresentationData createPresentation(ConceptRuleTheRuleDeclaration element) {
        return new PresentationData(
                element.getTheRuleHead().getText(),
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
