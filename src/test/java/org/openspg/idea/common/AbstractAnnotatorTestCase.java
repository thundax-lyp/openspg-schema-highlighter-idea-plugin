package org.openspg.idea.common;

import com.alibaba.fastjson.JSON;
import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.schema.annotator.SchemaAnnotatorTest;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractAnnotatorTestCase extends AbstractPlatformTestCase {

    protected void assertHighlights(List<HighlightInfo> actual, List<HighlightData> expected) {
        assertEquals(
                serializeData(expected),
                serializeInfo(actual)
        );
    }

    protected String serializeInfo(List<HighlightInfo> infos) {
        return serializeData(infos
                .stream()
                .map(HighlightData::fromHighlightInfo)
                .toList()
        );
    }

    protected String serializeData(List<HighlightData> records) {
        return String.join("\n", records
                .stream()
                .sorted()
                .map(HighlightData::toString)
                .toList()
        );
    }

    protected record HighlightData(
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
            String tooltip = null;
            TextAttributesKey textAttributesKey = null;

            if (info.getSeverity() == HighlightSeverity.TEXT_ATTRIBUTES) {
                textAttributesKey = info.getHighlighter().getTextAttributesKey();
            } else {
                tooltip = info.getToolTip();
                if (tooltip != null && tooltip.startsWith("<html>") && tooltip.endsWith("</html>")) {
                    tooltip = tooltip.substring("<html>".length(), tooltip.length() - "</html>".length());
                }
            }

            return new HighlightData(
                    info.getText(),
                    info.startOffset,
                    info.endOffset,
                    info.getSeverity(),
                    textAttributesKey,
                    tooltip
            );
        }

        public static HighlightData highlight(String source, String keyword, int occurrence, TextAttributesKey textAttributesKey) {
            int startOffset = nthIndexOf(source, keyword, occurrence);
            assertTrue("Cannot find occurrence " + occurrence + " of \"" + keyword + "\"", startOffset >= 0);
            int endOffset = startOffset + keyword.length();
            return new HighlightData(
                    keyword,
                    startOffset,
                    endOffset,
                    HighlightSeverity.TEXT_ATTRIBUTES,
                    textAttributesKey,
                    null
            );
        }

        public static HighlightData error(String source, String keyword, int occurrence, HighlightSeverity severity, String tooltip) {
            int startOffset = nthIndexOf(source, keyword, occurrence);
            assertTrue("Cannot find occurrence " + occurrence + " of \"" + keyword + "\"", startOffset >= 0);
            int endOffset = startOffset + keyword.length();
            return new HighlightData(
                    keyword,
                    startOffset,
                    endOffset,
                    severity,
                    null,
                    tooltip
            );
        }

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
