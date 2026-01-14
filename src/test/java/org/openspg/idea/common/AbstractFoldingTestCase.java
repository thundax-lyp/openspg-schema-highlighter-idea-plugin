package org.openspg.idea.common;

import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public abstract class AbstractFoldingTestCase extends AbstractPlatformTestCase {

    private final FoldingBuilderEx myFoldingBuilder;

    public AbstractFoldingTestCase(String dataPath, String fileExt, FoldingBuilderEx foldingBuilder) {
        super(dataPath, fileExt, false);
        myFoldingBuilder = foldingBuilder;
    }

    protected void doTest() {
        String name = getTestName(myLowercaseFirstLetter);
        myFixture.configureByFile(name + "." + myFileExt);

        PsiFile file = myFixture.getFile();
        PsiDocumentManager.getInstance(getProject()).commitAllDocuments();
        Document document = file.getViewProvider().getDocument();
        assertNotNull(document);

        FoldingDescriptor[] descriptors = myFoldingBuilder.buildFoldRegions(file, document, false);

        List<FoldRegionData> regions = new ArrayList<>(descriptors.length);
        for (FoldingDescriptor descriptor : descriptors) {
            PsiElement element = descriptor.getElement().getPsi();
            String placeholder = escapePlaceholder(myFoldingBuilder.getPlaceholderText(element.getNode()));
            regions.add(new FoldRegionData(
                    descriptor.getRange().getStartOffset(),
                    descriptor.getRange().getEndOffset(),
                    placeholder
            ));
        }

        String actual = buildFoldingText(file.getText(), regions);
        String expected = readExpectedText(Path.of(getTestDataPath(), name + ".txt"));
        assertEquals(
                StringUtil.convertLineSeparators(expected),
                StringUtil.convertLineSeparators(actual)
        );
    }

    private String readExpectedText(Path path) {
        try {
            return Files.readString(path, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private String buildFoldingText(String text, List<FoldRegionData> regions) {
        Map<Integer, List<FoldRegionData>> starts = new HashMap<>();
        Map<Integer, List<FoldRegionData>> ends = new HashMap<>();
        for (FoldRegionData region : regions) {
            starts.computeIfAbsent(region.startOffset, key -> new ArrayList<>()).add(region);
            ends.computeIfAbsent(region.endOffset, key -> new ArrayList<>()).add(region);
        }

        StringBuilder result = new StringBuilder(text.length() + regions.size() * 16);
        int length = text.length();
        for (int offset = 0; offset <= length; offset++) {
            List<FoldRegionData> closing = ends.get(offset);
            if (closing != null) {
                closing.sort(Comparator.comparingInt((FoldRegionData data) -> data.startOffset).reversed());
                result.append("</fold>".repeat(closing.size()));
            }

            List<FoldRegionData> opening = starts.get(offset);
            if (opening != null) {
                opening.sort(Comparator.comparingInt((FoldRegionData data) -> data.endOffset).reversed());
                for (FoldRegionData region : opening) {
                    result.append("<fold text='").append(region.placeholder).append("'>");
                }
            }

            if (offset < length) {
                result.append(text.charAt(offset));
            }
        }
        return result.toString();
    }

    private String escapePlaceholder(@Nullable String placeholder) {
        if (placeholder == null) {
            return "";
        }
        return placeholder.replace("'", "&apos;");
    }

    private record FoldRegionData(int startOffset, int endOffset, String placeholder) {
    }

}
