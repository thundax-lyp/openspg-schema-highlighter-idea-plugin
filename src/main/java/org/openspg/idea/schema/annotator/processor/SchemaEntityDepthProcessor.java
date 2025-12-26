package org.openspg.idea.schema.annotator.processor;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.common.annotator.AnnotateProcessor;
import org.openspg.idea.schema.SchemaBundle;
import org.openspg.idea.schema.psi.SchemaEntity;

/**
 * Validates schema entity nesting depth against a configured limit.
 */
public class SchemaEntityDepthProcessor implements AnnotateProcessor {

    private static final int MAX_LEVEL = 3;

    /**
     * Emits an error annotation when an entity exceeds the max depth.
     */
    public boolean process(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof SchemaEntity entity) {
            if (entity.getLevel() >= MAX_LEVEL) {
                holder.newAnnotation(HighlightSeverity.ERROR, element.getText())
                        .range(element.getTextRange())
                        .tooltip(SchemaBundle.message(
                                "SchemaAnnotator.error.schema.depth.must.be.less.then",
                                MAX_LEVEL))
                        .create();
            }
        }
        return true;
    }

}
