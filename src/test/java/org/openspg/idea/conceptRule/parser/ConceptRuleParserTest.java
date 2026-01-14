package org.openspg.idea.conceptRule.parser;

import com.intellij.testFramework.ParsingTestCase;

public class ConceptRuleParserTest extends ParsingTestCase {

    public ConceptRuleParserTest() {
        super("conceptRule/parser", "rule", new ConceptRuleParserDefinition());
    }

    /**
     * Scenario: namespace only
     * Focus: namespace declaration parsing
     */
    public void testBasic() {
        doTest(true);
    }

    /**
     * Scenario: namespace and line comment
     * Focus: comment parsing as a top-level item
     */
    public void testComments() {
        doTest(true);
    }

    /**
     * Scenario: deployed file RiskMining
     * Focus: all of pipline
     */
    public void testRiskMining() {
        doTest(true);
    }

    /**
     * Scenario: deployed file SupplyChain
     * Focus: all of pipline
     */
    public void testSupplyChain() {
        doTest(true);
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/testFixture";
    }

}
