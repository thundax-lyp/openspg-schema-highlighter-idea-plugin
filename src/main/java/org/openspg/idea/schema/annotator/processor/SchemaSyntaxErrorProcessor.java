package org.openspg.idea.schema.annotator.processor;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.AnnotationBuilder;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiDocumentManager;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.common.AnnotateProcessor;
import org.openspg.idea.schema.SchemaBundle;
import org.openspg.idea.schema.action.SchemaInsertTextQuickFix;

public class SchemaSyntaxErrorProcessor implements AnnotateProcessor {

    @Override
    public boolean process(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (!(element instanceof PsiErrorElement errorElement)) {
            return true;
        }

        PsiFile file = element.getContainingFile();
        if (file == null) {
            return true;
        }
        Document document = PsiDocumentManager.getInstance(file.getProject()).getDocument(file);
        if (document == null) {
            return true;
        }

        int offset = element.getTextRange().getStartOffset();
        int line = document.getLineNumber(offset);
        int lineStart = document.getLineStartOffset(line);
        int lineEnd = document.getLineEndOffset(line);
        String lineText = document.getText(new TextRange(lineStart, lineEnd));
        String token = errorElement.getText();

        String expected = inferExpectedFormat(lineText);
        AnnotationBuilder builder = holder.newAnnotation(HighlightSeverity.ERROR,
                        SchemaBundle.message("SchemaAnnotator.error.syntax", token, expected))
                .range(element.getTextRange());
        applyFixesIfAny(lineText, lineStart, builder);
        builder.create();

        return true;
    }

    private String inferExpectedFormat(String lineText) {
        if (lineText.contains("->")) {
            return "Name(alias) -> EntityType, ConceptType:";
        }
        return "Name(alias): EntityType";
    }

    private void applyFixesIfAny(String lineText, int lineStart, AnnotationBuilder builder) {
        String trimmed = lineText.trim();
        boolean hasArrow = trimmed.contains("->");
        boolean hasColon = trimmed.contains(":");
        boolean hasParenOpen = trimmed.contains("(");
        boolean hasParenClose = trimmed.contains(")");

        if (hasParenOpen && !hasParenClose) {
            int insertAt = indexBeforeColonOrType(trimmed);
            builder.withFix(new SchemaInsertTextQuickFix(
                    lineStart + insertAt,
                    ")",
                    SchemaBundle.message("SchemaQuickFix.insert.text", ")")));
        }

        if (!hasArrow && !hasColon && containsTypeToken(trimmed)) {
            int typeIdx = firstTypeIndex(trimmed);
            if (typeIdx > 0) {
                builder.withFix(new SchemaInsertTextQuickFix(
                        lineStart + typeIdx,
                        ": ",
                        SchemaBundle.message("SchemaQuickFix.insert.text", ":")));
            }
        }

        if (hasArrow && !hasColon) {
            builder.withFix(new SchemaInsertTextQuickFix(
                    lineStart + trimmed.length(),
                    ":",
                    SchemaBundle.message("SchemaQuickFix.insert.text", ":")));
        }

        if (hasArrow && hasColon && isMissingTypeListAfterArrow(trimmed)) {
            int colonIdx = trimmed.indexOf(':');
            builder.withFix(new SchemaInsertTextQuickFix(
                    lineStart + colonIdx,
                    " EntityType",
                    SchemaBundle.message("SchemaQuickFix.insert.text", "EntityType")));
        }

        if (hasColon && isMissingTypeAfterColon(trimmed)) {
            int colonIdx = trimmed.indexOf(':');
            builder.withFix(new SchemaInsertTextQuickFix(
                    lineStart + colonIdx + 1,
                    " EntityType",
                    SchemaBundle.message("SchemaQuickFix.insert.text", "EntityType")));
        }

        if (hasParenOpen && hasParenClose && isEmptyAlias(trimmed)) {
            int insertAt = trimmed.indexOf('(') + 1;
            builder.withFix(new SchemaInsertTextQuickFix(
                    lineStart + insertAt,
                    "Alias",
                    SchemaBundle.message("SchemaQuickFix.insert.text", "Alias")));
        }

        if (!hasArrow && hasParenClose && hasColon && containsTypeToken(trimmed) && isInheritanceLike(trimmed)) {
            int insertAt = trimmed.indexOf(')') + 1;
            builder.withFix(new SchemaInsertTextQuickFix(
                    lineStart + insertAt,
                    " ->",
                    SchemaBundle.message("SchemaQuickFix.insert.text", "->")));
        }
    }

    private int indexBeforeColonOrType(String trimmed) {
        int colonIdx = trimmed.indexOf(':');
        if (colonIdx > 0) {
            return colonIdx;
        }
        int typeIdx = firstTypeIndex(trimmed);
        return typeIdx > 0 ? typeIdx : trimmed.length();
    }

    private boolean containsTypeToken(String text) {
        return text.contains("EntityType") || text.contains("ConceptType") || text.contains("EventType") || text.contains("IndexType");
    }

    private int firstTypeIndex(String text) {
        int idx = text.indexOf("EntityType");
        if (idx >= 0) return idx;
        idx = text.indexOf("ConceptType");
        if (idx >= 0) return idx;
        idx = text.indexOf("EventType");
        if (idx >= 0) return idx;
        return text.indexOf("IndexType");
    }

    private boolean isMissingTypeAfterColon(String text) {
        int colonIdx = text.indexOf(':');
        if (colonIdx < 0) {
            return false;
        }
        return text.substring(colonIdx + 1).trim().isEmpty();
    }

    private boolean isMissingTypeListAfterArrow(String text) {
        int arrowIdx = text.indexOf("->");
        if (arrowIdx < 0) {
            return false;
        }
        int colonIdx = text.indexOf(':', arrowIdx);
        if (colonIdx < 0) {
            return false;
        }
        return text.substring(arrowIdx + 2, colonIdx).trim().isEmpty();
    }

    private boolean isEmptyAlias(String text) {
        return text.contains("()") || text.contains("( )");
    }

    private boolean isInheritanceLike(String text) {
        return text.contains(",") && text.trim().endsWith(":");
    }
}
