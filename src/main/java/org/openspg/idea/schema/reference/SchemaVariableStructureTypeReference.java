package org.openspg.idea.schema.reference;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.schema.SchemaIcons;
import org.openspg.idea.schema.SchemaFileType;
import org.openspg.idea.schema.psi.*;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class SchemaVariableStructureTypeReference extends PsiPolyVariantReferenceBase<SchemaVariableStructureType> {

    private final String myEntityName;

    public SchemaVariableStructureTypeReference(@NotNull SchemaVariableStructureType element) {
        super(element, new TextRange(0, element.getTextLength()));
        myEntityName = element.getStructureName().getFullName();
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

    private Stream<SchemaStructureNameDeclaration> streamOfStructureNameDeclaration() {
        return PsiTreeUtil.findChildrenOfType(myElement.getContainingFile(), SchemaRootEntity.class)
                .stream()
                .map(SchemaRootEntity::getEntity)
                .map(SchemaEntity::getEntityHead)
                .map(SchemaEntityHead::getBasicStructureDeclaration)
                .map(SchemaBasicStructureDeclaration::getStructureNameDeclaration);
    }

    private List<PsiFile> findFiles() {
        Collection<VirtualFile> files = FileTypeIndex.getFiles(
                SchemaFileType.INSTANCE,
                GlobalSearchScope.projectScope(myElement.getProject())
        );
        PsiManager psiManager = PsiManager.getInstance(myElement.getProject());
        return files.stream()
                .map(psiManager::findFile)
                .filter(Objects::nonNull)
                .toList();
    }
}
