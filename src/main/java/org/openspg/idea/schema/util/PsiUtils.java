package org.openspg.idea.schema.util;

import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.schema.psi.SchemaEntity;
import org.openspg.idea.schema.psi.SchemaNamespace;
import org.openspg.idea.schema.psi.SchemaRootEntity;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class PsiUtils {

    public static Map<String, Object> toJson(@NotNull PsiFile file) {
        Map<String, Object> result = new LinkedHashMap<>();

        SchemaNamespace namespace = PsiTreeUtil.getChildOfType(file, SchemaNamespace.class);
        if (namespace != null) {
            result.put("namespace", namespace.toJson());
        }

        result.put("entities", PsiTreeUtil.getChildrenOfTypeAsList(file, SchemaRootEntity.class)
                .stream()
                .map(SchemaRootEntity::getEntity)
                .map(SchemaEntity::toJson)
                .collect(Collectors.toList()));
        return result;
    }

}
