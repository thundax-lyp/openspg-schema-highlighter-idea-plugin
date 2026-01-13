package org.openspg.idea.schema.folding;

import com.intellij.testFramework.fixtures.BasePlatformTestCase;

public class SchemaFoldingTest extends BasePlatformTestCase {

    protected final String myFileExt;
    protected final String myFullDataPath;
    protected final boolean myLowercaseFirstLetter;

    public SchemaFoldingTest() {
        this("folding", "schema", false);
    }

    protected SchemaFoldingTest(String dataPath, String fileExt, boolean lowercaseFirstLetter) {
        myFullDataPath = getTestDataPath() + "/" + dataPath;
        myFileExt = fileExt;
        myLowercaseFirstLetter = lowercaseFirstLetter;
    }

    /**
     * Scenario: entities, properties, comments, and plain text blocks
     * Focus: folding placeholder text and foldable element discovery
     */
    public void testBasic() {
        doTest();
    }

    protected void doTest() {
        String name = getTestName(false);
        myFixture.testFolding(myFullDataPath + "/" + name + "." + myFileExt);
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/testFixture";
    }

}
