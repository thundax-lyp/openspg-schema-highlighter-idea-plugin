package org.openspg.idea;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PluginSmokeTest {
    @Test
    void junitIsWired() {
        System.out.println("=".repeat(40));
        System.out.println("junit is wired");
        System.out.println("=".repeat(40));
        assertTrue(true);
    }
}
