package org.openspg.idea.conceptRule.psi.impl;

import com.intellij.psi.PsiReference;
import org.apache.commons.lang3.StringUtils;
import org.openspg.idea.conceptRule.psi.*;
import org.openspg.idea.conceptRule.reference.ConceptRuleConceptTypeReference;

import java.util.List;

public class ConceptRulePsiImplUtil {

    // ============================================
    // ConceptRuleNamespace methods
    //
    public static String getValue(ConceptRuleNamespaceDeclaration element) {
        return element.getNamespaceVariable().getText();
    }

    // ============================================
    // ConceptRuleConceptRuleDeclaration methods
    public static String getMajorLabel(ConceptRuleConceptRuleDeclaration element) {
        List<ConceptRuleNodePattern> nodePatterns = element.getConceptRuleHead().getNodePatternList();
        if (nodePatterns.isEmpty()) {
            return StringUtils.EMPTY;
        }
        return nodePatterns.get(0).getText().trim();
    }

    public static String getMinorLabel(ConceptRuleConceptRuleDeclaration element) {
        List<ConceptRuleNodePattern> nodePatterns = element.getConceptRuleHead().getNodePatternList();
        if (nodePatterns.size() <= 1) {
            return StringUtils.EMPTY;
        }
        return element.getConceptRuleHead().getFullEdgePointingRight().getText().trim() + nodePatterns.get(1).getText().trim();
    }

    // ============================================
    // ConceptRuleConceptType methods
    public static String getMajorLabel(ConceptRuleConceptType element) {
        List<ConceptRuleIdentifier> identifiers = element.getIdentifierList();
        if (identifiers.isEmpty()) {
            return StringUtils.EMPTY;
        }
        return identifiers.get(0).getLabel();
    }

    public static PsiReference getReference(ConceptRuleConceptType element) {
        return new ConceptRuleConceptTypeReference(element);
    }

    // ============================================
    // ConceptRuleIdentifier methods
    //
    public static String getLabel(ConceptRuleIdentifier element) {
        String label = element.getText();
        label = StringUtils.unwrap(label, "`");
        label = StringUtils.unwrap(label, "'");
        label = StringUtils.unwrap(label, "\"");
        return label;
    }

    // ============================================
    // ConceptRuleIdentifier methods
    //
    public static String getLabel(ConceptRuleConceptInstanceId element) {
        String label = element.getText();
        label = StringUtils.unwrap(label, "`");
        label = StringUtils.unwrap(label, "'");
        label = StringUtils.unwrap(label, "\"");
        return label;
    }

}
