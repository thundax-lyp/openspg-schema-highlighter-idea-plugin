package org.openspg.idea.schema.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.schema.psi.SchemaEntity;
import org.openspg.idea.schema.psi.SchemaEntityHead;
import org.openspg.idea.schema.psi.SchemaPropertyNameVariable;
import org.openspg.idea.schema.psi.SchemaPropertyValueVariable;
import org.openspg.idea.schema.psi.SchemaVariableStructureType;

final class SchemaCompletionContributor extends CompletionContributor {

    private static final String[] BUILTIN_PROPERTY_NAMES = {
            "desc",
            "properties",
            "relations",
            "hypernymPredicate",
            "regular",
            "spreadable",
            "autoRelate",
            "constraint",
            "rule",
            "index"
    };

    private static final String[] BUILTIN_PROPERTY_VALUES = {
            "isA",
            "locateAt",
            "mannerOf",
            "text",
            "vector",
            "sparseVector",
            "textAndVector",
            "textAndSparseVector",
            "notNull",
            "multiValue"
    };

    SchemaCompletionContributor() {
        this.extendCompletionForEntityType();
        this.extendCompletionForPropertyNames();
        this.extendCompletionForPropertyValues();
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
                            String currentName = currentHead.getBasicStructureDeclaration().getName();
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

    private void extendCompletionForPropertyNames() {
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(SchemaPropertyNameVariable.class), new CompletionProvider<>() {
            @Override
            public void addCompletions(@NotNull CompletionParameters parameters,
                                       @NotNull ProcessingContext context,
                                       @NotNull CompletionResultSet resultSet) {
                for (String keyword : BUILTIN_PROPERTY_NAMES) {
                    resultSet.addElement(LookupElementBuilder.create(keyword));
                }
            }
        });
    }

    private void extendCompletionForPropertyValues() {
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(SchemaPropertyValueVariable.class), new CompletionProvider<>() {
            @Override
            public void addCompletions(@NotNull CompletionParameters parameters,
                                       @NotNull ProcessingContext context,
                                       @NotNull CompletionResultSet resultSet) {
                for (String keyword : BUILTIN_PROPERTY_VALUES) {
                    resultSet.addElement(LookupElementBuilder.create(keyword));
                }
            }
        });
    }

}
