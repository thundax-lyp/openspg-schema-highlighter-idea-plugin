package org.openspg.idea.conceptRule;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.schema.SchemaIcons;

import javax.swing.*;

public final class ConceptRuleFileType extends LanguageFileType {

    public static final ConceptRuleFileType INSTANCE = new ConceptRuleFileType();

    private ConceptRuleFileType() {
        super(ConceptRuleLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "OpenSPG Concept Rule File";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "OpenSPG Concept rule file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "rule";
    }

    @Override
    public Icon getIcon() {
        return SchemaIcons.FILE;
    }

}
