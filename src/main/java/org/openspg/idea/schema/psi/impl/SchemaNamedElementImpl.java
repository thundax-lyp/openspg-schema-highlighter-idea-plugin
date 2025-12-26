package org.openspg.idea.schema.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.schema.psi.SchemaNamedElement;

public abstract class SchemaNamedElementImpl extends ASTWrapperPsiElement implements SchemaNamedElement {

    public SchemaNamedElementImpl(@NotNull ASTNode node) {
        super(node);
    }

}
