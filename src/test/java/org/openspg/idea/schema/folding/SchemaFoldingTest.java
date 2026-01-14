package org.openspg.idea.schema.folding;

import org.openspg.idea.common.AbstractFoldingTestCase;

public class SchemaFoldingTest extends AbstractFoldingTestCase {

    public SchemaFoldingTest() {
        super("schema/folding", "schema", new SchemaFoldingBuilder());
    }

    /**
     * Scenario: entities, properties, comments, and plain text blocks
     * Focus: folding placeholder text and foldable element discovery
     */
    public void testBasic() {
        doTest();
    }

    /**
     * Scenario: nested entities with multiple levels
     * Focus: entity and property folding placeholders at each level
     */
    public void testNestedEntities() {
        doTest();
    }

    /**
     * Scenario: entity and property without bodies
     * Focus: placeholders without "{...}" suffix
     */
    public void testEmptyBodies() {
        doTest();
    }

    /**
     * Scenario: multiple comment lines
     * Focus: each comment line can be folded
     */
    public void testMultipleComments() {
        doTest();
    }

    /**
     * Scenario: multiple plain text blocks
     * Focus: plain text content folding within each block
     */
    public void testMultiplePlainTextBlocks() {
        doTest();
    }
}
