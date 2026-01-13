package org.openspg.idea.schema.annotator;

import com.alibaba.fastjson.JSON;
import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.schema.SchemaFileType;
import org.openspg.idea.schema.highlighter.SchemaHighlightingColors;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class SchemaAnnotatorTest extends BasePlatformTestCase {

    /**
     * Scenario: highlight a small schema sample
     * Focus: syntax + annotator highlights on key tokens
     * Assert: keyword/operator/entity name/alias/reference are highlighted
     */
    public void testBasic() {
        String text = """
                namespace Sample
                
                Person(人物): EntityType
                Child(孩子) -> Person:
                    desc: "ok"
                """;

        myFixture.configureByText(SchemaFileType.INSTANCE, text);
        List<String> actualItems = myFixture.doHighlighting()
                .stream()
                .map(HighlightData::fromHighlightInfo)
                .sorted()
                .map(HighlightData::toString)
                .toList();

        List<String> expectedItems = Stream.of(
                        HighlightData.generate(text, "Person", 1, HighlightSeverity.TEXT_ATTRIBUTES, SchemaHighlightingColors.ENTITY_NAME),
                        HighlightData.generate(text, "人物", 1, HighlightSeverity.TEXT_ATTRIBUTES, SchemaHighlightingColors.ENTITY_ALIAS),
                        HighlightData.generate(text, "Child", 1, HighlightSeverity.TEXT_ATTRIBUTES, SchemaHighlightingColors.ENTITY_NAME),
                        HighlightData.generate(text, "孩子", 1, HighlightSeverity.TEXT_ATTRIBUTES, SchemaHighlightingColors.ENTITY_ALIAS),
                        HighlightData.generate(text, "Person", 2, HighlightSeverity.TEXT_ATTRIBUTES, SchemaHighlightingColors.ENTITY_REFERENCE)
                )
                .sorted()
                .map(HighlightData::toString)
                .toList();

        assertOrderedEquals(actualItems, expectedItems);
    }

    /**
     * Scenario: invalid semantic name in structure declaration
     * Focus: error annotation from semantic name processor
     * Assert: error severity is emitted for the semantic name token
     */
    public void testInvalidSemanticNameError() {
        String text = """
                namespace Sample
                
                Person(人物): EntityType
                    properties:
                        BAD#belongTo(错误): Person
                """;

        myFixture.configureByText(SchemaFileType.INSTANCE, text);

        List<String> actualItems = myFixture.doHighlighting()
                .stream()
                .filter(info -> info.getSeverity() == HighlightSeverity.ERROR)
                .map(HighlightData::fromHighlightInfo)
                .sorted()
                .map(HighlightData::toString)
                .toList();

        List<String> expectedItems = Stream.of(
                        HighlightData.error(text, "BAD", 1, "Semantic name must be one of SYNANT, CAU, USE, SEQ, IND, INC")
                )
                .map(HighlightData::toString)
                .toList();

        assertOrderedEquals(actualItems, expectedItems);
    }

    /**
     * Scenario: invalid inherited structure type in structure declaration
     * Focus: error annotation from structure type processor
     * Assert: error severity is emitted for the structure type token
     */
    public void testInvalidInheritedStructureTypeError() {
        String text = """
                namespace Sample
                
                Person(人物): EntityType
                Work(作品) -> EntityType, UnknownType:
                """;

        myFixture.configureByText(SchemaFileType.INSTANCE, text);

        List<String> actualItems = myFixture.doHighlighting()
                .stream()
                .filter(info -> info.getSeverity() == HighlightSeverity.ERROR)
                .map(HighlightData::fromHighlightInfo)
                .sorted()
                .map(HighlightData::toString)
                .toList();

        List<String> expectedItems = Stream.of(
                        HighlightData.error(text, "UnknownType", 1, "Undefined type \"UnknownType\"")
                )
                .map(HighlightData::toString)
                .toList();

        assertOrderedEquals(actualItems, expectedItems);
    }

    /**
     * Scenario: namespace is declaration
     * Focus: error annotation from namespace processor
     * Assert: error severity is emitted for the first element of document
     */
    public void testMissingNamespaceError() {
        String text = """
                Person(人物): EntityType
                """;

        myFixture.configureByText(SchemaFileType.INSTANCE, text);

        List<String> actualItems = myFixture.doHighlighting()
                .stream()
                .filter(info -> info.getSeverity() == HighlightSeverity.ERROR)
                .map(HighlightData::fromHighlightInfo)
                .sorted()
                .map(HighlightData::toString)
                .toList();

        List<String> expectedItems = Stream.of(
                        HighlightData.error(text, "Person(人物): EntityType", 1, "First element of document must be a namespace")
                )
                .map(HighlightData::toString)
                .toList();

        assertOrderedEquals(actualItems, expectedItems);
    }

    /**
     * Scenario: namespace is singleton
     * Focus: error annotation from namespace processor
     * Assert: error severity is emitted for the redeclared namespace
     */
    public void testRedeclaredNamespaceError() {
        String text = """
                namespace First
                
                namespace Second

                Person(人物): EntityType
                """;

        myFixture.configureByText(SchemaFileType.INSTANCE, text);

        List<String> actualItems = myFixture.doHighlighting()
                .stream()
                .filter(info -> info.getSeverity() == HighlightSeverity.ERROR)
                .map(HighlightData::fromHighlightInfo)
                .sorted()
                .map(HighlightData::toString)
                .toList();

        List<String> expectedItems = Stream.of(
                        HighlightData.error(text, "namespace Second", 1, "Duplicate definition of namespace")
                )
                .map(HighlightData::toString)
                .toList();

        assertOrderedEquals(actualItems, expectedItems);
    }

    /**
     * Scenario: namespace is singleton
     * Focus: error annotation from namespace processor
     * Assert: error severity is emitted for the redeclared namespace
     */
    public void testRedeclaredEntityError() {
        String text = """
                namespace Sample

                Person(人物): EntityType

                Person(重名人物): ConceptType
                """;

        myFixture.configureByText(SchemaFileType.INSTANCE, text);

        List<String> actualItems = myFixture.doHighlighting()
                .stream()
                .filter(info -> info.getSeverity() == HighlightSeverity.ERROR)
                .map(HighlightData::fromHighlightInfo)
                .sorted()
                .map(HighlightData::toString)
                .toList();

        List<String> expectedItems = Stream.of(
                        HighlightData.error(text, "Person", 1, "Cannot redeclare block-scoped schema \"Person\""),
                        HighlightData.error(text, "Person", 2, "Cannot redeclare block-scoped schema \"Person\"")
                )
                .sorted()
                .map(HighlightData::toString)
                .toList();

        assertOrderedEquals(actualItems, expectedItems);
    }

    public record HighlightData(
            String myText,
            int myStartOffset,
            int myEndOffset,
            HighlightSeverity mySeverity,
            TextAttributesKey myTextAttributesKey,
            String myTooltip
    ) implements Comparable<HighlightData> {

        @Override
        public @NotNull String toString() {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("text", myText);
            map.put("start", myStartOffset);
            map.put("end", myEndOffset);
            if (mySeverity != null) {
                map.put("severity", mySeverity.myName);
            }
            if (myTextAttributesKey != null) {
                map.put("key", myTextAttributesKey.getExternalName());
            }
            if (myTooltip != null) {
                map.put("tooltip", myTooltip);
            }
            return JSON.toJSONString(map);
        }

        @Override
        public int compareTo(@NotNull SchemaAnnotatorTest.HighlightData o) {
            if (myStartOffset != o.myStartOffset) {
                return myStartOffset - o.myStartOffset;
            }
            return myEndOffset - o.myEndOffset;
        }

        public static HighlightData fromHighlightInfo(HighlightInfo info) {
            if (info.getSeverity() == HighlightSeverity.ERROR) {
                String tooltip = info.getToolTip();
                if (tooltip != null && tooltip.startsWith("<html>") && tooltip.endsWith("</html>")) {
                    tooltip = tooltip.substring("<html>".length(), tooltip.length() - "</html>".length());
                }
                return new HighlightData(
                        info.getText(),
                        info.startOffset,
                        info.endOffset,
                        null,
                        null,
                        tooltip
                );
            }
            return new HighlightData(
                    info.getText(),
                    info.startOffset,
                    info.endOffset,
                    info.getSeverity(),
                    info.getHighlighter().getTextAttributesKey(),
                    null
            );
        }

        public static HighlightData generate(String source, String keyword, int occurrence, HighlightSeverity severity, TextAttributesKey textAttributesKey) {
            int startOffset = nthIndexOf(source, keyword, occurrence);
            assertTrue("Cannot find occurrence " + occurrence + " of \"" + keyword + "\"", startOffset >= 0);
            int endOffset = startOffset + keyword.length();
            return new HighlightData(
                    keyword,
                    startOffset,
                    endOffset,
                    severity,
                    textAttributesKey,
                    null
                    );
        }

        public static HighlightData error(String source, String keyword, int occurrence, String tooltip) {
            int startOffset = nthIndexOf(source, keyword, occurrence);
            assertTrue("Cannot find occurrence " + occurrence + " of \"" + keyword + "\"", startOffset >= 0);
            int endOffset = startOffset + keyword.length();
            return new HighlightData(
                    keyword,
                    startOffset,
                    endOffset,
                    null,
                    null,
                    tooltip
            );
        }

        private static int nthIndexOf(String text, String target, int occurrence) {
            int fromIndex = 0;
            for (int i = 1; i <= occurrence; i++) {
                int index = text.indexOf(target, fromIndex);
                if (index < 0) {
                    return -1;
                }
                fromIndex = index + target.length();
                if (i == occurrence) {
                    return index;
                }
            }
            return -1;
        }

    }

}
