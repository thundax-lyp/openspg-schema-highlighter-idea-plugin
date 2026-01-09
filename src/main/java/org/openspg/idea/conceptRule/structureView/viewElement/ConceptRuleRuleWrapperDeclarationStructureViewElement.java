package org.openspg.idea.conceptRule.structureView.viewElement;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import org.openspg.idea.conceptRule.psi.*;
import org.openspg.idea.schema.SchemaIcons;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConceptRuleRuleWrapperDeclarationStructureViewElement extends AbstractConceptRuleStructureViewElement<ConceptRuleRuleWrapperDeclaration> {

    public ConceptRuleRuleWrapperDeclarationStructureViewElement(ConceptRuleRuleWrapperDeclaration element) {
        super(element);
    }

    @Override
    public String getNullableAlphaSortKey() {
        return myElement.getRuleWrapperHead().getText();
    }

    @Override
    protected PresentationData createPresentation(ConceptRuleRuleWrapperDeclaration element) {
        List<String> labels = new ArrayList<>();
        List<String> locations = new ArrayList<>();

        myElement.getRuleWrapperHead()
                .getLabelExpressionList()
                .stream()
                .flatMap(x -> x.getLabelNameList().stream())
                .flatMap(labelName -> {
                    if (labelName.getConceptType() != null) {
                        return Stream.of(labelName.getConceptType());
                    }
                    return labelName.getConceptNameList().stream();
                })
                .forEach(psiElement -> {
                    if (psiElement instanceof ConceptRuleConceptType entityType) {
                        String label = entityType.getIdentifierList()
                                .stream()
                                .map(ConceptRuleIdentifier::getLabel)
                                .collect(Collectors.joining("."));
                        if (labels.isEmpty()) {
                            labels.add(label);
                        } else {
                            locations.add(label);
                        }

                    } else if (psiElement instanceof ConceptRuleConceptName conceptName) {
                        String label = conceptName.getConceptType()
                                .getIdentifierList()
                                .stream()
                                .map(ConceptRuleIdentifier::getLabel)
                                .collect(Collectors.joining("."));
                        String instanceId = conceptName.getConceptInstanceId().getLabel();
                        if (labels.isEmpty()) {
                            labels.add(label);
                            locations.add(instanceId);
                        } else {
                            locations.add("(" + label + " / " + instanceId + ")");
                        }
                    }
                });

        return new PresentationData(
                String.join(" ", labels),
                String.join(" ", locations),
                SchemaIcons.Nodes.Entity,
                null
        );
    }

    @Override
    public TreeElement[] getChildren() {
        List<ConceptRuleConceptRuleDeclaration> elements = myElement.getRuleWrapperBody()
                .getRuleWrapperRuleDeclarationList()
                .stream()
                .flatMap(x -> x.getRuleWrapperRuleBody().getConceptRuleDeclarationList().stream())
                .toList();

        if (elements.isEmpty()) {
            return TreeElement.EMPTY_ARRAY;
        }

        List<TreeElement> treeElements = new ArrayList<>(elements.size());
        for (ConceptRuleConceptRuleDeclaration element : elements) {
            treeElements.add(new ConceptRuleConceptRuleDeclarationStructureViewElement(element));
        }

        return treeElements.toArray(new TreeElement[0]);
    }

}
