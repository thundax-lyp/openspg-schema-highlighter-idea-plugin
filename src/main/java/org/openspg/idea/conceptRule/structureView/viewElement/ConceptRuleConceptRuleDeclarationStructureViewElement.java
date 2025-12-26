package org.openspg.idea.conceptRule.structureView.viewElement;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.conceptRule.psi.ConceptRuleConceptRuleDeclaration;
import org.openspg.idea.conceptRule.psi.ConceptRuleTheActionDeclaration;
import org.openspg.idea.conceptRule.psi.ConceptRuleTheGraphStructureDeclaration;
import org.openspg.idea.conceptRule.psi.ConceptRuleTheRuleDeclaration;
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
                element.getMajorLabel(),
                element.getMinorLabel(),
                SchemaIcons.Nodes.Entity,
                null
        );
    }

    @Override
    public TreeElement @NotNull [] getChildren() {
        List<PsiElement> elements = PsiTreeUtil.getChildrenOfAnyType(
                myElement.getConceptRuleBody(),
                ConceptRuleTheGraphStructureDeclaration.class,
                ConceptRuleTheRuleDeclaration.class,
                ConceptRuleTheActionDeclaration.class
        );

        List<TreeElement> treeElements = new ArrayList<>(elements.size());

        for (PsiElement element : elements) {
            if (element instanceof ConceptRuleTheGraphStructureDeclaration theGraphElement) {
                treeElements.add(new ConceptRuleTheGraphStructureDeclarationStructureViewElement(theGraphElement));

            } else if (element instanceof ConceptRuleTheRuleDeclaration theRuleElement) {
                treeElements.add(new ConceptRuleTheRuleDeclarationStructureViewElement(theRuleElement));

            } else if (element instanceof ConceptRuleTheActionDeclaration theActionElement) {
                treeElements.add(new ConceptRuleTheActionDeclarationStructureViewElement(theActionElement));

            } else {
                throw new IllegalArgumentException("Unknown element type: " + element.getClass().getName());
            }
        }

        return treeElements.toArray(new TreeElement[0]);
    }

}
