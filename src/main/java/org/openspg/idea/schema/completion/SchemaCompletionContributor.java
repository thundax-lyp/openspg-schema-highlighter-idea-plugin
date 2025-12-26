package org.openspg.idea.schema.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.schema.psi.SchemaEntity;
import org.openspg.idea.schema.psi.SchemaEntityHead;
import org.openspg.idea.schema.psi.SchemaVariableStructureType;

final class SchemaCompletionContributor extends CompletionContributor {

    SchemaCompletionContributor() {
        this.extendCompletionForEntityType();
    }

    private void extendCompletionForEntityType() {
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(SchemaVariableStructureType.class), new CompletionProvider<>() {
                    public void addCompletions(@NotNull CompletionParameters parameters,
                                               @NotNull ProcessingContext context,
                                               @NotNull CompletionResultSet resultSet) {
                        resultSet.addElement(LookupElementBuilder.create("EntityType"));
                        resultSet.addElement(LookupElementBuilder.create("ConceptType"));
                        resultSet.addElement(LookupElementBuilder.create("EventType"));
                        resultSet.addElement(LookupElementBuilder.create("StandardType"));
                        resultSet.addElement(LookupElementBuilder.create("BasicType"));

                        // for inherited-structure
                        SchemaEntityHead currentHead = (SchemaEntityHead) PsiTreeUtil.findFirstParent(parameters.getPosition(), SchemaEntityHead.class::isInstance);
                        if (currentHead != null
                                && currentHead.getBasicStructureDeclaration().getStructureTypeDeclaration().getInheritedStructureTypeDeclaration() != null) {
                            String currentName = currentHead.getName();
                            PsiTreeUtil.findChildrenOfType(parameters.getOriginalFile(), SchemaEntity.class).forEach(entity -> {
                                if (!entity.getName().equals(currentName)) {
                                    resultSet.addElement(LookupElementBuilder.create(entity.getName()));
                                }
                            });
                        }

                    }
                }
        );
    }

}
