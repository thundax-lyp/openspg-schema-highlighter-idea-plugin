package org.openspg.idea.common;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public interface AnnotateProcessor {

    /**
     * @param element the element to process
     * @param holder  the annotation holder
     * @return true if continue processing, false if stop processing
     */
    boolean process(@NotNull final PsiElement element, @NotNull AnnotationHolder holder);

}
