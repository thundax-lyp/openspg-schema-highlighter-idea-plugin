package org.openspg.idea.schema.reference;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiPolyVariantReferenceBase;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.schema.SchemaIcons;
import org.openspg.idea.schema.psi.*;

public class SchemaVariableStructureTypeReference extends PsiPolyVariantReferenceBase<SchemaVariableStructureType> {

    private final String myEntityName;

    public SchemaVariableStructureTypeReference(@NotNull SchemaVariableStructureType element) {
        super(element, new TextRange(0, element.getTextLength()));
        this.myEntityName = element.getText();
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        return PsiTreeUtil.findChildrenOfType(myElement.getContainingFile(), SchemaRootEntity.class)
                .stream()
                .map(SchemaRootEntity::getEntity)
                .map(SchemaEntity::getEntityHead)
                .map(SchemaEntityHead::getBasicStructureDeclaration)
                .map(SchemaBasicStructureDeclaration::getStructureNameDeclaration)
                .map(SchemaStructureNameDeclaration::getStructureName)
                .filter(x -> x.getText().equals(this.myEntityName))
                .map(PsiElementResolveResult::new)
                .toArray(ResolveResult[]::new);
    }

    @Override
    public Object @NotNull [] getVariants() {
        return PsiTreeUtil.getChildrenOfTypeAsList(myElement.getContainingFile(), SchemaRootEntity.class)
                .stream()
                .map(x -> LookupElementBuilder
                        .create(x)
                        .withIcon(SchemaIcons.FILE)
                        .withTypeText(x.getContainingFile().getName())
                )
                .toArray(LookupElement[]::new);
    }

}
