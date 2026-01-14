package org.openspg.idea.conceptRule.folding;

import org.openspg.idea.common.AbstractFoldingTestCase;
import org.openspg.idea.conceptRule.ConceptRuleFoldingBuilder;

public class ConceptRuleFoldingTest extends AbstractFoldingTestCase {

    public ConceptRuleFoldingTest() {
        super("conceptRule/folding", "rule", new ConceptRuleFoldingBuilder());
    }

    /**
     * Scenario: wrapper rule with structure/rule/action blocks
     * Focus: folding placeholders for wrapper, define, and sections
     */
    public void testBasic() {
        doTest();
    }

    /**
     * Scenario: concept rule with only graph structure
     * Focus: folding placeholders without wrapper or rule/action sections
     */
    public void testGraphStructureOnly() {
        doTest();
    }

    /**
     * Scenario: line/block comments only
     * Focus: comment folding placeholders
     */
    public void testComments() {
        doTest();
    }

}
