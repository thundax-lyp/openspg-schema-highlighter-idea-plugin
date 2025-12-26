package org.openspg.idea.schema.lexer;

import com.intellij.lexer.FlexAdapter;

public class SchemaLexerAdapter extends FlexAdapter {

    public SchemaLexerAdapter() {
        super(new SchemaLexer(null));
    }

}
