package org.openspg.idea.common;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class FoldingAdapter<T extends PsiElement> {

    @SuppressWarnings("unchecked")
    public Class<T> getType() {
        Type superClass = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) superClass).getActualTypeArguments();
        return (Class<T>) params[0];
    }

    @SuppressWarnings("unchecked")
    public @NotNull String getPlaceholderText(@NotNull PsiElement element) {
        return apply((T) element);
    }

    protected abstract String apply(@NotNull T element);
}
