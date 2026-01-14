package org.openspg.idea.common;

import com.intellij.testFramework.fixtures.BasePlatformTestCase;

public abstract class AbstractPlatformTestCase extends BasePlatformTestCase {

    protected static final String TEST_DATA_PATH = "src/test/resources/testFixture";

    protected final String myFileExt;
    protected final String myDataPath;
    protected final boolean myLowercaseFirstLetter;

    public AbstractPlatformTestCase() {
        this(null, null, false);
    }

    public AbstractPlatformTestCase(String dataPath, String fileExt, boolean lowercaseFirstLetter) {
        myDataPath = dataPath;
        myFileExt = fileExt;
        myLowercaseFirstLetter = lowercaseFirstLetter;
    }

    @Override
    protected String getTestDataPath() {
        return TEST_DATA_PATH + "/" + myDataPath;
    }

}
