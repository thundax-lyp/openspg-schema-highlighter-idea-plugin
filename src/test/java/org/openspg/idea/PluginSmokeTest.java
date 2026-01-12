package org.openspg.idea;

import com.intellij.testFramework.fixtures.BasePlatformTestCase;

public class PluginSmokeTest extends BasePlatformTestCase {

    public void testPlatform() {
        System.out.println("=".repeat(40));
        assertTrue(true);
        System.out.println("BasePlatformTestCase is wired");
        System.out.println("=".repeat(40));
    }
}
