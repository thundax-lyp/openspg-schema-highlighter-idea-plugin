package org.openspg.idea.schema.annotator.processor;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.common.AnnotateProcessor;
import org.openspg.idea.schema.SchemaBundle;
import org.openspg.idea.schema.psi.SchemaEntity;
import org.openspg.idea.schema.psi.SchemaFile;
import org.openspg.idea.schema.psi.SchemaRootEntity;

import java.util.List;

/**
 * Validates namespace has been defined and is not duplicated.
 */
public class SchemaRootEntityProcessor implements AnnotateProcessor {

    /**
     * Emits an error annotation when namespace is undefined or duplicated.
     */
    public boolean process(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof SchemaFile) {
            List<SchemaEntity> entities = PsiTreeUtil.getChildrenOfTypeAsList(element, SchemaRootEntity.class)
                    .stream()
                    .map(SchemaRootEntity::getEntity)
                    .toList();
            entities.forEach(entity -> {
                String currentName = entity.getName();
                long count = entities.stream().filter(x -> x.getName().equals(currentName)).count();
                if (count > 1) {
                    holder.newAnnotation(HighlightSeverity.ERROR, element.getText())
                            .range(entity.getEntityHead()
                                    .getBasicStructureDeclaration()
                                    .getStructureNameDeclaration()
                            )
                            .tooltip(SchemaBundle.message(
                                    "SchemaAnnotator.error.cannot.redeclare.block.scoped.schema",
                                    currentName))
                            .create();
                }
            });
        }
        return true;
    }

}
