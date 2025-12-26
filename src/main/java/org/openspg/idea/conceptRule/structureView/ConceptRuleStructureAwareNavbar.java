package org.openspg.idea.conceptRule.structureView;

import com.intellij.ide.navigationToolbar.StructureAwareNavBarModelExtension;
import com.intellij.lang.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.openspg.idea.conceptRule.ConceptRuleLanguage;
import org.openspg.idea.conceptRule.psi.ConceptRuleFile;
import org.openspg.idea.conceptRule.psi.ConceptRuleNamespaceDeclaration;
import org.openspg.idea.conceptRule.psi.ConceptRuleRuleWrapperDeclaration;
import org.openspg.idea.schema.SchemaIcons;

import javax.swing.*;

public class ConceptRuleStructureAwareNavbar extends StructureAwareNavBarModelExtension {

    @NotNull
    @Override
    protected Language getLanguage() {
        return ConceptRuleLanguage.INSTANCE;
    }

    @Override
    public @Nullable String getPresentableText(Object object) {
        if (object instanceof ConceptRuleFile file) {
            return file.getName();
        }

        if (object instanceof ConceptRuleNamespaceDeclaration namespace) {
            return namespace
                    .getNamespaceVariable()
                    .getText();
        }

        if (object instanceof ConceptRuleRuleWrapperDeclaration ruleWrapper) {
            return ruleWrapper
                    .getRuleWrapperHead()
                    .getText()
                    .trim();
        }

        return null;
    }

    @Override
    @Nullable
    public Icon getIcon(Object object) {
        if (object instanceof ConceptRuleRuleWrapperDeclaration) {
            return SchemaIcons.Nodes.Entity;
        }

        return null;
    }

}
