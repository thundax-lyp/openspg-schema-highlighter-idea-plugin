package org.openspg.idea.conceptRule;

import com.intellij.lang.Language;

public final class ConceptRuleLanguage extends Language {

    public static final ConceptRuleLanguage INSTANCE = new ConceptRuleLanguage();

    private ConceptRuleLanguage() {
        super("OpenSPG Concept Rule");
    }

}
