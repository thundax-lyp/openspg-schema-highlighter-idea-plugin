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
import java.util.Objects;
import java.util.stream.Stream;

public class SchemaVariableStructureTypeReference extends PsiPolyVariantReferenceBase<SchemaVariableStructureType> {

    private final String myNamespace;
    private final String myEntityName;

    public SchemaVariableStructureTypeReference(@NotNull SchemaVariableStructureType element) {
        super(element, new TextRange(0, element.getTextLength()));
        myNamespace = findNamespace(element.getContainingFile());
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

    private String findNamespace(PsiFile file) {
        SchemaNamespace namespace = PsiTreeUtil.findChildOfType(file, SchemaNamespace.class);
        if (namespace != null) {
            return namespace.getNamespace();
        }
        return null;
    }

    private Stream<SchemaStructureNameDeclaration> streamOfStructureNameDeclaration() {
        return streamOfFiles()
                .filter(x -> {
                    if (x == myElement.getContainingFile()) {
                        return true;
                    }
                    return myNamespace == null || myNamespace.equals(findNamespace(x));
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
