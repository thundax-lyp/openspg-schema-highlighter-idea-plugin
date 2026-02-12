package org.openspg.idea.schema.folding;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.DumbAware;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.openspg.idea.common.FoldingAdapter;
import org.openspg.idea.schema.psi.SchemaEntity;
import org.openspg.idea.schema.psi.SchemaPlainTextContent;
import org.openspg.idea.schema.psi.SchemaProperty;

import java.util.Set;

public class SchemaFoldingBuilder extends FoldingBuilderEx implements DumbAware {

    private final Set<FoldingAdapter<? extends PsiElement>> adapters = Set.of(
            new SchemaCommentFoldingAdapter(),
            new SchemaEntityFoldingAdapter(),
            new SchemaPropertyFoldingAdapter(),
            new SchemaPlainTextContentFoldingAdapter()
    );

    @Override
    @SuppressWarnings("unchecked")
    public FoldingDescriptor @NotNull [] buildFoldRegions(@NotNull PsiElement root, @NotNull Document document, boolean quick) {
        Class<? extends PsiElement>[] classes = adapters
                .stream()
                .map(FoldingAdapter::getType)
                .toArray(Class[]::new);

        return PsiTreeUtil.findChildrenOfAnyType(root, classes)
                .stream()
                .map(element -> new FoldingDescriptor(element, element.getTextRange()))
                .toArray(FoldingDescriptor[]::new);
    }

    @Override
    public @Nullable String getPlaceholderText(@NotNull ASTNode node) {
        PsiElement element = node.getPsi();
        for (FoldingAdapter<? extends PsiElement> adapter : adapters) {
            if (adapter.getType().isInstance(element)) {
                return adapter.getPlaceholderText(element);
            }
        }
        return element.getText();
    }

    @Override
    public boolean isCollapsedByDefault(@NotNull ASTNode node) {
        return node.getPsi() instanceof PsiComment;
    }

    public static class SchemaCommentFoldingAdapter extends FoldingAdapter<PsiComment> {
        @Override
        protected String apply(@NotNull PsiComment element) {
            return "#...";
        }
    }

    public static class SchemaEntityFoldingAdapter extends FoldingAdapter<SchemaEntity> {
        @Override
        protected String apply(@NotNull SchemaEntity element) {
            String placeHolder = element.getEntityHead().getText();
            if (!element.isBodyEmpty()) {
                placeHolder += " {...}";
            }
            return placeHolder;
        }
    }

    public static class SchemaPropertyFoldingAdapter extends FoldingAdapter<SchemaProperty> {
        @Override
        protected String apply(@NotNull SchemaProperty element) {
            String placeHolder = element.getPropertyHead().getText();
            if (!element.isBodyEmpty()) {
                placeHolder += " {...}";
            }
            return placeHolder;
        }
    }

    public static class SchemaPlainTextContentFoldingAdapter extends FoldingAdapter<SchemaPlainTextContent> {
        @Override
        protected String apply(@NotNull SchemaPlainTextContent element) {
            return "...";
        }
    }

}

