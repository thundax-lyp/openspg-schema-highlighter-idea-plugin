package org.openspg.idea.schema;

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.schema.psi.SchemaEntityHead;
import org.openspg.idea.schema.psi.SchemaProperty;
import org.openspg.idea.schema.psi.SchemaVariableStructureType;

import java.util.Collection;

public final class SchemaLineMarkerProvider extends RelatedItemLineMarkerProvider {

    @Override
    protected void collectNavigationMarkers(@NotNull PsiElement element,
                                            @NotNull Collection<? super RelatedItemLineMarkerInfo<?>> result) {
        if (element instanceof SchemaEntityHead entityHead) {
            handleEntityHead(entityHead, result);
        }
    }

    private void handleEntityHead(@NotNull SchemaEntityHead entityHead,
                                  @NotNull Collection<? super RelatedItemLineMarkerInfo<?>> result) {
        PsiElement propertyElement = PsiTreeUtil.findFirstParent(entityHead, (PsiElement element) -> element instanceof SchemaProperty);
        if (propertyElement != null) {
            return;
        }

        String entityName = entityHead.getBasicStructureDeclaration().getName();

        PsiElement[] targetElements = PsiTreeUtil.findChildrenOfType(entityHead.getContainingFile(), SchemaVariableStructureType.class)
                .stream()
                .filter(variableStructureType -> variableStructureType.getText().equals(entityName))
                .toArray(PsiElement[]::new);

        NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder
                .create(SchemaIcons.FILE)
                .setTargets(targetElements)
                .setTooltipText(SchemaBundle.message("SchemaLineMarkerProvider.navigate.to.usages"));

        result.add(builder.createLineMarkerInfo(PsiTreeUtil.getDeepestFirst(entityHead)));
    }

}
