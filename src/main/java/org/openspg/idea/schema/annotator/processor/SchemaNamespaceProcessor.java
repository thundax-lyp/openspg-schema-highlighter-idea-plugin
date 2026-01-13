package org.openspg.idea.schema.annotator.processor;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.common.annotator.AnnotateProcessor;
import org.openspg.idea.schema.SchemaBundle;
import org.openspg.idea.schema.psi.*;

import java.util.List;

/**
 * Validates namespace has been defined and is not duplicated.
 */
public class SchemaNamespaceProcessor implements AnnotateProcessor {

    /**
     * Emits an error annotation when namespace is undefined or duplicated.
     */
    public boolean process(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof SchemaFile) {
            List<PsiElement> rootOrNamespaces = PsiTreeUtil.getChildrenOfAnyType(element, SchemaNamespace.class, SchemaRootEntity.class);
            if (!rootOrNamespaces.isEmpty()) {
                PsiElement firstChild = rootOrNamespaces.get(0);
                if (!(firstChild instanceof SchemaNamespace)) {
                    holder.newAnnotation(HighlightSeverity.ERROR, element.getText())
                            .range(element.getFirstChild())
                            .tooltip(SchemaBundle.message("SchemaAnnotator.error.first.element.of.document.must.be.a.namespace"))
                            .create();
                }
            }

            List<SchemaNamespace> namespaces = rootOrNamespaces
                    .stream()
                    .filter(x -> x instanceof SchemaNamespace)
                    .map(x -> (SchemaNamespace) x)
                    .toList();
            if (namespaces.isEmpty() && rootOrNamespaces.size() > 1) {
                holder.newAnnotation(HighlightSeverity.ERROR, element.getText())
                        .range(rootOrNamespaces.get(0))
                        .tooltip(SchemaBundle.message("SchemaAnnotator.error.namespace.not.defined"))
                        .create();
            }

            for (int idx = 1; idx < namespaces.size(); idx++) {
                holder.newAnnotation(HighlightSeverity.ERROR, element.getText())
                        .range(namespaces.get(idx))
                        .tooltip(SchemaBundle.message("SchemaAnnotator.error.duplicate.definition.of.namespace"))
                        .create();
            }
        }
        return true;
    }

}
