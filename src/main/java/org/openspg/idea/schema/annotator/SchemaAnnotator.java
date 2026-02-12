package org.openspg.idea.schema.annotator;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.common.AnnotateProcessor;
import org.openspg.idea.schema.annotator.processor.*;

import java.util.List;

/**
 * Aggregates schema-specific annotator processors and runs them in order.
 */
final class SchemaAnnotator implements Annotator {

    private final List<AnnotateProcessor> myProcessors;

    public SchemaAnnotator() {
        myProcessors = List.of(
                new SchemaHighlightingProcessor(),
                new SchemaEntitySemanticNameProcessor(),
                new SchemaEntityDepthProcessor(),
                new SchemaInheritedStructureTypeProcessor(),
                new SchemaNamespaceProcessor(),
                new SchemaRootEntityProcessor()
        );
    }

    /**
     * Delegates annotation to registered processors, stopping on failure.
     */
    @Override
    public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {
        for (AnnotateProcessor processor : myProcessors) {
            if (!processor.process(element, holder)) {
                return;
            }
        }
    }

}
