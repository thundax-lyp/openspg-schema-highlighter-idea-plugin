package org.openspg.idea.schema.annotator.processor;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.common.AnnotateProcessor;
import org.openspg.idea.schema.SchemaBundle;
import org.openspg.idea.schema.action.SchemaCreateTypeQuickFix;
import org.openspg.idea.schema.psi.SchemaVariableStructureType;
import org.openspg.idea.schema.reference.SchemaVariableStructureTypeReference;

/**
 * Validates inherited types have been defined.
 */
public class SchemaInheritedStructureTypeProcessor implements AnnotateProcessor {

    /**
     * Emits an error annotation when inherited type is undefined.
     */
    public boolean process(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof SchemaVariableStructureType structureType) {
            SchemaVariableStructureTypeReference reference = (SchemaVariableStructureTypeReference) structureType.getReference();
            if (reference != null && reference.multiResolve(false).length == 0) {
                holder.newAnnotation(HighlightSeverity.ERROR, element.getText())
                        .range(element.getTextRange())
                        .tooltip(SchemaBundle.message(
                                "SchemaAnnotator.error.undefined.type",
                                structureType.getStructureName().getStructureRealName().getText()))
                        .withFix(new SchemaCreateTypeQuickFix(structureType.getText()))
                        .create();
            }
        }
        return true;
    }

}
