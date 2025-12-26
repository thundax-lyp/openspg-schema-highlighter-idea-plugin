package org.openspg.idea.schema;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public final class SchemaFileType extends LanguageFileType {

    public static final SchemaFileType INSTANCE = new SchemaFileType();

    private SchemaFileType() {
        super(SchemaLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "OpenSPG Schema File";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "OpenSPG Schema mark language file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "schema";
    }

    @Override
    public Icon getIcon() {
        return SchemaIcons.FILE;
    }

}
