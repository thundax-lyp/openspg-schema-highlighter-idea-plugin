package org.openspg.idea.conceptRule.psi.impl;

import org.apache.commons.lang3.StringUtils;
import org.openspg.idea.conceptRule.psi.*;

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
