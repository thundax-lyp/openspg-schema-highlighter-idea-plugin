package org.openspg.idea.schema;

import com.intellij.lang.Language;

public final class SchemaLanguage extends Language {

    public static final SchemaLanguage INSTANCE = new SchemaLanguage();

    private SchemaLanguage() {
        super("OpenSPG Schema");
    }

}
