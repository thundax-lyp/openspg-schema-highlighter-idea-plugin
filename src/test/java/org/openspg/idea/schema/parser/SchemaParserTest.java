package org.openspg.idea.schema.parser;

import com.intellij.testFramework.ParsingTestCase;

public class SchemaParserTest extends ParsingTestCase {

    public SchemaParserTest() {
        super("schema/parser", "schema", new SchemaParserDefinition());
    }

    /**
     * Scenario: basic sample with namespace, entity, and property
     * Focus: namespace/entity head/property parsing coverage
     */
    public void testBasic() {
        doTest(true);
    }

    /**
     * Scenario: entity inheritance declaration
     * Focus: inherited type list and alias parsing
     */
    public void testInheritedTypes() {
        doTest(true);
    }

    /**
     * Scenario: properties block with child entities
     * Focus: properties block and child entity parsing
     */
    public void testProperties() {
        doTest(true);
    }

    /**
     * Scenario: comment in all placement
     * Focus: comment
     */
    public void testComments() {
        doTest(true);
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/testFixture";
    }

    @Override
    protected boolean includeRanges() {
        return true;
    }
}
