package org.openspg.idea.conceptRule.structureView.viewElement;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.conceptRule.psi.ConceptRuleConceptRuleDeclaration;
import org.openspg.idea.conceptRule.psi.ConceptRuleConceptRuleItem;
import org.openspg.idea.schema.SchemaIcons;

import java.util.ArrayList;
import java.util.List;

public class ConceptRuleConceptRuleDeclarationStructureViewElement extends AbstractConceptRuleStructureViewElement<ConceptRuleConceptRuleDeclaration> {

    public ConceptRuleConceptRuleDeclarationStructureViewElement(ConceptRuleConceptRuleDeclaration element) {
        super(element);
    }

    @Override
    public String getNullableAlphaSortKey() {
        return myElement.getConceptRuleHead().getText();
    }

    @Override
    protected PresentationData createPresentation(ConceptRuleConceptRuleDeclaration element) {
        return new PresentationData(
                "Define " + element.getMajorLabel(),
                element.getMinorLabel(),
                SchemaIcons.Nodes.Entity,
                null
        );
    }

    @Override
    public TreeElement @NotNull [] getChildren() {
        List<TreeElement> treeElements = new ArrayList<>();

        for (ConceptRuleConceptRuleItem item : myElement.getConceptRuleBody().getConceptRuleItemList()) {
            if (item.getTheGraphStructureDeclaration() != null) {
                treeElements.add(new ConceptRuleTheGraphStructureDeclarationStructureViewElement(item.getTheGraphStructureDeclaration()));

            } else if (item.getTheRuleDeclaration() != null) {
                treeElements.add(new ConceptRuleTheRuleDeclarationStructureViewElement(item.getTheRuleDeclaration()));

            } else if (item.getTheActionDeclaration() != null) {
                treeElements.add(new ConceptRuleTheActionDeclarationStructureViewElement(item.getTheActionDeclaration()));

            } else {
                throw new IllegalArgumentException("Unknown element type: " + item.getClass().getName());
            }
        }

        return treeElements.toArray(new TreeElement[0]);
    }

}
