package org.openspg.idea.conceptRule.folding;

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
import org.openspg.idea.conceptRule.psi.*;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;

public class ConceptRuleFoldingBuilder extends FoldingBuilderEx implements DumbAware {

    private final Set<FoldingAdapter<? extends PsiElement>> adapters = Set.of(
            new ConceptRuleCommentFoldingAdapter(),
            new ConceptRuleRuleWrapperDeclarationFoldingAdapter(),
            new ConceptRuleConceptRuleDeclarationFoldingAdapter(),
            new ConceptRuleTheGraphStructureDeclarationFoldingAdapter(),
            new ConceptRuleTheRuleDeclarationFoldingAdapter(),
            new ConceptRuleTheActionDeclarationFoldingAdapter()
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
                return adapter.getPlaceholderText(node, element);
            }
        }
        throw new IllegalArgumentException("Unknown PsiElement type: " + element.getClass());
    }

    @Override
    public boolean isCollapsedByDefault(@NotNull ASTNode node) {
        return node.getPsi() instanceof PsiComment;
    }

    public abstract static class FoldingAdapter<T extends PsiElement> {

        @SuppressWarnings("unchecked")
        public Class<T> getType() {
            Type superClass = getClass().getGenericSuperclass();
            Type[] params = ((ParameterizedType) superClass).getActualTypeArguments();
            return (Class<T>) params[0];
        }

        @SuppressWarnings("unchecked")
        public String getPlaceholderText(@NotNull ASTNode node, @NotNull PsiElement element) {
            assert getType().isInstance(element);
            return getPlaceholderText((T) element);
        }

        protected abstract String getPlaceholderText(T element);
    }

    public static class ConceptRuleCommentFoldingAdapter extends FoldingAdapter<PsiComment> {
        @Override
        protected String getPlaceholderText(@NotNull PsiComment element) {
            return "#...";
        }
    }

    public static class ConceptRuleRuleWrapperDeclarationFoldingAdapter extends FoldingAdapter<ConceptRuleRuleWrapperDeclaration> {
        @Override
        protected String getPlaceholderText(@NotNull ConceptRuleRuleWrapperDeclaration element) {
            return element.getRuleWrapperHead().getText() + " ...";
        }
    }

    public static class ConceptRuleConceptRuleDeclarationFoldingAdapter extends FoldingAdapter<ConceptRuleConceptRuleDeclaration> {
        @Override
        protected String getPlaceholderText(@NotNull ConceptRuleConceptRuleDeclaration element) {
            return element.getConceptRuleHead().getText() + " {...}";
        }
    }

    public static class ConceptRuleTheGraphStructureDeclarationFoldingAdapter extends FoldingAdapter<ConceptRuleTheGraphStructureDeclaration> {
        @Override
        protected String getPlaceholderText(@NotNull ConceptRuleTheGraphStructureDeclaration element) {
            return element.getTheGraphStructureHead().getText() + " {...}";
        }
    }

    public static class ConceptRuleTheRuleDeclarationFoldingAdapter extends FoldingAdapter<ConceptRuleTheRuleDeclaration> {
        @Override
        protected String getPlaceholderText(@NotNull ConceptRuleTheRuleDeclaration element) {
            return element.getTheRuleHead().getText() + " {...}";
        }
    }

    public static class ConceptRuleTheActionDeclarationFoldingAdapter extends FoldingAdapter<ConceptRuleTheActionDeclaration> {
        @Override
        protected String getPlaceholderText(@NotNull ConceptRuleTheActionDeclaration element) {
            return element.getTheActionHead().getText() + " {...}";
        }
    }

}

