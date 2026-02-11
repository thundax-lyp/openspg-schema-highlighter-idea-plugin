package org.openspg.idea.schema.reference;

import com.intellij.lang.documentation.AbstractDocumentationProvider;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.openspg.idea.schema.highlighter.SchemaHighlightingColors;
import org.openspg.idea.schema.psi.*;

import java.awt.*;
import java.util.List;

public class SchemaDocumentationProvider extends AbstractDocumentationProvider {

    @Override
    public @Nullable String getQuickNavigateInfo(PsiElement element, PsiElement originalElement) {
        return buildDocHtml(element, isDifferent(element, originalElement));
    }

    @Override
    public @Nullable String generateDoc(PsiElement element, @Nullable PsiElement originalElement) {
        return buildDocHtml(element, isDifferent(element, originalElement));
    }

    private boolean isDifferent(PsiElement element, @Nullable PsiElement originalElement) {
        if (originalElement == null) {
            return true;
        }
        return !element.getContainingFile().equals(originalElement.getContainingFile());
    }

    private @Nullable String buildDocHtml(@NotNull PsiElement element, boolean includeFilePath) {
        List<SchemaEntity> entities = findEntities(element);
        if (entities.isEmpty()) {
            return null;
        }

        String fileName = element.getContainingFile().getName();
        String commentColor = cssColor(SchemaHighlightingColors.COMMENT);

        StringBuilder html = new StringBuilder();
        html.append("<div style='white-space: nowrap;'>");

        if (includeFilePath) {
            html.append(colorText(StringUtil.escapeXmlEntities("["), commentColor));
            html.append(colorText(StringUtil.escapeXmlEntities(fileName), commentColor));
            html.append(colorText(StringUtil.escapeXmlEntities("]"), commentColor));
            html.append("<br/>");
        }

        for (int i = entities.size() - 1; i >= 0; i--) {
            SchemaEntity entity = entities.get(i);
            int level = entities.size() - i - 1;
            html.append(buildEntityDocHtml(entity, level * 20));
            if (i > 0) {
                html.append("<br/>");
            }
        }
        html.append("</div>");

        return html.toString();
    }

    private String buildEntityDocHtml(@NotNull SchemaEntity entity, int indent) {
        SchemaBasicStructureDeclaration declaration = entity.getEntityHead().getBasicStructureDeclaration();
        List<String> semanticNames = declaration
                .getStructureNameDeclaration()
                .getStructureName()
                .getStructureSemanticNameList()
                .stream()
                .map(PsiElement::getText)
                .toList();

        String realName = declaration
                .getStructureNameDeclaration()
                .getStructureName()
                .getStructureRealName()
                .getText();
        String aliasName = declaration.getStructureAliasDeclaration().getText();
        List<String> types = declaration.getStructureTypeDeclaration().getTypes();

        String operationColor = cssColor(SchemaHighlightingColors.OPERATION_SIGN);
        String nameColor = cssColor(SchemaHighlightingColors.ENTITY_NAME);
        String aliasColor = cssColor(SchemaHighlightingColors.ENTITY_ALIAS);
        String referenceColor = cssColor(SchemaHighlightingColors.ENTITY_REFERENCE);

        StringBuilder html = new StringBuilder();
        html.append("<div style='white-space: nowrap; margin-left: ").append(indent).append("px;'>");
        semanticNames.forEach(semanticName -> {
            html.append(colorText(StringUtil.escapeXmlEntities(semanticName), referenceColor));
            html.append(colorText(StringUtil.escapeXmlEntities("#"), operationColor));
        });

        html.append(colorText(StringUtil.escapeXmlEntities(realName), nameColor));

        html.append(colorText(StringUtil.escapeXmlEntities("("), operationColor));
        html.append(colorText(StringUtil.escapeXmlEntities(aliasName), aliasColor));
        html.append(colorText(StringUtil.escapeXmlEntities(")"), operationColor));

        if (types.size() > 1) {
            html.append(colorText(StringUtil.escapeXmlEntities("-> "), operationColor));
            html.append(colorText(StringUtil.escapeXmlEntities(String.join(", ", types)), referenceColor));
            html.append(colorText(StringUtil.escapeXmlEntities(": "), operationColor));
        } else {
            html.append(colorText(StringUtil.escapeXmlEntities(": "), operationColor));
            html.append(colorText(StringUtil.escapeXmlEntities(String.join("", types)), referenceColor));
        }
        html.append("</div>");

        return html.toString();
    }

    private @NotNull List<SchemaEntity> findEntities(@NotNull PsiElement element) {
        return PsiTreeUtil.collectParents(element, SchemaEntity.class, false, x -> false);
    }

    private @NotNull String colorText(@NotNull String text, @Nullable String color) {
        if (color == null || color.isBlank()) {
            return text;
        }
        return "<span style='color: " + color + ";'>" + text + "</span>";
    }

    private @Nullable String cssColor(@NotNull TextAttributesKey key) {
        EditorColorsScheme scheme = EditorColorsManager.getInstance().getGlobalScheme();
        TextAttributes attrs = scheme.getAttributes(key);
        Color color = attrs == null ? null : attrs.getForegroundColor();
        if (color == null) {
            return null;
        }
        return "rgb(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + ")";
    }
}
