package org.openspg.idea.conceptRule.structureView.viewElement;

import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.conceptRule.psi.ConceptRuleConceptRuleDeclaration;
import org.openspg.idea.conceptRule.psi.ConceptRuleRuleWrapperDeclaration;

import java.util.ArrayList;
import java.util.List;

public class ConceptRuleFileStructureViewElement extends AbstractConceptRuleStructureViewElement<PsiFile> {

    public ConceptRuleFileStructureViewElement(PsiFile element) {
        super(element);
    }

    @NotNull
    @Override
    public String getAlphaSortKey() {
        return myElement.getName();
    }

    @Override
    public TreeElement @NotNull [] getChildren() {
        List<PsiElement> elements = PsiTreeUtil.getChildrenOfAnyType(
                myElement,
                ConceptRuleRuleWrapperDeclaration.class,
                ConceptRuleConceptRuleDeclaration.class
        );

        List<TreeElement> treeElements = new ArrayList<>(elements.size());

        for (PsiElement element : elements) {
            if (element instanceof ConceptRuleRuleWrapperDeclaration ruleWrapper) {
                treeElements.add(new ConceptRuleRuleWrapperDeclarationStructureViewElement(ruleWrapper));
            } else if (element instanceof ConceptRuleConceptRuleDeclaration conceptRule) {
                treeElements.add(new ConceptRuleConceptRuleDeclarationStructureViewElement(conceptRule));
            }
        }

        return treeElements.toArray(new TreeElement[0]);
    }

}
