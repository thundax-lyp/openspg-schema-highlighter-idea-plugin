package org.openspg.idea.conceptRule.lexer;

import com.intellij.lexer.FlexAdapter;

public class ConceptRuleLexerAdapter extends FlexAdapter {

    public ConceptRuleLexerAdapter() {
        super(new ConceptRuleLexer(null));
    }

}
