package org.openspg.idea.conceptRule.reference;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.conceptRule.psi.ConceptRuleConceptType;
import org.openspg.idea.conceptRule.psi.ConceptRuleNamespaceDeclaration;
import org.openspg.idea.schema.SchemaFileType;
import org.openspg.idea.schema.SchemaIcons;
import org.openspg.idea.schema.psi.*;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Stream;

public class ConceptRuleConceptTypeReference extends PsiPolyVariantReferenceBase<ConceptRuleConceptType> {

    private final String myNamespace;
    private final String myEntityName;

    public ConceptRuleConceptTypeReference(@NotNull ConceptRuleConceptType element) {
        super(element, new TextRange(0, element.getTextLength()));
        myNamespace = findNamespace(element.getContainingFile());
        myEntityName = element.getMajorLabel();
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        return streamOfStructureNameDeclaration()
                .filter(x -> x.getStructureName().getFullName().equals(myEntityName))
                .map(PsiElementResolveResult::new)
                .toArray(ResolveResult[]::new);
    }

    @Override
    public Object @NotNull [] getVariants() {
        return streamOfStructureNameDeclaration()
                .map(x -> LookupElementBuilder
                        .create(x)
                        .withIcon(SchemaIcons.FILE)
                        .withTypeText(x.getStructureName().getFullName())
                )
                .toArray(LookupElement[]::new);
    }

    private String findNamespace(PsiFile file) {
        ConceptRuleNamespaceDeclaration namespace = PsiTreeUtil.findChildOfType(file, ConceptRuleNamespaceDeclaration.class);
        if (namespace != null) {
            return namespace.getValue();
        }
        return null;
    }

    private Stream<SchemaStructureNameDeclaration> streamOfStructureNameDeclaration() {
        return streamOfFiles()
                .filter(x -> {
                    if (myNamespace == null) {
                        return false;
                    }
                    SchemaNamespace namespace = PsiTreeUtil.findChildOfType(x, SchemaNamespace.class);
                    return namespace != null && myNamespace.equals(namespace.getNamespace());
                })
                .flatMap(x -> PsiTreeUtil.findChildrenOfType(x, SchemaRootEntity.class).stream())
                .map(SchemaRootEntity::getEntity)
                .map(SchemaEntity::getEntityHead)
                .map(SchemaEntityHead::getBasicStructureDeclaration)
                .map(SchemaBasicStructureDeclaration::getStructureNameDeclaration);
    }

    private Stream<PsiFile> streamOfFiles() {
        Collection<VirtualFile> files = FileTypeIndex.getFiles(
                SchemaFileType.INSTANCE,
                GlobalSearchScope.projectScope(myElement.getProject())
        );
        PsiManager psiManager = PsiManager.getInstance(myElement.getProject());
        return files.stream()
                .map(psiManager::findFile)
                .filter(Objects::nonNull);
    }

}
